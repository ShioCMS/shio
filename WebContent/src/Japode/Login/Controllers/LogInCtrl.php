<?php

namespace Japode\Login\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;

class LogInCtrl {
     // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/', array(new self(), 'logIn'))->bind('login');
    }
    
      public function logIn(Application $app, Request $request ) {
        return $app['twig']->render('login.twig', array(
                    'facebook_appid' => $GLOBALS['facebook_appid'],
                    'facebook_redirect' => urlencode($GLOBALS['facebook_redirect']),                
        ));
    }
}
