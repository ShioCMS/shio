<?php

namespace Japode\Content\Controllers;

use Silex\Application;
use Japode\User\User;
use Japode\PostType\PostType;

class Content {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/', array(new self(), 'content'))->bind('content');
    }

    public function content(Application $app) {
        $user = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        if ($user->getLastPostType() != null) {
            $lastPostType = $app['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($user->getLastPostType());
        }
        return $app['twig']->render('home.twig', array(
                    'user' => $user,
                    'lastPostType' => $lastPostType,
                    'label.title' => 'label.title',
                    'post.label' => 'post.label',
                    'post.content' => 'post.content',
                    'post.description' => 'post.description',
                    'post.date' => 'post.date',
                    'message' => 'your content',
        ));
    }
}
