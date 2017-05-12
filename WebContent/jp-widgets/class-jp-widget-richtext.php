<?php

use Japode\PostType\Entity\JPPostTypeAttr;
use Japode\Post\Entity\JPPostAttr;
use Japode\Widget\AbstractWidget;

class JPWidgetRichText extends AbstractWidget {

    public function doRender(JPPostTypeAttr $_postTypeAttributeObject = NULL, JPPostAttr $_postAttributeObject = NULL) {
        $this->template = $this->twig->loadTemplate("widgetRichText.twig");
        return $this->template->render(array(
                    'postTypeAttributeObject' => $_postTypeAttributeObject,
                    'postAttributeObject' => $_postAttributeObject,
                    'attributeName' => $this->getAttributeName(),
        ));

        /*
        if ($_postAttributeObject == null)
            $html = sprintf("<textarea id='%s' name='%s' ></textarea>",  $this->getAttributeName(),  $this->getAttributeName());
        else 
            $html = sprintf("<textarea id='%s' name='%s' >%s</textarea>",  $this->getAttributeName(),  $this->getAttributeName(), $_postAttributeObject->getStrValue());
        return $html;
         * 
         */
    }
}
