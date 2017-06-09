<?php
namespace Japode\PostType;

use Silex\Application;
use Symfony\Component\HttpFoundation\Request;
use Silex\ControllerProviderInterface;
use Japode\Base\Base;
use Japode\Post\PostObject;
use Japode\Post\PostAttributeObject;
use Japode\PostType\PostTypeObject;
use Japode\PostType\PostTypeAttributeObject;

class PostType extends Base implements ControllerProviderInterface {
    private $postTypeID;

    public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];
        
        /* Set corresponding endpoints on the controller classes */
        Controllers\PostTypeCtrl::addRoutes($routing);        
        
        return $routing;        
        
    }
    
    function __construct($_postTypeID = NULL) {
        parent::__construct();
        $this->postTypeID = $_postTypeID;
        $sql = "SELECT * from jp_ctd where id = '" .
                $this->postTypeID . "'";
//Comment to test       
// if (!($this->pdo->query($sql)->fetch())) {
       //     $this->postTypeID = $this->getIDByName($this->postTypeID);
       // }
    }

    public function newEmptyInstance() {
        $jpPostObject = new PostObject();
        $jpPostObject->setIDPostType($this->postTypeID);
        $jpCTDObject = $this->getObject();
        $jpCTDAttributes = $jpCTDObject->getAttributes();


        $attributes = array();
        foreach ($jpCTDAttributes as $attribute) {
            $jpPostAttributeObject = new PostAttributeObject();
            $jpPostAttributeObject->setID(NULL);
            $jpPostAttributeObject->setIDAttribute($attribute->getID());
            $jpPostAttributeObject->setIDPostType($this->postTypeID);
            $jpPostAttributeObject->setIntValue(NULL);
            $jpPostAttributeObject->setStrValue(NULL);
            $jpPostAttributeObject->setDateValue(NULL);
            $jpPostAttributeObject->setType('1');
            array_push($attributes, $jpPostAttributeObject);
        }
        $jpPostObject->setAttributes($attributes);
        $jpPostObject->save();
        
        return $jpPostObject;
    }

    public function getIDByName($_name) {
        $sql = "SELECT id from jp_ctd where name = '" .
                $_name . "'";
        if ($row = $this->pdo->query($sql)->fetch()) {
            return $row['id'];
        } else {
            return NULL;
        }
    }

    public function saveDefinition(Request $request) {
        $_name = $request->get('name');
        $_title = $request->get('title');
        $_description = $request->get('description');

        if (($this->postTypeID == NULL) || (strlen($this->postTypeID) <= 0)) {
            $sql = sprintf("INSERT into jp_ctd(id, name, title, description, date) VALUES ('%s', '%s', '%s', '%s', now())",
            $this->getGUID(),
            $_name,
            $_title,
            $_description);
            $rs = $this->pdo->prepare($sql);

            $rs->execute();
        } else {
            $sql = sprintf("UPDATE jp_ctd set name = '%s', description = '%s', title = '%s', date = now() where id = '%s'",
            $_name,
            $_description,
            $_title,
            $this->postTypeID);
                    
            $rs = $this->pdo->prepare($sql);

            $rs->execute();
        }
        if (!$rs) {
            return false;
        } else {
            return true;
        }
    }

    public function saveAttributes(Request $request) {
        $jpCTDObject = $this->listByID($this->postTypeID);
        $jpCTDAttributes = $jpCTDObject->getAttributes();

        foreach ($jpCTDAttributes as $attribute) {
            $manyValue = '0';
            if ($request->get('many_' . $attribute->getID())) {
                $manyValue = '1';
            }

            $requiredValue = '0';
            if ($request->get('req_' . $attribute->getID())) {
                $requiredValue = '1';
            }
            $sql = sprintf("UPDATE `jp_ctd_definition` SET "
                    . "`label`='%s'"
                    . ",`many`='%s'"
                    . ",`required`='%s'", $request->get('lbl_' . $attribute->getID()), $manyValue, $requiredValue);
            $sql .= sprintf(" where id_ctd = '%s' and attribute_id = '%s'", $this->postTypeID, $attribute->getID());
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        }


        return true;
    }

    public function saveAttribute(Request $request) {
        $attribID = $request->get('attribid');
        $attribute = $this->getAttribute($attribID);
        $manyValue = '0';
        if ($request->get('many')) {
            $manyValue = '1';
        }

        $requiredValue = '0';
        if ($request->get('req')) {
            $requiredValue = '1';
        }
        $sql = sprintf("UPDATE `jp_ctd_definition` SET "
                . "`label`='%s'"
                . ",`name`='%s'"
                . ",`description`='%s'"
                . ",`many`='%s'"
                . ",`required`='%s'", $request->get('label'), $request->get('name'), $request->get('description'), $manyValue, $requiredValue);
        $sql .= sprintf(" where id_ctd = '%s' and attribute_id = '%s'", $this->postTypeID, $attribute->getID());

        $rs = $this->pdo->prepare($sql);
        $rs->execute();

        return true;
    }

    public function getAttribute($_postAttribute) {
        $sqlAttribute = "SELECT * from jp_ctd_definition where " .
                " attribute_id = '" . $_postAttribute . "'";
        $attribRow = $this->pdo->query($sqlAttribute)->fetch();

        $jpCTDAttributeObject = new PostTypeAttributeObject();
        $jpCTDAttributeObject->setID($attribRow['attribute_id']);
        $jpCTDAttributeObject->setIDCTD($attribRow['id_ctd']);
        $jpCTDAttributeObject->setIDWidget($attribRow['id_widget']);
        $jpCTDAttributeObject->setOrder($attribRow['order']);
        $jpCTDAttributeObject->setName($attribRow['name']);
        $jpCTDAttributeObject->setLabel($attribRow['label']);
        $jpCTDAttributeObject->setDescription($attribRow['description']);
        $jpCTDAttributeObject->setIsTitle($attribRow['isTitle']);
        $jpCTDAttributeObject->setIsSummary($attribRow['isSummary']);
        $jpCTDAttributeObject->setMany($attribRow['many']);
        $jpCTDAttributeObject->setRequired($attribRow['required']);

        return $jpCTDAttributeObject;
    }

    public function addAttribute($_id, $_widget_id, $_label = NULL, $_order = NULL, $_isTitle = false, $_isSummary = false) {

        $guid = $this->getGUID();
        if ($_order == NULL) {
            $sql = sprintf("SELECT MAX(`order`) as lastattrib FROM `jp_ctd_definition` WHERE "
                    . "`id_ctd` = '%s'", $_id);
            $maxRow = $this->pdo->query($sql)->fetch();
            $order = strval(intval($maxRow['lastattrib']) + 1);
        } else {
            $order = $_order;
        }

        if ($_label == NULL) {
            switch ($order) {
                case 1:
                    $label = "Title";
                    break;
                case 2:
                    $label = "Description";
                    break;

                default:
                    $label = "Label" . $order;
                    break;
            }
        } else {
            $label = $_label;
        }
        $sql = sprintf("INSERT INTO  `jp_ctd_definition` (  `id_ctd` ,  `attribute_id` ,  `order` ,  `label` ,  `id_widget` ,  `isTitle` ,  `isSummary` )"
                . "VALUES ('%s', '%s', '%s', '%s', '%s', '%d', '%d')", $_id, $guid, $order, $label, $_widget_id, (int) $_isTitle, (int) $_isSummary);
        $rs = $this->pdo->prepare($sql);
        $rs->execute();

        if (!$rs) {
            return false;
        } else {
            return true;
        }
    }

    public function removeAttribute($_ctd_id, $_attribute_id) {
        $sql = sprintf("DELETE FROM  `jp_ctd_definition`"
                . "WHERE `id_ctd` = '%s' AND  `attribute_id` = '%s'", $_ctd_id, $_attribute_id);
        $rs = $this->pdo->prepare($sql);
        $rs->execute();

        if (!$rs) {
            return false;
        } else {
            return true;
        }
    }

    public function jp_list() {


        $sql = "SELECT * from jp_ctd order by name";
        $ctdList = array();
        foreach ($this->pdo->query($sql) as $row) {
            $jpCTDObject = new PostTypeObject();
            $jpCTDObject->setID($row['id']);
            $jpCTDObject->setTitle($row['title']);
            $jpCTDObject->setName($row['name']);
            $jpCTDObject->setDescription($row['description']);
            $jpCTDObject->setDate($row['date']);

            $sqlAttributes = "SELECT * from jp_ctd_definition where id_ctd = '" .
                    $jpCTDObject->getID() . "'";
            $ctdAttributes = array();
            foreach ($this->pdo->query($sqlAttributes) as $attribRow) {
                $jpCTDAttributeObject = new PostTypeAttributeObject();
                $jpCTDAttributeObject->setID($attribRow['attribute_id']);
                $jpCTDAttributeObject->setIDCTD($attribRow['id_ctd']);
                $jpCTDAttributeObject->setIDWidget($attribRow['id_widget']);
                $jpCTDAttributeObject->setOrder($attribRow['order']);
                $jpCTDAttributeObject->setName($attribRow['name']);
                $jpCTDAttributeObject->setLabel($attribRow['label']);
                $jpCTDAttributeObject->setDescription($attribRow['description']);
                $jpCTDAttributeObject->setIsTitle($attribRow['isTitle']);
                $jpCTDAttributeObject->setIsSummary($attribRow['isSummary']);
                $jpCTDAttributeObject->setMany($attribRow['many']);
                $jpCTDAttributeObject->setRequired($attribRow['required']);

                array_push($ctdAttributes, $jpCTDAttributeObject);
            }
            $jpCTDObject->setAttributes($ctdAttributes);
            array_push($ctdList, $jpCTDObject);
        }

        return $ctdList;
    }

    public function listByID($_id) {

        //Deprecated usar getPostTypeByID()
        $this->postTypeID = $_id;
        return $this->getObject();
    }

    public function getObject() {
        $sqlAttributes = "SELECT * from jp_ctd_definition where id_ctd = '" .
                $this->postTypeID . "' order by `order`";
        $ctdAttributes = array();
        foreach ($this->pdo->query($sqlAttributes) as $attribRow) {
            $jpCTDAttributeObject = new PostTypeAttributeObject();
            $jpCTDAttributeObject->setID($attribRow['attribute_id']);
            $jpCTDAttributeObject->setIDCTD($attribRow['id_ctd']);
            $jpCTDAttributeObject->setIDWidget($attribRow['id_widget']);
            $jpCTDAttributeObject->setOrder($attribRow['order']);
            $jpCTDAttributeObject->setName($attribRow['name']);
            $jpCTDAttributeObject->setLabel($attribRow['label']);
            $jpCTDAttributeObject->setDescription($attribRow['description']);
            $jpCTDAttributeObject->setIsTitle($attribRow['isTitle']);
            $jpCTDAttributeObject->setIsSummary($attribRow['isSummary']);
            $jpCTDAttributeObject->setMany($attribRow['many']);
            $jpCTDAttributeObject->setRequired($attribRow['required']);

            array_push($ctdAttributes, $jpCTDAttributeObject);
        }

        $sql = "SELECT * from jp_ctd where id = '" . $this->postTypeID . "'";
        $jpCTDObject = new PostTypeObject();
        foreach ($this->pdo->query($sql) as $row) {
            $jpCTDObject->setID($row['id']);
            $jpCTDObject->setName($row['name']);
            $jpCTDObject->setTitle($row['title']);
            $jpCTDObject->setDescription($row['description']);
            $jpCTDObject->setDate($row['date']);
            $jpCTDObject->setAttributes($ctdAttributes);
        }

        return $jpCTDObject;
    }

}
