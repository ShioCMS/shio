<?php

namespace Japode\Widget\Controllers;

use Silex\Application;
use Japode\PostType\PostTypeService;


class WidgetCtrl {

    // Connects the routes in Silex
    static public function addRoutes($routing) {

        $routing->get('/{action}/{param1}/{param2}/', array(new self(), 'widget'))->bind('widget');
        $routing->post('/{action}/{param1}/{param2}/', array(new self(), 'widgetProcess'))->bind('widgetProcess');
    }

    var $silexApp;

    public function widget(Application $app, $action, $param1, $param2) {
        $this->silexApp = $app;
        switch ($action) {
            case 'add':
                return $this->addAttribute($param1, $param2);
                break;
            case 'remove':
                return $this->removeAttribute($param1, $param2);
                break;
            case 'process':
                return $this->processAttribute($param1, $param2);
                break;
        }
    }

    public function widgetProcess(Application $app, $action, $param1, $param2, Symfony\Component\HttpFoundation\Request $request) {
        $this->silexApp = $app;
        switch ($action) {
            case 'process':
                return $this->processAttribute($param1, $param2);
                break;
        }
    }

    public function addAttribute($_widget_id, $_ctd_id) {
        
       $postTypeService = new PostTypeService($this->silexApp);
        if ($postTypeService->addAttribute($_ctd_id, $_widget_id)) {
            return $this->silexApp->redirect('/post/type/attributes/' . $_ctd_id . '/');
        } else {
            return new Response("Insert Error", 500);
        }
    }

    public function removeAttribute($_ctd_id, $_attribute_id) {
        $postTypeService = new PostTypeService($this->silexApp);
        if ($postTypeService->removeAttribute($_ctd_id, $_attribute_id)) {
            return $this->silexApp->redirect('/post/type/attributes/' . $_ctd_id . '/');
        } else {
            return new Response("Delete Error", 500);
        }
    }

    public function processAttribute($_postTypeID, $_attributeID) {
   ///     $jpCTD = new PostType($_postTypeID);
   //     $jpCTDAttribute = $jpCTD->getAttribute($_attributeID);
   //     return $jpCTDAttribute->processAction();
    }

}
