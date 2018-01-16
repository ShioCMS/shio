package com.viglet.shiohara.onstartup.widget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

@Component
public class ShWidgetOnStartup {

	@Autowired
	ShWidgetRepository shWidgetRepository;

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

		}

	}
}
