package com.viglet.shiohara.listener.onstartup;

import com.viglet.shiohara.persistence.model.ShWidget;
import com.viglet.shiohara.persistence.service.ShWidgetService;


public class ShWidgetOnStartup {
	public static void createDefaultRows() {
		ShWidgetService shWidgetService = new ShWidgetService();
		
		if (shWidgetService.listAll().isEmpty()) {

			ShWidget shWidget = new ShWidget();
			shWidget.setName("Text");
			shWidget.setDescription("Text Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextWidget");
			shWidget.setImplementationCode("template/widget/text.html");
			shWidget.setType("TEXT,TEXTAREA");
			
			shWidget = new ShWidget();
			shWidget.setName("Text Area");
			shWidget.setDescription("Text Area Widget");
			shWidget.setClassName("com.viglet.shiohara.widget.ShTextAreaWidget");
			shWidget.setImplementationCode("template/widget/textarea.html");
			shWidget.setType("TEXT,TEXTAREA");
			
		}

	}
}
