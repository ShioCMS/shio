<?php

namespace Japode\Region;

use Japode\Base\BaseObject;

class RegionObject extends BaseObject {

    private $id;
    private $name;
    private $id_post_type;
    private $id_post;

    function setID($_id) {
        $this->id = $_id;
    }

    function setName($_name) {
        $this->name = $_name;
    }

    function setIDPostType($_id_post_type) {
        $this->id_post_type = $_id_post_type;
    }

    function setIDPost($_id_post) {
        $this->id_post = $_id_post;
    }

    function getID() {
        return $this->id;
    }

    function getName() {
        return $this->name;
    }

    function getIDPostType() {
        return $this->id_post_type;
    }

    function getIDPost() {
        return $this->id_post;
    }

    public function save() {
        if ($this->getID() == NULL) {
            $this->setID($this->getGUID());
            $sql = sprintf("INSERT into jp_region(id, id_post, id_post_type, name) VALUES ('%s', '%s', '%s', '%s')", addslashes($this->getID()), addslashes($this->getIDPost()), addslashes($this->getIDPostType()), addslashes($this->getName()));
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        } else {
            $sql = sprintf("UPDATE jp_region set  id_post = '%s', name = '%s'", addslashes($this->getIDPost()), addslashes($this->getName()));
            $sql .= sprintf("where id = '%s' and id_post_type = '%s'", addslashes($this->getID()), addslashes($this->getIDPostType()));
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        }
    }
}
