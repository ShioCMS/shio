<?php

namespace Japode\Login\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Japode\Base\Base;

class RecoverCtrl extends Base {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/recover/', array(new self(), 'recover'))->bind('recover');
        $routing->post('/recover/process/', array(new self(), 'recoverProcess'))->bind('recoverProcess');
        $routing->get('/newpassword/{guid}/', array(new self(), 'newPassword'))->bind('newPassword');
        $routing->post('/newpassword/process/', array(new self(), 'newPasswordProcess'))->bind('newPasswordProcess');
    }

    public function recover(Application $app, Request $request) {

        return $app['twig']->render('login/recover.twig');
    }

    public function newPassword(Application $app, Request $request, $guid) {
        $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByRecoverPassword($guid);


        return $app['twig']->render('login/new_password.twig', array(
                    'guid' => $guid,
                    'username' => $user->getUserName()
        ));
    }

    public function newPasswordProcess(Application $app, Request $request) {
        try {
            $guid = $request->get('guid');
            $password = $request->get('password');

            $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByRecoverPassword($guid);
            $user->setRecoverPassword(null);
            $user->setPassword(md5($password));

            $email = $user->getEmail();
            $app['db.orm']->persist($user);
            $app['db.orm']->flush();

            return $app->redirect($app['url_generator']->generate('login') . '?reset_password=1');
        } catch (Exception $e) {
            return $app->redirect($app['url_generator']->generate('login') . '?reset_password=0');
        }
    }

    public function recoverProcess(Application $app, Request $request) {
        try {
            $guid = $this->getGUID();
            $email = $request->get('email');

            $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByEmail($email);
            $user->setRecoverPassword($guid);
            $app['db.orm']->persist($user);
            $app['db.orm']->flush();
            $message = \Swift_Message::newInstance()
                    ->setSubject('Japode.com password reset request received')
                    ->setFrom(array('info@japode.com' => 'Japode'))
                    ->setTo(array($email));

            $message->setBody("Hey there,\n\n" .
                    "Someone just requested a password reset for your account on japode.com. If this was you, click on the link below to reset your password and create a new one.\n\n" .
                    "http://" . $GLOBALS['japode_domain'] . "/login/newpassword/" . $guid . "/ \n\n" .
                    "Thanks!\n\n" .
                    "Japode");
            $app['mailer']->send($message);
            return $app['twig']->render('login/recover_process.twig', array(
                        'email' => $email
            ));
        } catch (Exception $e) {
            return Response("Recover Password Error", 500);
        }
    }

}
