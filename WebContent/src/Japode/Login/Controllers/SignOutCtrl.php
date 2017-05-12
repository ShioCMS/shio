<?php

namespace Japode\Login\Controllers;

use Silex\Application;

class SignOutCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        /* Set corresponding endpoints on the controller classes */
        $routing->get('/', array(new self(), 'signOut'))->bind('signOut');
    }

    public function signOut(Application $app) {
        session_destroy();
        return $app->redirect('/');
    }

}
