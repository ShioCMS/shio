<?php

namespace Japode\Profile;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Profile implements ControllerProviderInterface {
     public function connect(Application $app) {
    // creates a new controller based on the default route
        $routing = $app['controllers_factory'];
        
        /* Set corresponding endpoints on the controller classes */
        Controllers\ProfileCtrl::addRoutes($routing);
        
        return $routing;
    }
}
