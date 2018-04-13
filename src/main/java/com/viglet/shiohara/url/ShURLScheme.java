package com.viglet.shiohara.url;

import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerMapping;

@Controller
public class ShURLScheme {
	@Autowired
	private HttpServletRequest request;

	public String get() {
		String shSiteName = request.getHeader("x-sh-site");
		String url = null;
		if (shSiteName != null) {
			url = "";
		} else {
			String contextURL = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String shContext = null;
			String shFormat = null;
			String shLocale = null;
			String[] contexts = contextURL.split("/");

			for (int i = 1; i < contexts.length; i++) {
				switch (i) {
				case 1:
					shContext = contexts[i];
					break;
				case 2:
					shSiteName = contexts[i];
					break;
				case 3:
					shFormat = contexts[i];
					break;
				case 4:
					shLocale = contexts[i];
					break;
				}
			}

			url = "/" + shContext + "/" + shSiteName.replaceAll(" ", "-") + "/" + shFormat + "/" + shLocale;
		}
		return url;
	}
}
