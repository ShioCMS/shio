<?php

namespace Japode\User;

use Silex\Application;
use Japode\User\Entity\JPUser;

class UserService {
    public static function setLastLogin(Application $app, JPUser $user) {
        
        $userCurrent = $app['db.orm']->getRepository('Japode\User\Entity\JPUser')->find($user->getId());
        
        if (is_null($userCurrent)) {
            // Usuario não encontrado no Banco de Dados, portanto cria um novo.
            $userCurrent = $user;
        }
        $userCurrent->setLastLogin(new \DateTime());
        $userCurrent->setLoginTimes($userCurrent->getLoginTimes()+1);
        $app['db.orm']->merge($userCurrent);
        $app['db.orm']->flush();
    }
    
}
