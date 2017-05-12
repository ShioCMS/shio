<?php

use Japode\PostType\Entity\JPPostTypeAttr;
use Japode\Post\Entity\JPPostAttr;
use Japode\Widget\AbstractWidget;

class JPWidgetComboBox extends AbstractWidget {

    public function doRender(JPPostTypeAttr $_postTypeAttributeObject = NULL, JPPostAttr $_postAttributeObject = NULL) {
        if ($_postAttributeObject == null)
            $html = sprintf("<select id = '%s' name = '%s'><option value='volvo'>Volvo</option><option value='saab'>Saab</option><option value='mercedes'>Mercedes</option><option value= audi'>Audi</option></select>", $this->getAttributeName(), $this->getAttributeName());
        else
            $html = sprintf("<select id = '%s' name = '%s'><option value='$s'>Volvo</option><option value='saab'>Saab</option><option value='mercedes'>Mercedes</option><option value= audi'>Audi</option></select>", $this->getAttributeName(), $this->getAttributeName(), $_postAttributeObject->getStrValue());
        return $html;
    }

}
