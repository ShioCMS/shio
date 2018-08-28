package com.viglet.shiohara.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.sites.ShSitesContextURL;

@Component
public class ShHiddenWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	public String render(ShPostTypeAttr shPostTypeAttr, ShObject shObject) {
		
		String widgetSettings = shPostTypeAttr.getWidgetSettings();
		JSONObject settings = new JSONObject(widgetSettings);
		String defaultValue = settings.getString("defaultValue");
		
		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		ctx.setVariable("defaultValue", defaultValue);
		
		return templateEngine.process("widget/hidden/hidden-widget", ctx);
	}

	@Override
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr) {
		// TODO Auto-generated method stub
		return true;
	}

	@Override
	public void postRender(ShPost shPost, ShSitesContextURL shSitesContextURL) throws IOException {
		// TODO Auto-generated method stub
		
	}
}
