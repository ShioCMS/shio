<?php

use Japode\PostType\Entity\JPPostTypeAttr;
use Japode\Post\Entity\JPPostAttr;
use Japode\Widget\AbstractWidget;

class JPWidgetText extends AbstractWidget {

    public function doRender(JPPostTypeAttr $_postTypeAttributeObject = NULL, JPPostAttr $_postAttributeObject = NULL) {
        if ($_postAttributeObject == null){

            $html = sprintf("<input id = '%s' name = '%s'  type='text' class='form-control'></input>", $this->getAttributeName(),  $this->getAttributeName());
        }    
        else
            $html = sprintf("<input id = '%s' name = '%s'  type='text' class='form-control' value='%s'></input>", $this->getAttributeName(),  $this->getAttributeName(), $_postAttributeObject->getStrValue());
        return $html;
    }
}
