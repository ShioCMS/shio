<?php

namespace Japode\Services\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Response;
use Japode\Post\Post;
use Japode\PostType\PostType;

class PostCtrl {

    static public function addRoutes($routing) {
        $routing->get('/post/{posttypeid}/{postid}/', array(new self(), 'post'))->bind('post');
    }

    public function post(Application $app, $postypename, $postid) {
        $jpPost = new Post();

        $jpPostbject = $jpPost->getPostByID($postid);
        $jpPostAttribObject = $jpPostbject->getAttributes();
        $jpPostJson = array();
        $jpCTD = new PostType();
        foreach ($jpPostAttribObject as $attribute) {
            $jpCTDAttributeObject = $jpCTD->getAttribute($attribute->getIDAttribute());
            $jpPostJson[$jpCTDAttributeObject->getName()] = $attribute->getStrValue();
        }
        
        $response = new Response(json_encode($jpPostJson));
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
        $response->headers->set('Content-Type', 'application/json');

        return $response;
    }

}
