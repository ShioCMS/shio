<?php

namespace Japode\Login;

use Silex\Application;
use Silex\ControllerProviderInterface;

class SignUp implements ControllerProviderInterface {

     public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\SignUpCtrl::addRoutes($routing);

        return $routing;
    }
}
