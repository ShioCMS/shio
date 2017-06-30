<?php

namespace Japode\Post\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Response;
use Symfony\Component\HttpFoundation\Request;
use Japode\Post\Entity\JPPost;
use Japode\Post\Entity\JPPostAttr;
use Japode\PostType\PostTypeAttrService;
use Japode\Util\Util;

class PostCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/{action}/', array(new self(), 'post'))->bind('post');
        $routing->get('/{action}/{param1}/', array(new self(), 'post'))->bind('post1');
        $routing->get('/{action}/{param1}/{param2}/', array(new self(), 'post'))->bind('post2');
        $routing->post('/{action}/', array(new self(), 'postProcess'))->bind('postProcess');
    }

    var $silexApp;

    public function post(Application $app, $action, $param1 = NULL, $param2 = NULL) {
        $this->silexApp = $app;
        $_id_posttype = $param1;
        $_id_post = $param2;
        switch ($action) {
            case 'select':
                return $this->selectPost();
                break;
            case 'editor':
                if ($_id_post == NULL) {
                    return $this->newPost($_id_posttype);
                } else {
                    return $this->editPost($_id_posttype, $_id_post);
                }
                break;
            case 'editorform':
                if ($_id_post == NULL) {
                    $response = new Response($this->newPostForm($_id_posttype));
                    $response->headers->set('Access-Control-Allow-Origin', '*');
                    $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');
                    return $response;
                } else {
                    $response = new Response($this->editPostForm($_id_posttype, $_id_post));
                    $response->headers->set('Access-Control-Allow-Origin', '*');
                    $response->headers->set('Access-Control-Allow-Headers', 'Origin, X-Requested-With, Content-Type, Accept');

                    return $response;
                }
                break;
            case 'manyadd':
                $_id_attrib = $_id_posttype;
                if ($_id_post == NULL) {
                    $this->addMany($_id_attrib);
                } else {
                    $this->addMany($_id_attrib, $_id_post);
                }
                break;
        }
    }

    public function postProcess(Application $app, Request $request) {
        $this->silexApp = $app;
        $isNew = false;
        $redirect_url = $request->get('redirect_url');       
        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($request->get('idposttype'));
        if (is_null($postType)) {
            $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findOneBy(array('name' => $request->get('idposttype')));
        }        


        if ($request->get('jpid') != null) {
            $isNew = false;
            $post = $this->silexApp['db.orm']->getRepository('Japode\Post\Entity\JPPost')->find($request->get('jpid'));
        } else {
            $isNew = true;
            $post = new JPPost();
            $post->setId(Util::getGUID());
        }

        $post->setDate(new \DateTime());
        $post->setPostType($postType);
        $post->setTitle("...");
        $post->setSummary("...");

        $this->silexApp['db.orm']->merge($post);

        if ($isNew) {
            $postTypeAttrs = $postType->getPostTypeAttrs();
            foreach ($postTypeAttrs as $postTypeAttr) {

                $postAttr = new JPPostAttr();
                $postAttr->setId(Util::getGUID());

                $str_value = $request->get($postTypeAttr->getId());
                switch (intval($postTypeAttr->getPosition())) {
                    case 1:
                        $post->setTitle($str_value);
                        break;
                    case 2:
                        $post->setSummary($str_value);
                        break;
                }

                $postAttr->setDateValue(new \DateTime());
                $postAttr->setPost($post);
                $postAttr->setPostType($postType);
                $postAttr->setPostTypeAttr($postTypeAttr);
                $postAttr->setStrValue($str_value);
                $postAttr->setType('1');
                $this->silexApp['db.orm']->merge($postAttr);
                $post->addPostAttr($postAttr);
            }
        } else {
            $postAttrs = $post->getPostAttrs();
            foreach ($postAttrs as $postAttr) {
                $postTypeAttr = $postAttr->getPostTypeAttr();
                $str_value = $request->get($postTypeAttr->getId());
                switch (intval($postTypeAttr->getPosition())) {
                    case 1:
                        $post->setTitle($str_value);
                        break;
                    case 2:
                        $post->setSummary($str_value);
                        break;
                }

                $postAttr->setDateValue(new \DateTime());
                $postAttr->setPost($post);
                $postAttr->setPostType($postType);
                $postAttr->setPostTypeAttr($postTypeAttr);
                $postAttr->setStrValue($str_value);
                $postAttr->setType('1');
                $this->silexApp['db.orm']->merge($postAttr);
            }
        }

        $this->silexApp['db.orm']->merge($post);

        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();

        $user->setLastPostType($postType->getId());

        $this->silexApp['db.orm']->merge($user);

        $this->silexApp['db.orm']->flush();
        if (is_null($redirect_url)) {
            return $app->redirect('/content/');
        } else {
            return $app->redirect($redirect_url);
        }
    }

    public function selectPost() {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();

        return $this->silexApp['twig']->render('postSelect.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'posttype.id' => 'posttype.id',
                    'posttype.name' => 'posttype.name',
                    'posttype.description' => 'posttype.description',
                    'message' => 'What would you like to post?',
        ));
    }

    public function newPost($_id_posttype) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findOneBy(array('name' => $_id_posttype));

        return $this->silexApp['twig']->render('postEdit.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'id' => '',
                    'idposttype' => $_id_posttype,
                    'attribsRender' => $postType->getPostTypeAttrs(),
                    'message' => 'create your new ' . $postType->getTitle() . ' Post',
        ));
    }

    public function editPost($_id_posttype, $_id_post) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findOneBy(array('name' => $_id_posttype));
        $post = $this->silexApp['db.orm']->getRepository('Japode\Post\Entity\JPPost')->find($_id_post);

        $attribs = array();
        foreach ($post->getPostAttrs() as $postAttribRow) {
            $attribs[$postAttribRow->getId()] = $postAttribRow;
        }

        return $this->silexApp['twig']->render('postEdit.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'id' => $_id_post,
                    'idposttype' => $_id_posttype,
                    'attribsRender' => $postType->getPostTypeAttrs(),
                    'posttype_name' => $postType->getTitle(),
                    'attribs' => $attribs,
                    'message' => 'edit your ' . $postType->getTitle() . ' Post',
        ));
    }

    public function newPostForm($_id_posttype) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        
        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($_id_posttype);
        if (is_null($postType)) {
            $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findOneBy(array('name' => $_id_posttype));
        }
        $portTypeAttrService = new PostTypeAttrService($this->silexApp);
        return $this->silexApp['twig']->render('postEditForm.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'id' => '',
                    'idposttype' => $_id_posttype,
                    'attribs' => NULL,
                    'attribsRender' => $postType->getPostTypeAttrs(),
                    'postTypeAttrService' => $portTypeAttrService,
                    'message' => 'create your new ' . $postType->getTitle() . ' Post',
        ));
    }

    public function editPostForm($_id_posttype, $_id_post) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($_id_posttype);
        if (is_null($postType)) {
            $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->findOneBy(array('name' => $_id_posttype));
        }
        $post = $this->silexApp['db.orm']->getRepository('Japode\Post\Entity\JPPost')->find($_id_post);
        $portTypeAttrService = new PostTypeAttrService($this->silexApp);

        $attribs = array();
        foreach ($post->getPostAttrs() as $postAttribRow) {
            $postTypeAttr = $postAttribRow->getPostTypeAttr();
            $attribs[$postTypeAttr->getId()] = $postAttribRow;
        }

        return $this->silexApp['twig']->render('postEditForm.twig', array(
                    //           'user' => $user,
                    //          'firstname' => $user->getFirstName(),
                    'id' => $_id_post,
                    'idposttype' => $_id_posttype,
                    'attribsRender' => $postType->getPostTypeAttrs(),
                    'posttype_name' => $postType->getTitle(),
                    'attribs' => $attribs,
                    'postTypeAttrService' => $portTypeAttrService,
                    'message' => 'edit your ' . $postType->getTitle() . ' Post',
        ));
    }

    public function addMany($_postAttribute, $_id_post) {
        $postTypeAttr = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostTypeAttr')->find($_postAttribute);
        $portTypeAttrService = new PostTypeAttrService($this->silexApp);
        return $portTypeAttrService->getRender($postTypeAttr);
    }

}
