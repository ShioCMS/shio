<?php

namespace Japode\Database;

use PDO;

class Database {

    public static function getConnection() {
        return new PDO("mysql:host=" . $GLOBALS['database_hostname'] . ";dbname=" . $GLOBALS['database_name'], $GLOBALS['database_username'], $GLOBALS['database_password']);
    }

}
