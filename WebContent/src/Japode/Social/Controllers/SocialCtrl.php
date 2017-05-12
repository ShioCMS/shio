<?php

namespace Japode\Social\Controllers;

use Silex\Application;
use Japode\User\User;

class SocialCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/', array(new self(), 'root'))->bind('root');
        $routing->get('/{username}/', array(new self(), 'pageUser'))->bind('pageUser');
    }

    function root(Application $app) {
        if (!isset($_SESSION['userid'])) {
            $_SESSION['callback'] = $_SERVER['REQUEST_URI'];
            return $app->redirect('/intro');
        } else {
            return $this->social($app);
        }
    }

    function social(Application $app) {
        $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        return $app['twig']->render('fullscreen.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'lastname' => $user->getLastName(),
                    'label.title' => 'label.title',
                    'post.label' => 'post.label',
                    'post.content' => 'post.content',
                    'post.description' => 'post.description',
                    'post.date' => 'post.date',
                    'message' => 'your social life',
        ));
    }
    function pageUser(Application $app, $username) {
        $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->findByUsername($username);
        if (isset($user)) {
        return $app['twig']->render('pageUser.twig', array(
                    'user' => $user
            ));
        }
        return $app['twig']->render('404.twig');
    }

}
