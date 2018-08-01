package com.viglet.shiohara.widget;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

@Component
public class ShRelatorWidget implements ShWidgetImplementation {

	@Autowired
	private SpringTemplateEngine templateEngine;
	
	public String render(ShPostTypeAttr shPostTypeAttr) {
		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		return templateEngine.process("widget/text/text-widget", ctx);
	}
}
