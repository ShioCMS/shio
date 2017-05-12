<?php

namespace Japode\Login;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Silex\Provider\SessionServiceProvider;

class LogIn implements ControllerProviderInterface {
  /**
     * function to set up the container for the Client app
     */
    public function setup(Application $app)
    {
        // create session object and start it
        $app->register(new SessionServiceProvider());

        if (!$app['session']->isStarted()) {
            $app['session']->start();
        }

   
    }

    public function connect(Application $app) {
        
        // set up the service container
   //     $this->setup($app);
        
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\LogInCtrl::addRoutes($routing);
        Controllers\RecoverCtrl::addRoutes($routing);
        
        return $routing;
    }

}
