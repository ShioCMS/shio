<?php

namespace Japode\Post;

use Japode\Base\BaseObject;
use Japode\User\User;

class PostObject extends BaseObject {

    private $id;
    private $id_posttype;
    private $title;
    private $summary;
    private $date;
    private $attributes;

    function setID($_id) {
        $this->id = $_id;
    }

    function setIDPostType($_id_posttype) {
        $this->id_posttype = $_id_posttype;
    }

    function setTitle($_title) {
        $this->title = $_title;
    }

    function setSummary($_summary) {
        $this->summary = $_summary;
    }

    function setDate($_date) {
        $this->date = $_date;
    }

    function setAttributes($_attributes) {
        $this->attributes = $_attributes;
    }

    function getID() {
        return $this->id;
    }

    function getIDPostType() {
        return $this->id_posttype;
    }

    function getTitle() {
        return $this->title;
    }

    function getSummary() {
        return $this->summary;
    }

    function getDate() {
        return $this->date;
    }

    function getAttributes() {
        return $this->attributes;
    }

    public function save() {
        $jpPostAttribute = $this->getAttributes();

        if ($this->getID() == NULL) {
            $this->setID($this->getGUID());
            foreach ($jpPostAttribute as $attribute) {
                $sql = sprintf("INSERT into jp_post_attribs(id, id_posttype, id_attribute, type, str_value) VALUES ('%s', '%s', '%s', '%s', '%s')", addslashes($this->getID()), addslashes($this->getIDPostType()), addslashes($attribute->getIDAttribute()), addslashes($attribute->getType()), addslashes($attribute->getStrValue()));
                $rs = $this->pdo->prepare($sql);
                $rs->execute();
            }
            $sql = sprintf("INSERT into jp_post(id, id_posttype, title, summary, date) VALUES ('%s', '%s', '%s', '%s',now())", addslashes($this->getID()), addslashes($this->getIDPostType()), addslashes($this->getTitle()), addslashes($this->getSummary()));
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        } else {
            foreach ($jpPostAttribute as $attribute) {
                $sql = sprintf("UPDATE jp_post_attribs set type = '%s', str_value = '%s'", addslashes($attribute->getType()), addslashes($attribute->getStrValue()));
                $sql .= sprintf(" where id = '%s' and id_posttype = '%s' and id_attribute = '%s'", addslashes($this->getID()), addslashes($this->getIDPostType()), addslashes($attribute->getIDAttribute()));
                $rs = $this->pdo->prepare($sql);
                $rs->execute();
            }
            $sql = sprintf("UPDATE jp_post set  title = '%s', summary = '%s', date = now()", addslashes($this->getTitle()), addslashes($this->getSummary()));
            $sql .= sprintf("where id = '%s' and id_posttype = '%s'", addslashes($this->getID()), addslashes($this->getIDPostType()));
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        }

        $jpUser = new User();
        $jpUserObject = $jpUser->getUser();

        $sql = sprintf("UPDATE `jp_users` set `last_posttype`='%s'", $this->getIDPostType());
        $sql .= sprintf(" WHERE `email`='%s'", addslashes($jpUserObject->getEmail()));
        $rs = $this->pdo->prepare($sql);
        $rs->execute();
    }

}
