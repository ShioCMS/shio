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
package com.viglet.shio.utils;

import java.util.HashMap;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.url.ShURLScheme;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShSiteUtils {
	@Autowired
	ShURLScheme shURLScheme;

	public Map<String, Object> toSystemMap(ShSite shSite) {
		Map<String, Object> shSiteItemSystemAttrs = new HashMap<>();
		shSiteItemSystemAttrs.put("id", shSite.getId());
		shSiteItemSystemAttrs.put("title", shSite.getName());
		shSiteItemSystemAttrs.put("summary", shSite.getDescription());
		shSiteItemSystemAttrs.put("link", shURLScheme.get(shSite));

		Map<String, Object> shSiteItemAttrs = new HashMap<>();

		shSiteItemAttrs.put("system", shSiteItemSystemAttrs);
		return shSiteItemAttrs;
	}
	
	public JSONObject toJSON(ShSite shSite) {
		JSONObject shSiteItemSystemAttrs = new JSONObject();
		shSiteItemSystemAttrs.put("id", shSite.getId());
		shSiteItemSystemAttrs.put("title", shSite.getName());
		shSiteItemSystemAttrs.put("summary", shSite.getDescription());
		shSiteItemSystemAttrs.put("link", shURLScheme.get(shSite));

		JSONObject shSiteItemAttrs = new JSONObject();

		shSiteItemAttrs.put("system", shSiteItemSystemAttrs);
		return shSiteItemAttrs;
	}

	public String generatePostLink(ShSite shSite) {
		String link = shURLScheme.get(shSite);
		return link;
	}

}
