<?php

namespace Japode\PostType;

use Silex\Application;
use Japode\Util\Util;
use Japode\PostType\Entity\JPPostTypeAttr;

class PostTypeService {

    var $app;

    public function __construct(Application $app) {
        $this->app = $app;
    }
  
    public function addAttribute($postTypeId, $widgetId, $_order = NULL, $_label = NULL) {

        try {
            if ($_order == NULL) {
                $maxRow = $this->app['db.orm']->getRepository('Japode\PostType\Entity\JPPostTypeAttr')->maxOrderByPostTypeId($postTypeId);
                $order = strval(intval($maxRow["max_position"]) + 1);
            } else {
                $order = $_order;
            }

            if ($_label == NULL) {
                switch ($order) {
                    case 1:
                        $label = "Title";
                        break;
                    case 2:
                        $label = "Description";
                        break;
                    default:
                        $label = "Label" . $order;
                        break;
                }
            } else {
                $label = $_label;
            }
            $postType = $this->app['db.orm']->getRepository('Japode\PostType\Entity\JPPostType')->find($postTypeId);
            $widget = $this->app['db.orm']->getRepository('Japode\Widget\Entity\JPWidget')->find($widgetId);
            $postTypeAttr = new JPPostTypeAttr();
            $postTypeAttr->setId(Util::getGUID());
            $postTypeAttr->setPosition($order);
            $postTypeAttr->setLabel($label);
            $postTypeAttr->setWidget($widget);
            $postTypeAttr->setPostType($postType);
            $postTypeAttr->setIsTitle(0);
            $postTypeAttr->setIsSummary(0);

            $this->app['db.orm']->persist($postTypeAttr);
            $this->app['db.orm']->flush();

            return true;
        } catch (Exception $e) {
            return false;
        }
    }

    public function removeAttribute($_ctd_id, $_attribute_id) {
        try {
            $postTypeAttr = $this->app['db.orm']->getRepository('Japode\PostType\Entity\JPPostTypeAttr')->find($_attribute_id);

            $this->app['db.orm']->remove($postTypeAttr);
            $this->app['db.orm']->flush();

            return true;
        } catch (Exception $e) {
            return false;
        }
    }

}
