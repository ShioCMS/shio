<?php
namespace Japode\Post;

use Japode\Base\BaseObject;

class PostAttributeObject extends BaseObject {

    private $id;
    private $id_posttype;
    private $id_attribute;
    private $int_value;
    private $str_value;
    private $date_value;
    private $type;

    function setID($_id) {
        $this->id = $_id;
    }

    function setIDPostType($_id_posttype) {
        $this->id_posttype = $_id_posttype;
    }

    function setIDAttribute($_id_attribute) {
        $this->id_attribute = $_id_attribute;
    }

    function setIntValue($_int_value) {
        $this->int_value = $_int_value;
    }

    function setStrValue($_str_value) {
        $this->str_value = $_str_value;
    }

    function setDateValue($_date_value) {
        $this->date_value = $_date_value;
    }

    function setType($_type) {
        $this->type = $_type;
    }

    function getID() {
        return $this->id;
    }

    function getIDPostType() {
        return $this->id_posttype;
    }

    function getIDAttribute() {
        return $this->id_attribute;
    }

    function getIntValue() {
        return $this->int_value;
    }

    function getStrValue() {
        return $this->str_value;
    }

    function getDateValue() {
        return $this->date_value;
    }

    function getType() {
        return $this->type;
    }
    
}
