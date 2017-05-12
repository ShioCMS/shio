<?php

namespace Japode\Region;

use Japode\Base\Base;

class Region extends Base {

    private $regionID;

    function __construct($_regionID = NULL) {
        parent::__construct();
        $this->regionID = $_regionID;
        $sql = "SELECT * from jp_region where id = '" .
                $this->regionID . "'";
        if (!($this->pdo->query($sql)->fetch())) {
            $this->regionID = $this->getIDByName($this->regionID);
        }
    }

    public function getIDByName($_name) {
        $sql = "SELECT id from jp_region where name = '" .
                $_name . "'";
        if ($row = $this->pdo->query($sql)->fetch()) {
            return $row['id'];
        } else {
            return NULL;
        }
    }

    public function getObject() {
        $sql = "SELECT * from jp_region where id = '" . $this->regionID . "'";

        if ($row = $this->pdo->query($sql)->fetch()) {
            $jpRegionObject = new RegionObject();
            $jpRegionObject->setID($row['id']);
            $jpRegionObject->setIDPost($row['id_post']);
            $jpRegionObject->setIDPostType($row['id_post_type']);
            $jpRegionObject->setName($row['name']);
            return $jpRegionObject;
        } else {
            return NULL;
        }
    }
}
