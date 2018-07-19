package com.viglet.shiohara.widget;

import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

@Component
public class ShTextWidget implements ShWidgetImplementation {

	public String render(ShPostTypeAttr shPostTypeAttr) {
		StringBuilder sb = new StringBuilder();
		sb.append("<div class=\"form-group\">");
		sb.append("<label>" + shPostTypeAttr.getLabel() + "</label>");
		sb.append("<input type=\"text\" class=\"form-control\" size=\"50\" name=\"__sh-post-type-attr-"
				+ shPostTypeAttr.getName() + "\" />");
		sb.append("</div>");
		return sb.toString();
	}
}
