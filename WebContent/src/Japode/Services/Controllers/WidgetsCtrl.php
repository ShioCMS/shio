<?php

namespace Japode\Services\Controllers;

use Silex\Application;

class WidgetsCtrl {

    static public function addRoutes($routing) {
        $routing->get('/widgets/', array(new self(), 'widgets'))->bind('widgets');
    }

    public function widgets(Application $app) {
        $widgets = $app['db.orm']->getRepository('Japode\Widget\Entity\JPWidget')->findAll();

        $response = $app->json($widgets);
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');

        return $response;
    }

}
