package com.viglet.shiohara.onstartup.widget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShWidgetOnStartup {

	@Autowired
	private ShWidgetRepository shWidgetRepository;

	public void createDefaultRows() {

		if (shWidgetRepository.findAll().isEmpty()) {

			ShWidget shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.TEXT);
			shWidget.setDescription("Text Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextWidget");
			shWidget.setImplementationCode("template/widget/text.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.TEXT_AREA);
			shWidget.setDescription("Text Area Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextAreaWidget");
			shWidget.setImplementationCode("template/widget/textarea.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.FILE);
			shWidget.setDescription("File Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShFileWidget");
			shWidget.setImplementationCode("template/widget/file.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.ACE_JS);
			shWidget.setDescription("Ace Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShAceEditorWidget");
			shWidget.setImplementationCode("template/widget/ace-editor-js.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidgetRepository.save(shWidget);

			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.ACE_HTML);
			shWidget.setDescription("Ace Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShAceEditorWidget");
			shWidget.setImplementationCode("template/widget/ace-editor-html.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);
			
			shWidget = new ShWidget();
			shWidget.setName(ShSystemWidget.HTML_EDITOR);
			shWidget.setDescription("HTML Editor Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShHTMLWidget");
			shWidget.setImplementationCode("template/widget/html-editor.html");
			shWidget.setType("TEXT,TEXTAREA");

			shWidgetRepository.save(shWidget);

		}

	}
}
