<?php

namespace Japode\PostType\Controllers;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Symfony\Component\HttpFoundation\Response;
use Japode\PostType\Entity\JPPostType;
use Japode\PostType\Entity\JPPostTypeAttr;
use Japode\PostType\PostTypeAttrService;
use Japode\Util\Util;

class PostTypeCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {
        $routing->get('/{action}/', array(new self(), 'postType'))->bind('postType');
        $routing->get('/{action}/{param1}/', array(new self(), 'postType'))->bind('postType1');
        $routing->get('/{action}/{param1}/{param2}/', array(new self(), 'postType'))->bind('postType2');
        $routing->post('/{action}/{param1}/', array(new self(), 'postTypeProcess'))->bind('postTypeProcess');
    }

    var $silexApp;

    public function postType(Application $app, $action, $param1 = NULL, $param2 = NULL) {
        $this->silexApp = $app;
        $_id = $param1;
        $_attrib_id = $param2;
        switch ($action) {
            case 'editor':
                if ($_id == NULL) {
                    return $this->newPostType();
                } else {
                    return $this->editPostType($_id);
                }
                break;

                break;
            case 'attributes':
                return $this->postTypeAttributes($_id);
                break;
            case 'attribute':
                return $this->editAttribute($_id, $_attrib_id);
                break;
            case 'process':
                if ($_id == 'basic') {
                    return $this->processPostType($_POST['jpid'], $_POST['name'], $_POST['description']);
                } else if ($_id == 'attributes') {
                    return $this->processPostTypeAttributes();
                } else if ($_id == 'attribute') {
                    return $this->processPostTypeAttribute();
                }
                break;
        }
    }

    public function postTypeProcess(Application $app, $action, $param1, Request $request) {
        $this->silexApp = $app;
        switch ($action) {
            case 'process':
                if ($param1 == 'basic') {
                    return $this->processPostType($request);
                } else if ($param1 == 'attributes') {
                    return $this->processPostTypeAttributes($request);
                } else if ($param1 == 'attribute') {
                    return $this->processPostTypeAttribute($request);
                }
                break;
        }
    }

    public function editAttribute($postTypeID, $attributeID) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();
        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($postTypeID);
        $postTypeAttr = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostTypeAttr')->find($attributeID);
        $postTypeAttrService = new PostTypeAttrService($this->silexApp);
        return $this->silexApp['twig']->render('attributeEdit.twig', array(
                    'user' => $user,
                    'attrib' => $postTypeAttr,
                    'postType' => $postType,
                    'attribs' => $postType->getPostTypeAttrs(),
                    'firstname' => $user->getFirstName(),
                    'id' => $postTypeID,
                    'jpType' => NULL,
                    'message' => 'edit your ' . $postType->getTitle() . '\'s  Attribute',
                    'widgetProperties' => $postTypeAttrService->doEdit($postTypeAttr),
        ));
    }

    public function newPostType() {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();

        $postType = new JPPostType();
        return $this->silexApp['twig']->render('postTypeModel.twig', array(
                    'postType' => $postType,
                    'id' => '',
                    'name' => $postType->getName(),
                    'description' => $postType->getDescription(),
                    'attribs' => $postType->getPostTypeAttrs(),
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'message' => 'model your new Post Type',
                    'jpType' => "basic",
        ));
    }

    public function postTypeAttributes($id) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();

        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($id);

        $postTypeAttr = new JPPostTypeAttr();
        return $this->silexApp['twig']->render('postTypeAttributes.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'jpType' => 'attribute',
                    'id' => $id,
                    'widget.name' => 'widget.name',
                    'postType' => $postType,
                    'attrib' => $postTypeAttr,
                    'attribs' => $postType->getPostTypeAttrs(),
                    'message' => 'model your ' . $postType->getTitle() . ' Post Type',
        ));
    }

    public function editPostType($id) {
        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();

        $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($id);

        return $this->silexApp['twig']->render('postTypeModel.twig', array(
                    'user' => $user,
                    'firstname' => $user->getFirstName(),
                    'jpType' => "basic",
                    'id' => $postType->getId(),
                    'name' => $postType->getName(),
                    'description' => $postType->getDescription(),
                    'message' => 'model your ' . $postType->getName() . ' Post Type',
        ));
    }

    public function processPostType(Request $request) {
        try {
             if ($request->get('posttypeid') != null) {
                $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($request->get('posttypeid'));
            } else {
                $postType = new JPPostType();
                $postType->setId(Util::getGUID());
            }
            
            $postType->setName($request->get('name'));
            $postType->setTitle($request->get('title'));
            $postType->setDescription($request->get('description'));

            $this->silexApp['db.orm']->merge($postType);
            $this->silexApp['db.orm']->flush();

            return $this->silexApp->redirect('/post/select/');
        } catch (Exception $e) {
            return new Response("Save Error", 500);
        }
    }

    public function processPostTypeAttributes(Request $request) {
        try {
            if ($request->get('posttypeid') != null) {
                $postType = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($request->get('posttypeid'));
            } else {
                $postType = new JPPostType();
                $postType->setId(Util::getGUID());
            }
            $postType->setName($request->get('name'));
            $postType->setTitle($request->get('title'));
            $postType->setDescription($request->get('description'));

            $this->silexApp['db.orm']->merge($postType);
            $this->silexApp['db.orm']->flush();

            $postTypeAttrs = $postType->getPostTypeAttrs();

            foreach ($postTypeAttrs as $attribute) {
                $manyValue = '0';
                if ($request->get('many_' . $attribute->getId())) {
                    $manyValue = '1';
                }

                $requiredValue = '0';
                if ($request->get('req_' . $attribute->getId())) {
                    $requiredValue = '1';
                }
                $attribute->setLabel($request->get('lbl_' . $attribute->getID()));
                $attribute->setMany($manyValue);
                $attribute->setRequired($requiredValue);

                $this->silexApp['db.orm']->merge($attribute);
                $this->silexApp['db.orm']->flush();
            }

            return $this->silexApp->redirect('/post/type/attributes/' . $postType->getId() . '/');
        } catch (Exception $e) {
            return new Response("Save Error", 500);
        }
    }

    public function processPostTypeAttribute(Request $request) {
        try {
            $postTypeID = $request->get('posttypeid');
            $attribID = $request->get('attribid');

            $postTypeAttr = $this->silexApp['db.orm']->getRepository('Japode\PostType\Entity\JPPostTypeAttr')->find($attribID);

            $manyValue = '0';
            if ($request->get('many')) {
                $manyValue = '1';
            }

            $requiredValue = '0';
            if ($request->get('req')) {
                $requiredValue = '1';
            }

            $postTypeAttr->setLabel($request->get('label'));
            $postTypeAttr->setName($request->get('name'));
            $postTypeAttr->setDescription($request->get('description'));
            $postTypeAttr->setMany($manyValue);
            $postTypeAttr->setRequired($requiredValue);

            $this->silexApp['db.orm']->persist($postTypeAttr);
            $this->silexApp['db.orm']->flush();

            return $this->silexApp->redirect('/post/type/attribute/' . $postTypeID . '/' . $attribID . '/');
        } catch (Exception $e) {
            return new Response("Save Error", 500);
        }
    }

}
