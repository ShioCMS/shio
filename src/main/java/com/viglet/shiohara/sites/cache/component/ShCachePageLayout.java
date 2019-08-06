package com.viglet.shiohara.sites.cache.component;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.cache.annotation.Cacheable;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.sites.ShSitesContextComponent;
import com.viglet.shiohara.sites.component.ShSitesPageLayout;
import com.viglet.shiohara.sites.nashorn.ShNashornEngineProcess;

@Component
public class ShCachePageLayout {
	static final Logger logger = LogManager.getLogger(ShCachePageLayout.class.getName());
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShNashornEngineProcess shNashornEngineProcess;

	// @Cacheable(value = "pageLayout", sync = true)
	public String cache(ShSitesPageLayout shSitesPageLayout, HttpServletRequest request, ShSite shSite,
			ApplicationContext context, String mimeType) {
		logger.debug(String.format("ShCachePageLayout.cache Key: %s %s", shSitesPageLayout.getId(),
				shSitesPageLayout.getPageCacheKey()));
		try {
			Object pageLayoutResult = shNashornEngineProcess.render(shSitesPageLayout.getJavascriptCode(),
					shSitesPageLayout.getHTML(), request, shSitesPageLayout.getShContent());

			return shSitesContextComponent
					.shRegionFactory(shSitesPageLayout, pageLayoutResult.toString(), shSite, mimeType, request).html();
		} catch (ScriptException e) {
			logger.error("ShCachePageLayout Script Exception: ", e);
			return null;
		} catch (IOException e) {
			logger.error("ShCachePageLayout IOException: ", e);
			return null;
		} catch (Exception e) {
			logger.error("ShCachePageLayout Exception: ", e);
			return null;
		}
	}
}
