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
package com.viglet.shiohara.sites.component;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.bean.ShSitePostTypeLayout;
import com.viglet.shiohara.bean.ShSitePostTypeLayouts;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.sites.ShSitesContextComponent;
import com.viglet.shiohara.sites.ShSitesContextURL;
import com.viglet.shiohara.sites.utils.ShSitesFolderUtils;
import com.viglet.shiohara.sites.utils.ShSitesPostUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;

@Component
public class ShSitesPage {
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShSitesFolderUtils shSitesFolderUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;

	public String shPostPage(ShSitesPageLayout shSitesPageLayout, ShSite shSite, ShSitesContextURL shSitesContextURL,
			String mimeType) {

		ShPost shPostItem = shPostRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
		if (shPostItem != null) {
			if (shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
				// Folder Index
				ShFolder shFolderItem = null;
				shFolderItem = shSitesContextComponent.shFolderItemFactory(shPostItem);
				Map<String, Object> shPostItemAttrs = new HashMap<>();
				Map<String, Object> shFolderItemAttrs = shSitesFolderUtils.toSystemMap(shFolderItem);
				Map<String, ShPostAttr> shFolderPageLayoutMap = shSitesContextComponent
						.shFolderPageLayoutMapFactory(shPostItem, shSite, shSitesContextURL.getInfo().getShFormat());
				if (shFolderPageLayoutMap != null) {
					shSitesPageLayout.setId(shFolderPageLayoutMap.get(ShSystemPostTypeAttr.ID).getStrValue());
					shSitesPageLayout.setHTML(shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue());
					shSitesPageLayout.setJavascriptCode(
							shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());

					String shPostThemeId = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();

					Map<String, Object> shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);
					shPostItemAttrs.put("theme", shThemeAttrs);
					shFolderItemAttrs.put("theme", shThemeAttrs);

					shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
					shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
					shFolderItemAttrs.put("post", shPostItemAttrs);
					shFolderItemAttrs.put("site", shSiteUtils.toSystemMap(shSite));

					shSitesPageLayout.setShContent(shFolderItemAttrs);
				}
			} else {
				// Other Post
				JSONObject postTypeLayout = new JSONObject();

				if (shSite.getPostTypeLayout() != null) {
					postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
				}
				ObjectMapper mapper = new ObjectMapper();

				ShSitePostTypeLayouts shSitePostTypeLayouts;
				try {
					shSitePostTypeLayouts = mapper.readValue(
							postTypeLayout.get(shPostItem.getShPostType().getName()).toString(),
							ShSitePostTypeLayouts.class);

					String pageLayoutName = null;
					String format = shSitesContextURL.getInfo().getShFormat();
					if (format == null)
						format = "default";

					for (ShSitePostTypeLayout shSitePostTypeLayout : shSitePostTypeLayouts) {
						if (shSitePostTypeLayout.getFormat().equals(format)) {
							pageLayoutName = shSitePostTypeLayout.getLayout();
							if (shSitePostTypeLayout.getMimeType() != null) {
								mimeType = shSitePostTypeLayout.getMimeType();
							}
						}
					}
					List<ShPost> shPostPageLayouts = shPostRepository.findByTitle(pageLayoutName);

					Map<String, ShPostAttr> shPostPageLayoutMap = null;

					if (shPostPageLayouts != null) {
						for (ShPost shPostPageLayout : shPostPageLayouts) {
							if (shPostUtils.getSite(shPostPageLayout).getId().equals(shSite.getId())) {
								shPostPageLayoutMap = shSitesPostUtils.postToMap(shPostPageLayout);

							}
						}
					}

					if (shPostPageLayoutMap != null) {
						shSitesPageLayout.setId(shPostPageLayoutMap.get(ShSystemPostTypeAttr.ID).getStrValue());
						shSitesPageLayout.setHTML(shPostPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue());
						shSitesPageLayout.setJavascriptCode(
								shPostPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());

						String shPostThemeId = shPostPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
						Map<String, Object> shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

						Map<String, Object> shSiteItemAttrs = shSiteUtils.toSystemMap(shSite);

						Map<String, Object> shPostItemAttrs = shSitesPostUtils.toSystemMap(shPostItem);

						shPostItemAttrs.put("theme", shThemeAttrs);
						shPostItemAttrs.put("site", shSiteItemAttrs);

						shSitesPageLayout.setShContent(shPostItemAttrs);

					}
				} catch (JSONException | IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}
		
		return mimeType;
	}

	public void shFolderPage(ShSitesPageLayout shSitesPageLayout, ShSite shSite, ShSitesContextURL shSitesContextURL) {
		ShFolder shFolderItem = shFolderRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
		Map<String, Object> shPostItemAttrs = new HashMap<>();
		Map<String, Object> shFolderItemAttrs = shSitesFolderUtils.toSystemMap(shFolderItem);

		Map<String, ShPostAttr> shFolderPageLayoutMap = shSitesContextComponent
				.shFolderPageLayoutMapFactory(shFolderItem, shSite, shSitesContextURL.getInfo().getShFormat());
		if (shFolderPageLayoutMap != null) {
			shSitesPageLayout.setId(shFolderPageLayoutMap.get(ShSystemPostTypeAttr.ID).getStrValue());
			shSitesPageLayout.setHTML(shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue());
			shSitesPageLayout
					.setJavascriptCode(shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());

			String shPostThemeId = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();

			Map<String, Object> shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

			shPostItemAttrs.put("theme", shThemeAttrs);
			shFolderItemAttrs.put("theme", shThemeAttrs);

			shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
			shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
			shFolderItemAttrs.put("post", shPostItemAttrs);
			shFolderItemAttrs.put("site", shSiteUtils.toSystemMap(shSite));

			shSitesPageLayout.setShContent(shFolderItemAttrs);
		}
	}
}
