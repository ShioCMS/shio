<?php
namespace Japode\Content;

use Silex\Application;
use Silex\ControllerProviderInterface;

class Content implements ControllerProviderInterface {
    public function connect(Application $app) {
    // creates a new controller based on the default route
        $routing = $app['controllers_factory'];
        
        /* Set corresponding endpoints on the controller classes */
        Controllers\Content::addRoutes($routing);
        
        return $routing;
    }

}
