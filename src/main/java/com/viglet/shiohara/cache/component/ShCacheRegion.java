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

package com.viglet.shiohara.cache.component;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.sites.ShSitesContextComponent;
import com.viglet.shiohara.sites.components.ShSitesPageLayout;
import com.viglet.shiohara.utils.stage.ShStagePostUtils;

@Component
public class ShCacheRegion {
	static final Logger logger = LogManager.getLogger(ShCacheRegion.class.getName());
	@Autowired
	private ShStagePostUtils shStagePostUtils;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;

	@Cacheable(value = "region", key = "{#root.methodName, #regionName, #shSite.getId()}", sync = true)
	public String templateScopeCache(String regionName, ShSitesPageLayout shSitesPageLayout, ShSite shSite,
			StringBuilder shObjectJS, String mimeType, HttpServletRequest request) {
		return shSitesContextComponent.regionProcess(regionName, shSitesPageLayout, shSite, shObjectJS, mimeType,
				request);
	}

	@Cacheable(value = "region", key = "{#root.methodName, #regionName, #siteId}", sync = true)
	public boolean isCached(String regionName, String siteId) {
		ShPost shRegion = shSitesContextComponent.getRegion(regionName, siteId);
		if (shRegion != null) {
			Map<String, ShPostAttr> shRegionPostMap = shStagePostUtils.postToMap(shRegion);
			if (shRegionPostMap.get(ShSystemPostTypeAttr.CACHED) != null) {
				return shRegionPostMap.get(ShSystemPostTypeAttr.CACHED).getStrValue().equals("yes") ? true : false;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

}
