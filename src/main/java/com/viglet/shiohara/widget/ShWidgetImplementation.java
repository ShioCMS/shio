package com.viglet.shiohara.widget;

import java.io.IOException;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.sites.ShSitesContextURL;

@Component
public interface ShWidgetImplementation {
	public String render(ShPostTypeAttr shPostTypeAttr, ShObject shObject);
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr);
	public void postRender(ShPost shPost, ShSitesContextURL shSitesContextURL) throws IOException;
}
