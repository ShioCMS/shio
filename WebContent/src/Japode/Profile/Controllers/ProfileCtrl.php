<?php

namespace Japode\Profile\Controllers;

use Silex\Application;
use Japode\User\User;

class ProfileCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/', array(new self(), 'profile'))->bind('profile');
        $routing->get('/basic/', array(new self(), 'profileBasic'))->bind('basic');
    }

    public function profile(Application $app) {
        return $app->redirect('/profile/basic/');
    }

    public function profileBasic(Application $app) {
        $jpUser = new User();
        $jpUserObject = $jpUser->getUser();
        return $app['twig']->render('profile.twig', array(
                    'user' => $jpUserObject,
                    'firstname' => $jpUserObject->getFirstName(),
                    'lastname' => $jpUserObject->getLastName(),
                    'email' => $jpUserObject->getEmail(),
                    'message' => 'edit your profile',
        ));
    }

}
