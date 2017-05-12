<?php

namespace Japode\Services\Controllers;

use Silex\Application;

class PostsCtrl {

    static public function addRoutes($routing) {
        $routing->get('/posts/', array(new self(), 'posts'))->bind('posts');
    }

    public function posts(Application $app) {
        $posts = $app['db.orm']->getRepository('Japode\Post\Entity\JPPost')->findAll();

        $response = $app->json($posts);
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
        $response->headers->set('Content-Type', 'application/json');

        return $response;
    }

}
