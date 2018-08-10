package com.viglet.shiohara.widget;

import javax.servlet.http.HttpServletRequest;

import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

@Component
public interface ShWidgetImplementation {
	public String render(ShPostTypeAttr shPostTypeAttr);
	public boolean validateForm(HttpServletRequest request, ShPostTypeAttr shPostTypeAttr);
}
