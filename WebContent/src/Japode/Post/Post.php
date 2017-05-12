<?php

namespace Japode\Post;

use Silex\Application;
use Silex\ControllerProviderInterface;
use Symfony\Component\HttpFoundation\Request;
use Japode\Base\Base;
use Japode\Post\PostObject;
use Japode\PostType\PostType;

class Post extends Base implements ControllerProviderInterface {

    public function connect(Application $app) {
        // creates a new controller based on the default route
        $routing = $app['controllers_factory'];

        /* Set corresponding endpoints on the controller classes */
        Controllers\PostCtrl::addRoutes($routing);

        return $routing;
    }

    public function save(Request $request) {
        $_id = $request->get('jpid');
        $_id_posttype = $request->get('idposttype');
        $jpCTD = new PostType($_id_posttype);
        $jpCTDObject = $jpCTD->getObject();
        $postTypeID = $jpCTDObject->getID();
        $jpCTDAttributes = $jpCTDObject->getAttributes();
        $attrib_title = NULL;
        $attrib_summary = NULL;

        if (($_id == NULL) || (strlen($_id) <= 0)) {
            $id_post = $this->getGUID();
            foreach ($jpCTDAttributes as $attribute) {
                $type = '1';
                $id_attribute = $attribute->getID();
                $str_value = $request->get($attribute->getID());
                if (intval($attribute->getOrder()) == 1) {
                    $attrib_title = $str_value;
                }
                if (intval($attribute->getOrder()) == 2) {
                    $attrib_summary = $str_value;
                }
                $sql = sprintf("INSERT into jp_post_attribs(id, id_posttype, id_attribute, type, str_value) VALUES ('%s', '%s', '%s', '%s', '%s')", addslashes($id_post), addslashes($postTypeID), addslashes($id_attribute), addslashes($type), addslashes($str_value));
                $rs = $this->pdo->prepare($sql);
                $rs->execute();
            }
            $sql = sprintf("INSERT into jp_post(id, id_posttype, title, summary, date) VALUES ('%s', '%s', '%s', '%s',now())", addslashes($id_post), addslashes($postTypeID), addslashes($attrib_title), addslashes($attrib_summary));
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        } else {
            $id_post = $_id;
            foreach ($jpCTDAttributes as $attribute) {
                $type = '1';
                $id_attribute = $attribute->getID();
                $str_value = $request->get($attribute->getID());
                if (intval($attribute->getOrder()) == 1) {
                    $attrib_title = $str_value;
                }
                if (intval($attribute->getOrder()) == 2) {
                    $attrib_summary = $str_value;
                }
                $sql = sprintf("UPDATE jp_post_attribs set type = '%s', str_value = '%s'", addslashes($type), addslashes($str_value));
                $sql .= sprintf(" where id = '%s' and id_posttype = '%s' and id_attribute = '%s'", addslashes($id_post), addslashes($postTypeID), addslashes($id_attribute));
                $rs = $this->pdo->prepare($sql);
                $rs->execute();
            }
            $sql = sprintf("UPDATE jp_post set  title = '%s', summary = '%s', date = now()", addslashes($attrib_title), addslashes($attrib_summary));
            $sql .= sprintf("where id = '%s' and id_posttype = '%s'", addslashes($id_post), addslashes($postTypeID));
            $rs = $this->pdo->prepare($sql);
            $rs->execute();
        }

        $user = $this->silexApp['db.orm']->getRepository('Japode\User\Entity\JPUser')->getCurrentUser();

        $sql = sprintf("UPDATE `jp_users` set `last_posttype`='%s'", $postTypeID);
        $sql .= sprintf(" WHERE `email`='%s'", addslashes($jpUserObject->getEmail()));
        $rs = $this->pdo->prepare($sql);
        $rs->execute();

        return true;
    }

    public function jp_list() {
        $sql = "SELECT * from jp_post order by date desc";
        $postList = array();
        foreach ($this->pdo->query($sql) as $row) {
            $jpPostbject = new PostObject();
            $jpPostbject->setID($row['id']);
            $jpPostbject->setIDPostType($row['id_posttype']);
            $jpPostbject->setTitle($row['title']);
            $jpPostbject->setSummary($row['summary']);
            $jpPostbject->setDate($row['date']);
            array_push($postList, $jpPostbject);
        }

        return $postList;
    }

    public function getPostByID($_id) {
        $sql = "SELECT * from jp_post where id = '" . $_id . "'";
        if ($row = $this->pdo->query($sql)->fetch()) {
            $jpPostObject = new PostObject();
            $jpPostObject->setID($row['id']);
            $jpPostObject->setIDPostType($row['id_posttype']);
            $jpPostObject->setTitle($row['title']);
            $jpPostObject->setSummary($row['summary']);
            $jpPostObject->setDate($row['date']);

            $sqlAttributes = "SELECT * from jp_post_attribs where id = '" . $_id . "'";
            $attributes = array();
            foreach ($this->pdo->query($sqlAttributes) as $rowAttributes) {
                $jpPostAttributeObject = new PostAttributeObject();
                $jpPostAttributeObject->setID($rowAttributes['id']);
                $jpPostAttributeObject->setIDAttribute($rowAttributes['id_attribute']);
                $jpPostAttributeObject->setIDPostType($rowAttributes['id_posttype']);
                $jpPostAttributeObject->setIntValue($rowAttributes['int_value']);
                $jpPostAttributeObject->setStrValue($rowAttributes['str_value']);
                $jpPostAttributeObject->setDateValue($rowAttributes['date_value']);
                $jpPostAttributeObject->setType($rowAttributes['type']);
                array_push($attributes, $jpPostAttributeObject);
            }
            $jpPostObject->setAttributes($attributes);

            return $jpPostObject;
        } else {
            return NULL;
        }
    }

        public function getPostByFurl($_furlName) {
        $sql = "SELECT * from jp_post where title = '" . \str_replace("-", " ", $_furlName) . "'";
        if ($row = $this->pdo->query($sql)->fetch()) {
            $jpPostObject = new PostObject();
            $jpPostObject->setID($row['id']);
            $jpPostObject->setIDPostType($row['id_posttype']);
            $jpPostObject->setTitle($row['title']);
            $jpPostObject->setSummary($row['summary']);
            $jpPostObject->setDate($row['date']);

            $sqlAttributes = "SELECT * from jp_post_attribs where id = '" . $jpPostObject->getID() . "'";
            $attributes = array();
            foreach ($this->pdo->query($sqlAttributes) as $rowAttributes) {
                $jpPostAttributeObject = new PostAttributeObject();
                $jpPostAttributeObject->setID($rowAttributes['id']);
                $jpPostAttributeObject->setIDAttribute($rowAttributes['id_attribute']);
                $jpPostAttributeObject->setIDPostType($rowAttributes['id_posttype']);
                $jpPostAttributeObject->setIntValue($rowAttributes['int_value']);
                $jpPostAttributeObject->setStrValue($rowAttributes['str_value']);
                $jpPostAttributeObject->setDateValue($rowAttributes['date_value']);
                $jpPostAttributeObject->setType($rowAttributes['type']);
                array_push($attributes, $jpPostAttributeObject);
            }
            $jpPostObject->setAttributes($attributes);

            return $jpPostObject;
        } else {
            return NULL;
        }
    }

}
