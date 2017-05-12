<?php
namespace Japode\Base;

use Japode\Database\Database;

class Base {
    var $pdo;

    public function __construct() {

        $this->pdo = Database::getConnection();
        if (!$this->pdo) {
            die('Erro ao criar a conexão');
        }
    }

    public function getGUID() {
        if (function_exists('com_create_guid')) {
            $guid = com_create_guid();
        } else {
            mt_srand((double) microtime() * 10000); //optional for php 4.2.0 and up.
            $charid = strtoupper(md5(uniqid(rand(), true)));
            $hyphen = chr(45); // "-"
            $uuid = chr(123)// "{"
                    . substr($charid, 0, 8) . $hyphen
                    . substr($charid, 8, 4) . $hyphen
                    . substr($charid, 12, 4) . $hyphen
                    . substr($charid, 16, 4) . $hyphen
                    . substr($charid, 20, 12)
                    . chr(125); // "}"
            $guid = $uuid;
        }

        $guid = str_replace("{", "", $guid);
        $guid = str_replace("}", "", $guid);
        $guid = str_replace("-", "", $guid);
        return $guid;
    }

}
