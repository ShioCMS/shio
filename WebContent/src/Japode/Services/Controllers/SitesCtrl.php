<?php

namespace Japode\Services\Controllers;

use Silex\Application;

class SitesCtrl {

    static public function addRoutes($routing) {
        $routing->get('/sites/', array(new self(), 'sites'))->bind('sites');
    }

    public function sites(Application $app) {
        $sites = $app['db.orm']->getRepository('Japode\Site\Entity\JPSite')->findAll();

        $response = $app->json($sites);
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');

        return $response;
    }

}
