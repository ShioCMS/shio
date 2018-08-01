package com.viglet.shiohara.widget;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

@Component
public class ShComboBoxWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;

	public String render(ShPostTypeAttr shPostTypeAttr) {

		String widgetSettings = shPostTypeAttr.getWidgetSettings();
		JSONObject settings = new JSONObject(widgetSettings);
		String[] choicesArray = settings.getString("choices").split("\n");
		Map<String, String> choices = new HashMap<String, String>();
		for (String choice : choicesArray) {
			String[] choiceKV = choice.split(":");
			String name = choiceKV[0].trim();
			String label = choiceKV[1].trim();
			choices.put(name, label);
		}

		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		ctx.setVariable("choices", choices);

		return templateEngine.process("widget/combo-box/combo-box-widget", ctx);
	}
}
