package com.viglet.shiohara.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.thymeleaf.context.Context;
import org.thymeleaf.spring5.SpringTemplateEngine;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.sites.ShSitesContextURL;

@Component
public class ShDateWidget implements ShWidgetImplementation {
	@Autowired
	private SpringTemplateEngine templateEngine;
	
	public String render(ShPostTypeAttr shPostTypeAttr, ShObject shObject) {
		final Context ctx = new Context();
		ctx.setVariable("shPostTypeAttr", shPostTypeAttr);
		return templateEngine.process("widget/text/text-widget", ctx);
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
