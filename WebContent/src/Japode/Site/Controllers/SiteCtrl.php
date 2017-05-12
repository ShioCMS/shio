<?php

namespace Japode\Site\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Japode\Util\Util;
use Japode\Site\Entity\JPSite;

class SiteCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/editor/', array(new self(), 'newSite'))->bind('newSite');
        $routing->post('/process/basic/', array(new self(), 'siteProcess'))->bind('siteProcess');
    }

    public function newSite(Application $app) {
        $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        return $app['twig']->render('siteEdit.twig', array(
                    'site' => new JPSite(),
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'lastname' => $user->getLastName(),
                    'email' => $user->getEmail(),
                    'message' => 'create your new site'
        ));
    }

    public function siteProcess(Application $app, Request $request) {
        $site = new JPSite();
        $site->setId(Util::getGUID());
        
        $site
                ->setName($request->get('name'))
                ->setUrl($request->get('url'))
                ->setDescription($request->get('description'))
        ;

        $app['db.orm']->persist($site);
        $app['db.orm']->flush();

        return $app->redirect('/content');
    }

}
