<?php

namespace Japode\PostType;

use Silex\Application;
use Japode\PostType\Entity\JPPostTypeAttr;
use Japode\Post\Entity\JPPostAttr;

class PostTypeAttrService {

    var $app;

    public function __construct(Application $app) {
        $this->app = $app;
    }

    public function doEdit(JPPostTypeAttr $postTypeAttr) {
        $widget = $postTypeAttr->getWidget();
        
        $className = $widget->getClassName();
        require_once ABSPATH . $widget->getImplementationCode();
        $jpWidgetProcess = new $className();
        $jpWidgetProcess->setAttributeName($postTypeAttr->getId());
        return $jpWidgetProcess->doEdit();
    }
    
     public function getRender(JPPostTypeAttr $postTypeAttr, JPPostAttr $postAttr = NULL) {
        $widget = $postTypeAttr->getWidget();         
        $className = $widget->getClassName();
        require_once ABSPATH . $widget->getImplementationCode();
        $jpWidgetRender = new $className();
        $jpWidgetRender->setAttributeName($postTypeAttr->getId());
        return $jpWidgetRender->doRender($postTypeAttr, $postAttr);
    }

}
