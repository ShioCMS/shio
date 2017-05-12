<?php
namespace Japode\PostType;

class PostTypeObject {

    public $id;
    public $name;
    public $title;
    public $text;
    public $description;
    public $date;
    public $attributes;

    public function __construct() {
        
    }

    public function setID($_id) {
        $this->id = $_id;
    }

    public function setName($_name) {
        $this->name = $_name;
    }

    public function setTitle($_title) {
        $this->title = $_title;
    }

    public function setDescription($_description) {
        $this->description = $_description;
    }

    public function setDate($_date) {
        $this->date = $_date;
    }

    public function setAttributes($_attributes) {
        $this->attributes = $_attributes;
    }

    public function getID() {
        return $this->id;
    }

    public function getName() {
        return $this->name;
    }

    public function getTitle() {
        return $this->title;
    }

    public function getDescription() {
        return $this->description;
    }

    public function getDate() {
        return $this->date;
    }

    public function getAttributes() {
        return $this->attributes;
    }


}
