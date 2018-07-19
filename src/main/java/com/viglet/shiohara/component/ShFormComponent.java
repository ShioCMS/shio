package com.viglet.shiohara.component;

import javax.annotation.Resource;
import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.web.csrf.CsrfToken;
import org.springframework.stereotype.Component;
import org.springframework.web.util.WebUtils;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.widget.ShWidgetImplementation;

@Component
public class ShFormComponent {
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Resource
	private ApplicationContext applicationContext;

	public String byPostType(String shPostTypeName, HttpServletRequest request)
			throws InstantiationException, IllegalAccessException, ClassNotFoundException {

		ShPostType shPostType = shPostTypeRepository.findByName(shPostTypeName);

		CsrfToken csrf = (CsrfToken) request.getAttribute(CsrfToken.class.getName());

		StringBuilder sb = new StringBuilder();
		sb.append("<form  action=\"\" method=\"POST\">");
		if (csrf != null) {
			String token = csrf.getToken();
			if (token != null) {
				sb.append("<input type=\"hidden\" name=\"XSRF-TOKEN\" value=\"" + token + "\" />");
			}
		}
		sb.append("<input type=\"hidden\" name=\"__sh-post-type\" value=\"" + shPostType.getName() + "\" />");
		sb.append("</div>");
		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			String className = shPostTypeAttr.getShWidget().getClassName();
			ShWidgetImplementation object = (ShWidgetImplementation) Class.forName(className).newInstance();
			applicationContext.getAutowireCapableBeanFactory().autowireBean(object);
			sb.append(object.render(shPostTypeAttr));
		}
		sb.append("<button type=\"submit\">Save</button>");
		sb.append("</form>");
		return sb.toString();
	}
}
