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

package com.viglet.shiohara.sites.cache.component;

import java.util.Calendar;
import java.util.List;
import java.util.Map;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.sites.ShSitesContextComponent;
import com.viglet.shiohara.sites.ShSitesContextURL;
import com.viglet.shiohara.sites.component.ShSitesPage;
import com.viglet.shiohara.sites.component.ShSitesPageLayout;
import com.viglet.shiohara.sites.utils.ShSitesPostUtils;

@Component
public class ShCachePage {
	private static final Log logger = LogFactory.getLog(ShCachePage.class);
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShSitesPage shSitesPage;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShCacheObject shCacheObject;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;

	@Cacheable(value = "page", key = "{#shSitesContextURL.getInfo().getObjectId(), #shSitesContextURL.getInfo().getContextURLOriginal()}", sync = true)
	public ShCachePageBean cache(ShSitesContextURL shSitesContextURL) {

		ShCachePageBean shCachePageBean = new ShCachePageBean();
		String mimeType = "html";

		if (logger.isDebugEnabled())
			logger.debug("Creating the page cache de id: " + shSitesContextURL.getInfo().getObjectId() + " and URL "
					+ shSitesContextURL.getInfo().getContextURLOriginal());
		shCacheObject.updateCache(shSitesContextURL.getInfo().getObjectId(), shSitesContextURL);

		ShObject shObject = shObjectRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);

		ShSite shSite = shSiteRepository.findById(shSitesContextURL.getInfo().getSiteId()).orElse(null);

		ShSitesPageLayout shSitesPageLayout = new ShSitesPageLayout();
		shSitesPageLayout.setPageCacheKey(shSitesContextURL.getInfo().getContextURLOriginal());

		if (shObject instanceof ShFolder) {
			shSitesPage.shFolderPage(shSitesPageLayout, shSite, shSitesContextURL);
			if (shSitesContextURL.getInfo().getContextURL().endsWith(".json"))
				mimeType = "json";

		} else if (shObject instanceof ShPost) {

			mimeType = shSitesPage.shPostPage(shSitesPageLayout, shSite, shSitesContextURL, mimeType);

			ShPost shPost = (ShPost) shObject;
			if (shPost.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
				String format = shSitesContextURL.getInfo().getShFormat();
				Map<String, ShPostAttr> shFolderIndexMap = shSitesPostUtils.postToMap(shPost);
				// TTL
				ShPostAttr shPostAttrCacheTTL = shFolderIndexMap.get("CACHE_TTL");
				if (shPostAttrCacheTTL != null && shPostAttrCacheTTL.getStrValue() != null) {
					int minutes = Integer.parseInt(shPostAttrCacheTTL.getStrValue());

					Calendar expirationDate = Calendar.getInstance(); 
					expirationDate.add(Calendar.MINUTE, minutes);
					shCachePageBean.setExpirationDate(expirationDate.getTime());
				}
				// Formats
				if (format.toLowerCase().equals("default")) {
					if (shSitesContextURL.getInfo().getContextURL().endsWith(".json"))
						mimeType = "json";
				} else {
					ShPostAttr shPostAttrFormats = shFolderIndexMap.get("FORMATS");
					List<Map<String, ShPostAttr>> shPostAttrFormatList = shSitesPostUtils.relationToMap(shPostAttrFormats);
					if (shPostAttrFormatList != null) {
						for (Map<String, ShPostAttr> shPostAttrFormat : shPostAttrFormatList) {
							if (shPostAttrFormat.get("NAME").getStrValue().equals(format))
								mimeType = shPostAttrFormat.get("MIME_TYPE").getStrValue();
						}
					}
				}

			}
		}

		if (mimeType.equals("json"))
			shCachePageBean.setContentType(MediaType.APPLICATION_JSON_UTF8_VALUE);
		else if (mimeType.equals("xml"))
			shCachePageBean.setContentType(MediaType.APPLICATION_XML_VALUE);
		else
			shCachePageBean.setContentType(MediaType.TEXT_HTML_VALUE);

		String shPageLayoutHTML = null;
		try {
			shPageLayoutHTML = shSitesContextComponent.shPageLayoutFactory(shSitesPageLayout,
					shSitesContextURL.getRequest(), shSite, mimeType);
		} catch (Exception e) {
			logger.error("ShCachePage Error:", e);
		}
		shCachePageBean.setBody(shPageLayoutHTML);
		return shCachePageBean;
	}

	@CacheEvict(value = "page", key = "{#id, #url}")
	public void deleteCache(String id, String url) {
	}
}
