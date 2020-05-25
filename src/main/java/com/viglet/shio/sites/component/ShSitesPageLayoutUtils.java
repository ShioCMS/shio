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

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shio.bean.ShSitePostTypeLayout;
import com.viglet.shio.bean.ShSitePostTypeLayouts;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.post.type.ShSystemPostTypeAttr;
import com.viglet.shio.sites.utils.ShSitesPostUtils;
import com.viglet.shio.utils.ShPostUtils;

/**
 * Page Layout Utils.
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Component
public class ShSitesPageLayoutUtils {
	static final Logger logger = LogManager.getLogger(ShSitesPageLayoutUtils.class);
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public ShPost pageLayoutFromShObject(ShObject shObjectItem, ShSite shSite, String format) {
		String shPostFolderPageLayoutId = null;
		ShPost shFolderPageLayout = null;

		if (shObjectItem instanceof ShPost) {
			ShPost shSelectedPost = shSitesPostUtils.getPostByStage((ShPost) shObjectItem);
			if (shSelectedPost != null) {
				Map<String, ShPostAttr> shFolderIndexMap = shSitesPostUtils.postToMap((ShPost) shSelectedPost);
				shPostFolderPageLayoutId = shFolderIndexMap.get(ShSystemPostTypeAttr.PAGE_LAYOUT).getStrValue();
				if (!format.toLowerCase().equals("default")) {
					ShPostAttr shPostAttrFormats = shFolderIndexMap.get("FORMATS");
					List<Map<String, ShPostAttr>> shPostAttrFormatList = shSitesPostUtils
							.relationToMap(shPostAttrFormats);
					if (shPostAttrFormatList != null) {
						for (Map<String, ShPostAttr> shPostAttrFormat : shPostAttrFormatList) {
							if (shPostAttrFormat.get("NAME").getStrValue().equals(format)) {
								shPostFolderPageLayoutId = shPostAttrFormat.get("PAGE_LAYOUT").getStrValue();
							}
						}
					}
				}

				if (shPostFolderPageLayoutId != null) {
					shFolderPageLayout = shPostRepository.findById(shPostFolderPageLayoutId).orElse(null);
				}
			}
		} else if (shObjectItem instanceof ShFolder) {
			// If Folder doesn't have PageLayout, it will try use default Folder Page Layout
			if (shSite.getPostTypeLayout() != null) {
				JSONObject postTypeLayout = new JSONObject(shSite.getPostTypeLayout());

				if (postTypeLayout.has("FOLDER")) {
					ObjectMapper mapper = new ObjectMapper();

					ShSitePostTypeLayouts shSitePostTypeLayouts;
					try {
						shSitePostTypeLayouts = mapper.readValue(postTypeLayout.get("FOLDER").toString(),
								ShSitePostTypeLayouts.class);

						String pageLayoutName = null;
						if (format == null)
							format = "default";

						for (ShSitePostTypeLayout shSitePostTypeLayout : shSitePostTypeLayouts) {
							if (shSitePostTypeLayout.getFormat().equals(format)) {
								pageLayoutName = shSitePostTypeLayout.getLayout();
							}
						}
						List<ShPost> shPostPageLayouts = shPostRepository.findByTitle(pageLayoutName);

						if (shPostPageLayouts != null) {
							for (ShPost shPostPageLayout : shPostPageLayouts) {
								if (shPostUtils.getSite(shPostPageLayout).getId().equals(shSite.getId())) {
									shFolderPageLayout = shPostPageLayout;
								}
							}
						}
					} catch (JSONException | IOException e) {
						logger.error("pageLayoutFromShObject Error", e);
					}
				}
			}
		}

		return shFolderPageLayout;
	}
}