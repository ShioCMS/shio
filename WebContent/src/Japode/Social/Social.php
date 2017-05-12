<?php

namespace Japode\Social;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Social implements ControllerProviderInterface {

     public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\SocialCtrl::addRoutes($routing);

        return $routing;
    }
}
