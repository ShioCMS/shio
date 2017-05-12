<?php
namespace Japode\Chat;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Chat implements ControllerProviderInterface {
    public function connect(Application $app) {
    // creates a new controller based on the default route
        $routing = $app['controllers_factory'];
        
        /* Set corresponding endpoints on the controller classes */
        Controllers\ChatCtrl::addRoutes($routing);
        
        return $routing;
    }

}
