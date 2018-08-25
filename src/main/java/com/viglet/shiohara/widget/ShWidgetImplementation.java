package com.viglet.shiohara.widget;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

@Component
public interface ShWidgetImplementation {
	public String render(ShPostTypeAttr shPostTypeAttr, ShObject shObject);
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr);
}
