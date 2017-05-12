<?php

namespace Japode\Login\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Japode\Login\Controllers\SignUpCtrl;
use Japode\User\UserService;
use Japode\User\Entity\JPUser;

class SignInCtrl {

    var $silexApp;

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        /* Set corresponding endpoints on the controller classes */
        $routing->post('/{realm}/', array(new self(), 'signInProcess'))->bind('signInPost');
        $routing->get('/{realm}/', array(new self(), 'signInProcess'))->bind('signInGet');
    }

    public function signInProcess(Application $app, $realm, Request $request) {

        $this->silexApp = $app;
        if ($realm == 'fb') {
            return $this->loginFacebookProcess();
        } else {
            return $this->loginProcess($request->get('username'), $request->get('password'));
        }
    }

    function loginFacebookProcess() {
        if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['code'])) {
            // Informe o seu App ID abaixo
            $appId = $GLOBALS['facebook_appid'];

            // Digite o App Secret do seu aplicativo abaixo:
            $appSecret = $GLOBALS['facebook_appsecret'];

            // Url informada no campo "Site URL"
            $redirectUri = urlencode($GLOBALS['facebook_redirect']);

            // Obtém o código da query string
            $code = $_GET['code'];

            // Monta a url para obter o token de acesso e assim obter os dados do usuário
            $token_url = "https://graph.facebook.com/oauth/access_token?"
                    . "client_id=" . $appId . "&redirect_uri=" . $redirectUri
                    . "&client_secret=" . $appSecret . "&code=" . $code;

            //pega os dados
            $response = @file_get_contents($token_url);
            if ($response) {
                $params = null;
                parse_str($response, $params);
                if (isset($params['access_token']) && $params['access_token']) {
                    $graph_url = "https://graph.facebook.com/me?access_token="
                            . $params['access_token'];
                    $jsonUser = json_decode(file_get_contents($graph_url));

                    $user = new JPUser();
                    $user
                            ->setId($jsonUser->id)
                            ->setEmail($jsonUser->email)
                            ->setFirstName($jsonUser->first_name)
                            ->setLastName($jsonUser->last_name)
                            ->setRealm('facebook');


                    // nesse IF verificamos se veio os dados corretamente
                    if ($user->getId() != null) {
                        if ($this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->userExists($user->getRealm(), $user->getId())) {
                            $_SESSION['userid'] = $user->getId();
                            $_SESSION['realm'] = $user->getRealm();
                            UserService::setLastLogin($this->silexApp, $user);
                            if (!isset($_SESSION['callback'])) {
                                return $this->silexApp->redirect('/content');
                            } else {
                                return $this->callback();
                            }
                        } else {
                            $signUp = new SignUpCtrl();
                            return $signUp->signUpSave($this->silexApp, $user, $user->getRealm(), $user->getId());
                        }
                    }
                } else {
                    return new Response("Erro de conexão com Facebook", 500);
                }
            } else {
                return new Response("Erro de conexão com Facebook", 500);
            }
        } else if ($_SERVER['REQUEST_METHOD'] == 'GET' && isset($_GET['error'])) {
            return new Response('Permissão não concedida', 500);
        }
    }

    function loginProcess($_email, $_password) {
        if ($this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->checkPassword($_email, $_password)) {
            session_start();
            $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByEmail($_email);
            UserService::setLastLogin($this->silexApp, $user);
            $_SESSION['userid'] = $_email;
            $_SESSION['realm'] = 'default';
            if (!isset($_SESSION['callback']))
                return $this->silexApp->redirect('/content');
            else
                return $this->callback();
        }
        else {
            return new Response('Usuario ou senha incorreta', 500);
        }
    }

    function callback() {
        $callback = $_SESSION['callback'];
        unset($_SESSION['callback']);
        return $this->silexApp->redirect($callback);
    }

}
