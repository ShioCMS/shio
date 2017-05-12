<?php

namespace Japode\Post;

use Silex\Application;
use Japode\Util\Util;
use Japode\Post\Entity\JPPost;
use Japode\Post\Entity\JPPostAttr;

class PostService {

    var $app;

    public function __construct(Application $app) {
        $this->app = $app;
    }

    public function newEmpty($postTypeId) {

        $postType = $this->app['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($postTypeId);
        $post = new JPPost();
        $post->setId(Util::getGUID());
        $post->setDate(new \DateTime());
        $post->setPostType($postType);
        $post->setTitle("...");
        $post->setSummary("...");

        $this->app['db.orm']->merge($post);

        $postTypeAttrs = $postType->getPostTypeAttrs();
        foreach ($postTypeAttrs as $postTypeAttr) {

            $postAttr = new JPPostAttr();
            $postAttr->setId(Util::getGUID());
            $postAttr->setPost($post);
            $postAttr->setPostType($postType);
            $postAttr->setPostTypeAttr($postTypeAttr);
            $postAttr->setType('1');
            $postAttr->setStrValue("...");
            $this->app['db.orm']->merge($postAttr);
            $post->addPostAttr($postAttr);
        }

        $this->app['db.orm']->merge($post);
        $this->app['db.orm']->flush();

        return $post;
    }

}
