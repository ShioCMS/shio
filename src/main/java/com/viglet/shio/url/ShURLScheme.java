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
package com.viglet.shio.url;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.utils.ShFolderUtils;

/**
 * @author Alexandre Oliveira
 */
@Controller
public class ShURLScheme {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ShFolderUtils shFolderUtils;

	public String get(ShObject shObject) {
		String shXSiteName = request.getHeader("x-sh-site");
		String url = "";
		if (shXSiteName != null) {
			String shContext = request.getHeader("x-sh-context");
			if (shContext != null) {
				url = "/" + shContext;
			} else {
				url = "";
			}
		} else {
			String shContext = "sites";
			String shFormat = "default";
			String shLocale = "en-us";
			String shSiteName = null;
			if (shObject instanceof ShSite) {
				ShSite shSite = (ShSite) shObject;
				shSiteName = shSite.getFurl();
			} else if (shObject instanceof ShFolder) {
				ShFolder shFolder = (ShFolder) shObject;
				shSiteName = shFolderUtils.getSite(shFolder).getFurl();
			} else if (shObject instanceof ShPost) {
				ShPost shPost = (ShPost) shObject;
				ShFolder shFolder = shPost.getShFolder();
				shSiteName = shFolderUtils.getSite(shFolder).getFurl();
			}
			url = "/" + shContext + "/" + shSiteName + "/" + shFormat + "/" + shLocale;

		}
		return url;
	}

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

			url = "/" + shContext + "/" + shSiteName + "/" + shFormat + "/" + shLocale;
		}
		return url;
	}
}
