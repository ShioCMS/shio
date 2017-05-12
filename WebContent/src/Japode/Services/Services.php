<?php

namespace Japode\Services;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Services implements ControllerProviderInterface {

    public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\PostCtrl::addRoutes($routing);
        Controllers\PostTypesCtrl::addRoutes($routing);
        Controllers\PostsCtrl::addRoutes($routing);
        Controllers\RegionCtrl::addRoutes($routing);
        Controllers\SitesCtrl::addRoutes($routing);
        Controllers\WidgetsCtrl::addRoutes($routing);
        Controllers\FURLCtrl::addRoutes($routing);

        return $routing;
    }

}
