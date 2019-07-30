/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.sites;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
public class ShSitesContextURLProcess {
	private static final Log logger = LogFactory.getLog(ShSitesContextURLProcess.class);
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShSitesContextURLProcessCache shSitesContextURLProcessCache;

	public ShSitesContextURL getContextURL(HttpServletRequest request, HttpServletResponse response) {
		ShSitesContextURL shSitesContextURL = new ShSitesContextURL();
		shSitesContextURL.setRequest(request);
		shSitesContextURL.setResponse(response);

		String shXSiteName = request.getHeader("x-sh-site");
		shSitesContextURL.getInfo().setCacheEnabled(
				request.getHeader("x-sh-nocache") != null && request.getHeader("x-sh-nocache").equals("1") ? false
						: true);
		String context = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		if (shXSiteName != null) {
			String contextOriginal = context.replaceAll("^/sites", "");
			if (request.getParameter("sh-format") != null) {
				String format = request.getParameter("sh-format");
				shSitesContextURL.getInfo()
						.setContextURL(String.format("/sites/%s/%s/en-us%s", shXSiteName, format, contextOriginal));
				shSitesContextURL.getInfo()
						.setContextURLOriginal(contextOriginal.concat(String.format("?sh-format=%s", format)));
			} else {
				shSitesContextURL.getInfo().setContextURLOriginal(context.replaceAll("^/sites", ""));
				shSitesContextURL.getInfo().setContextURL(String.format("/sites/%s/default/en-us%s", shXSiteName,
						shSitesContextURL.getInfo().getContextURLOriginal()));
			}
		} else {

			if (request.getParameter("sh-format") != null) {
				String[] contexts = context.split("/");
				contexts[3] = request.getParameter("sh-format");
				context = StringUtils.join(contexts, "/");
				shSitesContextURL.getInfo().setContextURLOriginal(context);
			} else {
				shSitesContextURL.getInfo().setContextURLOriginal(context);
			}
			shSitesContextURL.getInfo().setContextURL(shSitesContextURL.getInfo().getContextURLOriginal());
		}

		if (request.getParameter("sh-format") != null) {
			shSitesContextURL.getInfo().setShFormat(request.getParameter("sh-format"));
		}

		ShSitesContextURLInfo shSitesContextURLInfo = shSitesContextURLProcessCache.detectContextURL(shSitesContextURL);
		shSitesContextURL.setInfo(shSitesContextURLInfo);

		return shSitesContextURL;

	}

	public void detectContextURL(ShSitesContextURL shSitesContextURL) {
		String shSiteName = null;
		String[] contexts = shSitesContextURL.getInfo().getContextURL().split("/");
		for (int i = 1; i < contexts.length; i++) {
			switch (i) {
			case 1:
				shSitesContextURL.getInfo().setShContext(contexts[i]);
				break;
			case 2:
				shSiteName = contexts[i];
				break;
			case 3:
				shSitesContextURL.getInfo().setShFormat(contexts[i]);
				break;
			case 4:
				shSitesContextURL.getInfo().setShLocale(contexts[i]);
				break;
			}
		}

		// TODO: Use Database, need to be cached
		ShSite shSite = shSiteRepository.findByFurl(shSiteName);
		shSitesContextURL.getInfo().setSiteId(shSite.getId());

		ArrayList<String> contentPath = shSitesContextComponent
				.contentPathFactory(shSitesContextURL.getInfo().getContextURL());

		String objectName = shSitesContextComponent.objectNameFactory(contentPath);

		// TODO: Use Database, need to be cached
		ShFolder shFolder = shFolderUtils.folderFromPath(shSite,
				shSitesContextComponent.folderPathFactory(contentPath));
		if (shFolder != null) {
			shSitesContextURL.getInfo().setParentFolderId(shFolder.getId());
		} else {
			logger.info("No folder for " + shSitesContextURL.getInfo().getContextURL());
		}
		// TODO: Use Database, need to be cached
		ShObject shObject = shSitesContextComponent.shObjectItemFactory(shSite, shFolder, objectName);
		if (shObject != null) {
			shSitesContextURL.getInfo().setObjectId(shObject.getId());
		}

	}

	public ShSiteRepository getShSiteRepository() {
		return shSiteRepository;
	}

	public void setShSiteRepository(ShSiteRepository shSiteRepository) {
		this.shSiteRepository = shSiteRepository;
	}

}
