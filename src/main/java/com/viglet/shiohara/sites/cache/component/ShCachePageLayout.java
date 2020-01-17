/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shiohara.sites.cache.component;

import java.io.IOException;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.sites.ShSitesContextComponent;
import com.viglet.shiohara.sites.component.ShSitesPageLayout;
import com.viglet.shiohara.sites.nashorn.ShNashornEngineProcess;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShCachePageLayout {
	static final Logger logger = LogManager.getLogger(ShCachePageLayout.class.getName());
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShNashornEngineProcess shNashornEngineProcess;

	public String cache(ShSitesPageLayout shSitesPageLayout, HttpServletRequest request, ShSite shSite,
			ApplicationContext context, String mimeType) {
		if (shSitesPageLayout.getId() != null) {
			logger.debug(String.format("ShCachePageLayout.cache Key: %s %s", shSitesPageLayout.getId(),
					shSitesPageLayout.getPageCacheKey()));
			try {
				Object pageLayoutResult = shNashornEngineProcess.render(
						String.format("Page Layout: %s", shSitesPageLayout.getId()),
						shSitesPageLayout.getJavascriptCode(), shSitesPageLayout.getHTML(), request,
						shSitesPageLayout.getShContent());

				return shSitesContextComponent
						.shRegionFactory(shSitesPageLayout, pageLayoutResult.toString(), shSite, mimeType, request)
						.html();
			} catch (ScriptException e) {
				logger.error("ShCachePageLayout Script Exception: ", e);
			} catch (IOException e) {
				logger.error("ShCachePageLayout IOException: ", e);
			} catch (Exception e) {
				logger.error("ShCachePageLayout Exception: ", e);
			}
		}
		return null;
	}
}
