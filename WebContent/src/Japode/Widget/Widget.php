<?php

namespace Japode\Widget;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Japode\Base\Base;

class Widget extends Base implements ControllerProviderInterface {

    public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\WidgetCtrl::addRoutes($routing);

        return $routing;
    }

}
