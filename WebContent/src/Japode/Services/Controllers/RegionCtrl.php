<?php

namespace Japode\Services\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Response;
use Japode\Post\PostService;
use Japode\Region\Entity\JPRegion;
use Japode\Util\Util;

class RegionCtrl {

    static public function addRoutes($routing) {
        $routing->get('/region/{regionName}/{postTypeName}/', array(new self(), 'region'))->bind('region');
    }

    public function region(Application $app, $regionName, $postTypeName) {
        $region = $app['db.orm']->getRepository('Japode\Region\Entity\JPRegion')->findByName($regionName);
        $postType = $app['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findByName($postTypeName);
        if ($region == NULL) {
            if ($postType != NULL) {
                $region = new JPRegion();
                $postService = new PostService($app);
                $post = $postService->newEmpty($postType->getId());
                $region->setId(Util::getGUID());
                $region->setPost($post);
                $region->setPostType($postType);
                $region->setName($regionName);

                $app['db.orm']->merge($region);
                $app['db.orm']->flush();
            }
        } else {
            $post = $region->getPost();
        }

        $postAttrs = $post->getPostAttrs();

        $json = array();
        $json['id'] = $post->getId();
        foreach ($postAttrs as $postAttr) {
            $postTypeAttr = $postAttr->getPostTypeAttr();
            $widget = $postTypeAttr->getWidget();
            if ($widget->getId() == 'file') {
                $json[$postTypeAttr->getLabel()] = 'http://' . $GLOBALS['japode_domain'] . $postAttr->getStrValue();
            } else {
                $json[$postTypeAttr->getLabel()] = $postAttr->getStrValue();
            }
        }

        $response = new Response(json_encode($json));
        $response->headers->set('Access-Control-Allow-Origin', '*');
        $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
        $response->headers->set('Content-Type', 'application/json');

        return $response;
    }

}
