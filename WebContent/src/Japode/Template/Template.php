<?php

namespace Japode\Template;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Template implements ControllerProviderInterface {
     public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\TemplateCtrl::addRoutes($routing);

        return $routing;
    }

}
