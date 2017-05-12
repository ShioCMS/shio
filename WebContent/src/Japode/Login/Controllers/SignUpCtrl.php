<?php

namespace Japode\Login\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Japode\Base\Base;
use Japode\User\UserService;
use Japode\User\Entity\JPUser;

class SignUpCtrl extends Base {

    var $silexApp;

// Connects the routes in Silex
    static public function addRoutes($routing) {
        /* Set corresponding endpoints on the controller classes */
        $routing->get('/', array(new self(), 'signUp'))->bind('signUp');
        $routing->post('/process/', array(new self(), 'signUpProcess'))->bind('signUpProcess');
        $routing->get('/confirm/{guid}/', array(new self(), 'confirmEmail'))->bind('confirmEmail');
    }

    public function signUp(Application $app) {
        return $app['twig']->render('signup.twig');
    }

    public function signUpProcess(Application $app, Request $request) {

        $this->silexApp = $app;

        $user = new JPUser();
        $user
                ->setUsername($request->get('username'))
                ->setEmail($request->get('email'))
                ->setFirstName($request->get('firstname'))
                ->setLastName($request->get('lastname'))
                ->setPassword(md5($request->get('password')))
        ;

        return $this->signUpSave($app, $user);
    }

    public function confirmEmail(Application $app, Request $request, $guid) {
        try {
            $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByConfirmEmail($guid);
            $user->setConfirmEmail(null);
            $app['db.orm']->persist($user);
            $app['db.orm']->flush();

            return $app->redirect($app['url_generator']->generate('login') . '?confirmemail=1');
        } catch (Exception $e) {
            return $app->redirect($app['url_generator']->generate('login') . '?invalidtoken=1');
        }
    }

    function signUpSave(Application $app, JPUser $user, $_realm = 'default', $_id = NULL) {
        try {
            if ($_realm == 'default') {
                $user->setId($user->getEmail());
            } else {
                $user->setId($_id);
            }

            $user->setRealm($_realm);

            $app['db.orm']->persist($user);

            $app['db.orm']->flush();
            $_SESSION['userid'] = $user->getID();
            $_SESSION['realm'] = $user->getRealm();
            UserService::setLastLogin($app, $user);
            $this->sendConfirmEmail($app, $user->getEmail());

            if (!isset($_SESSION['callback'])) {
                return $app->redirect('/content');
            } else {
                return $app->redirect($_SESSION['callback']);
            }
        } catch (Exception $e) {
            return new Response('Save User Error', 500);
        }
    }

    public function sendConfirmEmail(Application $app, $email) {
        try {
            $guid = $this->getGUID();
            $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByEmail($email);
            $user->setConfirmEmail($guid);
            $app['db.orm']->persist($user);
            $app['db.orm']->flush();

            $message = \Swift_Message::newInstance()
                    ->setSubject('Welcome to Japode.com - Please confirm your email')
                    ->setFrom(array('info@japode.com' => 'Japode'))
                    ->setTo(array($email));

            $message->setBody("Hey there,\n\n" .
                    "You're in! Thanks for signing up for an account on Japode.com - to keep the dirty spammers out and make sure you're a human, please click the link below to confirm your email address.\n\n" .
                    "http://" . $GLOBALS['japode_domain'] . "/signup/confirm/" . $guid . "/ \n\n" .
                    "Thanks!\n\n" .
                    "Japode");
            $app['mailer']->send($message);
            return true;
        } catch (Exception $e) {
            return false;
        }
    }

}
