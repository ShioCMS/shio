<?php

namespace Japode\Site;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Site implements ControllerProviderInterface {

    public function connect(Application $app) {

        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\SiteCtrl::addRoutes($routing);

        return $routing;
    }
}
