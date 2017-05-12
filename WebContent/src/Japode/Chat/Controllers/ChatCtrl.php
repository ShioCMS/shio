<?php

namespace Japode\Chat\Controllers;

use Silex\Application;
use Japode\User\User;
use Japode\User\UserObject;
class ChatCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/', array(new self(), 'chat'))->bind('chat');
    }

    public function chat(Application $app) {
        $jpUser = new User();
        $jpUserObject = $jpUser->getUser();
        return $app['twig']->render('chat.twig', array(
                     'user' => $jpUserObject,
                    'message' => 'chat with your friends',
        ));
    }

}
