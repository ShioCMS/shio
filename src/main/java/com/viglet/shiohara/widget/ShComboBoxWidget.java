package com.viglet.shiohara.widget;

import org.json.JSONObject;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

@Component
public class ShComboBoxWidget implements ShWidgetImplementation {

	public String render(ShPostTypeAttr shPostTypeAttr) {
		StringBuilder sb = new StringBuilder();
		String widgetSettings = shPostTypeAttr.getWidgetSettings();
		JSONObject settings = new JSONObject(widgetSettings);
		String[] choices = settings.getString("choices").split("\n");
		sb.append("<div class=\"form-group\">");
		sb.append("<label>" + shPostTypeAttr.getLabel() + "</label>");
		sb.append("\"<select class=\"form-control\" name=\"__sh-post-type-attr-" + shPostTypeAttr.getName() + "\">");
		for (String choice : choices) {
			String[] choiceKV = choice.split(":");
			String name = choiceKV[0].trim();
			String label = choiceKV[1].trim();

			sb.append("<option value=\"" + name + "\">" + label + "</option>");
		}

		sb.append("</select>");

		sb.append("</div>");
		return sb.toString();
	}
}
