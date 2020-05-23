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
package com.viglet.shio.sites.component;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;
import com.viglet.shio.post.type.ShSystemPostType;
import com.viglet.shio.sites.ShContent;
import com.viglet.shio.sites.ShSitesContextComponent;
import com.viglet.shio.sites.ShSitesContextURL;
import com.viglet.shio.sites.ShSitesContextURLProcess;
import com.viglet.shio.sites.utils.ShSitesFolderUtils;
import com.viglet.shio.sites.utils.ShSitesPostUtils;
import com.viglet.shio.utils.ShSiteUtils;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShSitesContent {

	@Autowired
	private ShSitesFolderUtils shSitesFolderUtils;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	@Autowired
	private ShSitesContextURLProcess shSitesContextURLProcess;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	
	public ShContent fromURL(String url) {
		ShSitesContextURL shSitesContextURL = new ShSitesContextURL();

		shSitesContextURLProcess.detectContextURL(url, shSitesContextURL);

		ShObject shObject = shObjectRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
		
		ShSite shSite = shSiteRepository.findById(shSitesContextURL.getInfo().getSiteId()).orElse(null);
		
		if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
			return this.fromFolder(shSite, shFolder);
		} else if (shObject instanceof ShPost) {
			ShPost shPost = (ShPost) shObject;
			if (shPost != null) {
				if (shPost.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
					return this.fromFolderIndex(shSite, shPost);

				} else {
					return this.fromPost(shSite, shPost);
				}
			}
		}
		return null;
	}

	public ShContent fromFolderIndex(ShSite shSite, ShPost shPost) {

		Map<String, Object> shPostItemAttrs = new HashMap<>();
		ShFolder shFolderItem = shSitesContextComponent.shFolderItemFactory(shPost);

		ShContent shFolderItemAttrs = shSitesFolderUtils.toSystemMap(shFolderItem);

		shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
		shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
		shFolderItemAttrs.put("post", shPostItemAttrs);
		shFolderItemAttrs.put("site", shSiteUtils.toSystemMap(shSite));

		return shFolderItemAttrs;
	}

	public ShContent fromPost(ShSite shSite, ShPost shPost) {

		Map<String, Object> shSiteItemAttrs = shSiteUtils.toSystemMap(shSite);

		ShContent shPostItemAttrs = shSitesPostUtils.toSystemMap(shPost);

		shPostItemAttrs.put("site", shSiteItemAttrs);

		return shPostItemAttrs;

	}

	public ShContent fromFolder(ShSite shSite, ShFolder shFolder) {

		ShContent shFolderItemAttrs = shSitesFolderUtils.toSystemMap(shFolder);

		shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolder));
		shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolder));
		shFolderItemAttrs.put("site", shSiteUtils.toSystemMap(shSite));

		return shFolderItemAttrs;

	}
}
