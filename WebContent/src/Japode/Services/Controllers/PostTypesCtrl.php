<?php

namespace Japode\Services\Controllers;

use Silex\Application;

class PostTypesCtrl {

    static public function addRoutes($routing) {
        $routing->get('/posttypes/', array(new self(), 'postTypes'))->bind('postTypes');
    }

    public function postTypes(Application $app) {

        $postType = $app['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findAll();

        $response = $app->json($postType);
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');

        return $response;
    }

}
