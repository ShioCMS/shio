/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
public class ShSitesContextURLProcessCache {
	private static final Log logger = LogFactory.getLog(ShSitesContextURLProcessCache.class);
	@Autowired
	ShSitesContextURLProcess shSitesContextURLProcess;
	@Autowired
	ShObjectRepository shObjectRepository;
	@Autowired
	ShFolderUtils shFolderUtils;

	@Cacheable(value = "url", key = "{#shSitesContextURL.getInfo().getContextURL(), #shSitesContextURL.getInfo().getContextURLOriginal()}", sync = true)
	public ShSitesContextURLInfo detectContextURL(ShSitesContextURL shSitesContextURL) {
		Date now = new Date();
		if (logger.isDebugEnabled())
			logger.debug("detectContextURL: " + shSitesContextURL.toString());
		shSitesContextURLProcess.detectContextURL(shSitesContextURL);
		ShSitesContextURLInfo shSitesContextURLInfo = new ShSitesContextURLInfo();
		shSitesContextURLInfo.setCacheEnabled(shSitesContextURL.getInfo().isCacheEnabled());
		shSitesContextURLInfo.setContextURL(shSitesContextURL.getInfo().getContextURL());
		shSitesContextURLInfo.setContextURLOriginal(shSitesContextURL.getInfo().getContextURLOriginal());
		shSitesContextURLInfo.setShContext(shSitesContextURL.getInfo().getShContext());
		shSitesContextURLInfo.setShFormat(shSitesContextURL.getInfo().getShFormat());
		shSitesContextURLInfo.setShLocale(shSitesContextURL.getInfo().getShLocale());
		shSitesContextURLInfo.setObjectId(shSitesContextURL.getInfo().getObjectId());
		shSitesContextURLInfo.setParentFolderId(shSitesContextURL.getInfo().getParentFolderId());
		shSitesContextURLInfo.setSiteId(shSitesContextURL.getInfo().getSiteId());

		if (shSitesContextURLInfo.getObjectId() != null) {
			ShObject shObject = shObjectRepository.findById(shSitesContextURLInfo.getObjectId()).orElse(null);
			if (shSitesContextURL.getInfo().getObjectId() != null && shObject instanceof ShPost
					&& ((ShPost) shObject).getShPostType().getName().equals(ShSystemPostType.FILE))
				shSitesContextURLInfo.setStaticFile(true);
			else
				shSitesContextURLInfo.setStaticFile(false);

			if (shObject instanceof ShPost && shObject.getFurl().equals("index")) {
				ShFolder shFolder = shFolderUtils.getParentFolder(shObject);
				shSitesContextURLInfo.setPageAllowGuestUser(shFolder.isPageAllowGuestUser());
				shSitesContextURLInfo.setPageAllowRegisterUser(shFolder.isPageAllowRegisterUser());

				shSitesContextURLInfo.setShPageGroups(shFolder.getShPageGroups() != null
						? shFolder.getShPageGroups().toArray(new String[shFolder.getShPageGroups().size()])
						: null);
			} else {
				shSitesContextURLInfo.setPageAllowGuestUser(shObject.isPageAllowGuestUser());
				shSitesContextURLInfo.setPageAllowRegisterUser(shObject.isPageAllowRegisterUser());
				shSitesContextURLInfo.setShPageGroups(shObject.getShPageGroups() != null
						? (String[]) shObject.getShPageGroups().toArray(new String[shObject.getShPageGroups().size()])
						: null);
			}
		} else {
			shSitesContextURLInfo.setPageAllowGuestUser(true);
			shSitesContextURLInfo.setPageAllowRegisterUser(false);
			shSitesContextURLInfo.setStaticFile(false);
			shSitesContextURLInfo.setShPageGroups(null);
		}

		if (logger.isDebugEnabled())
			logger.debug("detectContextURL After: " + shSitesContextURL.toString());
		Date after = new Date();
		if (logger.isDebugEnabled())
			logger.debug("URL Time: " + (after.getTime() - now.getTime()));
		return shSitesContextURLInfo;

	}
}
