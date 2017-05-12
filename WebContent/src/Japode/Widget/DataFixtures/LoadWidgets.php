<?php

use Doctrine\Common\DataFixtures\FixtureInterface;
use Doctrine\Common\Persistence\ObjectManager;
use Japode\Widget\Entity\JPWidget;

class LoadWidgets implements FixtureInterface {
    public function load(ObjectManager $manager) {
        
        $richText = new JPWidget();
        $richText
                ->setId('richtext')
                ->setName('Editor')
                ->setDescription('Editor')
                ->setImplementationCode('/jp-widgets/class-jp-widget-richtext.php')
                ->setClassName('JPWidgetRichText')
                ->setType('string,clob');
        $manager->persist($richText);
        $manager->flush();

        $file = new JPWidget();
        $file
                ->setId('file')
                ->setName('File')
                ->setDescription('File')
                ->setImplementationCode('/jp-widgets/class-jp-widget-file.php')
                ->setClassName('JPWidgetFile')
                ->setType('string');
        $manager->persist($file);
        $manager->flush();

        $markdown = new JPWidget();
        $markdown
                ->setId('markdown')
                ->setName('Markdown')
                ->setDescription('Markdown')
                ->setImplementationCode('/jp-widgets/class-jp-widget-markdown.php')
                ->setClassName('JPWidgetMarkdown')
                ->setType('string,clob');
        $manager->persist($markdown);
        $manager->flush();
        
        $comboBox = new JPWidget();
        $comboBox
                ->setId('combobox')
                ->setName('Selection List')
                ->setDescription('Selection List')
                ->setImplementationCode('/jp-widgets/class-jp-widget-combo.php')
                ->setClassName('JPWidgetComboBox')
                ->setType('string,clob');
        $manager->persist($comboBox);
        $manager->flush();

        $textArea = new JPWidget();
        $textArea
                ->setId('textarea')
                ->setName('Text Area')
                ->setDescription('A Text Box')
                ->setImplementationCode('/jp-widgets/class-jp-widget-textarea.php')
                ->setClassName('JPWidgetTextArea')
                ->setType('string,clob');
        $manager->persist($textArea);
        $manager->flush();
        
        $textField = new JPWidget();
        $textField
                ->setId('textfield')
                ->setName('Text Field')
                ->setDescription('A Text Field')
                ->setImplementationCode('/jp-widgets/class-jp-widget-text.php')
                ->setClassName('JPWidgetText')
                ->setType('string,int,float,clob');
        $manager->persist($textField);
        $manager->flush();


    }


}
