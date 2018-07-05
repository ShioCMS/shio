package com.viglet.shiohara.preview;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.cache.ShCacheManager;
import com.viglet.shiohara.cache.ShCachedObject;
import com.viglet.shiohara.sites.ShSitesContext;

@Controller
public class ShPreviewContext {
	@Autowired
	private ShSitesContext shSitesContext;
	@RequestMapping("/preview1/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ScriptException {
		String shXSiteName = request.getHeader("x-sh-site");
		boolean shXNoCache = request.getHeader("x-sh-nocache") != null && request.getHeader("x-sh-nocache").equals("1")
				? true
				: false;
		final int cacheMinutes = 1;
		if (shXSiteName != null) {
			String shFormat = "default";
			String shLocale = "en-us";
			String contextURL = "/sites/" + shXSiteName + "/" + shFormat + "/" + shLocale
					+ ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
							.replaceAll("^/sites/", "/");
			ShCachedObject shCacheObject = (ShCachedObject) ShCacheManager.getCache(contextURL);
			if (shCacheObject != null) {
				// End Page Layout
				// System.out.println("Is cached " + contextURL);
				response.setContentType(MediaType.TEXT_HTML_VALUE);
				response.getWriter().write(((String) shCacheObject.object).toString());
			} else {
				// System.out.println("Is not cached " + contextURL);
				String html = shSitesContext.siteContext(shXSiteName, "default", "en-us", 2, request, response);
				shCacheObject = new ShCachedObject(html, contextURL, cacheMinutes);
				/* Place the object into the cache! */
				ShCacheManager.putCache(shCacheObject);
			}
		} else {

			@SuppressWarnings("unused")
			String shContext = null;
			String contextURL = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String shSiteName = null;
			String shFormat = null;
			String shLocale = null;
			String[] contexts = contextURL.split("/");

			ShCachedObject shCacheObject = (ShCachedObject) ShCacheManager.getCache(contextURL);
			if (shXNoCache) {
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
				shSitesContext.siteContext(shSiteName, shFormat, shLocale, 5, request, response);
			} else {
				if (shCacheObject != null) {
					// End Page Layout
					// System.out.println("Is cached " + contextURL);
					response.setContentType(MediaType.TEXT_HTML_VALUE);
					response.getWriter().write(((String) shCacheObject.object).toString());
				} else {

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
					// System.out.println("Is not cached " + contextURL);
					String html = shSitesContext.siteContext(shSiteName, shFormat, shLocale, 5, request, response);
					shCacheObject = new ShCachedObject(html, contextURL, cacheMinutes);
					/* Place the object into the cache! */
					ShCacheManager.putCache(shCacheObject);
				}
			}
		}
	}
}
