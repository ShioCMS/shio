<?php
namespace Japode\PostType;

use Japode\Widget\Widget;
use Japode\Post\PostAttributeObject;

class PostTypeAttributeObject {

    private $id;
    private $id_ctd;
    private $id_widget;
    private $name;
    private $label;
    private $description;
    private $isTitle;
    private $isSummary;
    private $order;
    private $many;
    private $required;

    public function setID($_id) {
        $this->id = $_id;
    }

    public function setIDCTD($_id_ctd) {
        $this->id_ctd = $_id_ctd;
    }

    public function setIDWidget($_id_widget) {
        $this->id_widget = $_id_widget;
    }

    public function setName($_name) {
        $this->name = $_name;
    }

    public function setLabel($_label) {
        $this->label = $_label;
    }

    public function setDescription($_description) {
        $this->description = $_description;
    }

    public function setIsTitle($_isTitle) {
        $this->isTitle = $_isTitle;
    }

    public function setIsSummary($_isSummary) {
        $this->isSummary = $_isSummary;
    }

    public function setOrder($_order) {
        $this->order = $_order;
    }

    public function setMany($_many) {
        $this->many = $_many;
    }

    public function setRequired($_required) {
        $this->required = $_required;
    }

    public function getID() {
        return $this->id;
    }

    public function getIDCTD() {
        return $this->id_ctd;
    }

    public function getIDWidget() {
        return $this->id_widget;
    }

    public function getName() {
        return $this->name;
    }

    public function getLabel() {
        return $this->label;
    }

    public function getDescription() {
        return $this->description;
    }

    public function getOrder() {
        return $this->order;
    }

    public function getMany() {
        return $this->many;
    }

    public function getRequired() {
        return $this->required;
    }

    public function getRender(PostAttributeObject $_postAttributeObject = NULL) {
        $jpWidget = new Widget();
        $jpWidgetObject = $jpWidget->listByID($this->getIDWidget());
        $className = $jpWidgetObject->getClassName();
        require_once ABSPATH . $jpWidgetObject->getImplementationCode();
        $jpWidgetRender = new $className();
        $jpWidgetRender->setAttributeName($this->getID());
        return $jpWidgetRender->doRender($this, $_postAttributeObject);
    }

    public function isTitle() {
        return $this->isTitle;
    }

    public function isSummary() {
        return $this->isSummary;
    }

    public function processAction() {
        $jpWidget = new Widget();
        $jpWidgetObject = $jpWidget->listByID($this->getIDWidget());
        $className = $jpWidgetObject->getClassName();
        require_once ABSPATH . $jpWidgetObject->getImplementationCode();
        $jpWidgetProcess = new $className();
        $jpWidgetProcess->setAttributeName($this->getID());
        return $jpWidgetProcess->processAction();
    }

    public function doEdit() {
        $jpWidget = new Widget();
        $jpWidgetObject = $jpWidget->listByID($this->getIDWidget());
        $className = $jpWidgetObject->getClassName();
        require_once ABSPATH . $jpWidgetObject->getImplementationCode();
        $jpWidgetProcess = new $className();
        $jpWidgetProcess->setAttributeName($this->getID());
        return $jpWidgetProcess->doEdit();
    }
}
