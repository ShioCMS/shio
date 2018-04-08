package com.viglet.shiohara.onstartup.widget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

@Component
public class ShWidgetOnStartup {

	@Autowired
	private ShWidgetRepository shWidgetRepository;

	public void createDefaultRows() {

		if (shWidgetRepository.findAll().isEmpty()) {

			ShWidget shWidget = new ShWidget();
			shWidget.setName("Text");
			shWidget.setDescription("Text Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextWidget");
			shWidget.setImplementationCode("template/widget/text.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName("Text Area");
			shWidget.setDescription("Text Area Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextAreaWidget");
			shWidget.setImplementationCode("template/widget/textarea.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName("File");
			shWidget.setDescription("File Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShFileWidget");
			shWidget.setImplementationCode("template/widget/file.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName("Ace Editor - Javascript");
			shWidget.setDescription("Ace Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShAceEditorWidget");
			shWidget.setImplementationCode("template/widget/ace-editor-js.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName("Ace Editor - HTML");
			shWidget.setDescription("Ace Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShAceEditorWidget");
			shWidget.setImplementationCode("template/widget/ace-editor-html.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName("HTML Editor");
			shWidget.setDescription("HTML Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShHTMLWidget");
			shWidget.setImplementationCode("template/widget/html-editor.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

		}

	}
}
