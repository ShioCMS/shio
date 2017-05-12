<?php

namespace Japode\Services\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Response;
use Japode\Post\Post;
use Japode\PostType\PostType;
use DateTime;

class FURLCtrl {

    static public function addRoutes($routing) {
        $routing->get('/furl/{furlname}/', array(new self(), 'furl'))->bind('furl');
    }

    public function furl(Application $app, $furlname) {
        $jpPost = new Post();

        $jpPostbject = $jpPost->getPostByFURL($furlname);
        $jpPostAttribObject = $jpPostbject->getAttributes();
        $jpPostJson = array();
        $jpCTD = new PostType();
        $jpCTDObject = $jpCTD->listByID($jpPostbject->getIDPostType());
        foreach ($jpPostAttribObject as $attribute) {
            $jpCTDAttributeObject = $jpCTD->getAttribute($attribute->getIDAttribute());
            $jpPostJson[$jpCTDAttributeObject->getName()] = $attribute->getStrValue();
        }
        $mdate = new DateTime($jpPostbject->getDate());
        
        $jpPostJson[site_name] = 'Blog';
        $jpPostJson[author] = 'Alexandre Oliveira';
        $jpPostJson[id] = $jpPostbject->getID();
        $jpPostJson[posttype] = $jpPostbject->getIDPostType();
        $jpPostJson[posttypename] = $jpCTDObject->getTitle();
        $jpPostJson[content] = $jpPostbject->getTitle();
        $jpPostJson[description] = $jpPostbject->getSummary();
        $jpPostJson[label] = "SiteA";
        $jpPostJson[date] = $mdate->format('Y-m-d');
        $jpPostJson[date_format] = $mdate->format('d F Y');


        $response = new Response(json_encode($jpPostJson));
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
        $response->headers->set('Content-Type', 'application/json');

        return $response;
    }

}
