<?php

use Japode\PostType\Entity\JPPostTypeAttr;
use Japode\Post\Entity\JPPostAttr;
use Japode\Widget\AbstractWidget;

class JPWidgetMarkdown extends AbstractWidget {

    public function doRender(JPPostTypeAttr $_postTypeAttributeObject = NULL, JPPostAttr $_postAttributeObject = NULL) {
        $this->template = $this->twig->loadTemplate("widgetMarkdown.twig");
        return $this->template->render(array(
                    'postTypeAttributeObject' => $_postTypeAttributeObject,
                    'postAttributeObject' => $_postAttributeObject,
                    'attributeName' => $this->getAttributeName(),
        ));
    }

  /*  public function processAction() {
        $tmp_file_name = $_FILES['Filedata']['tmp_name'];
        $fileName = $_FILES['Filedata']['name']; 
        $pathAndName = "/Applications/MAMP/htdocs/japode/jp-staticfiles/".$fileName;
        $contextFileName="/jp-staticfiles/".$fileName;
        $ok = move_uploaded_file($tmp_file_name, $pathAndName);

        return trim($contextFileName);
    }
    */
    
    public function doEdit() {
        $this->template = $this->twig->loadTemplate("widgetFileUploadEdit.twig");
        return $this->template->render(array(
                    'none' => 'none',
        ));
    }

}
