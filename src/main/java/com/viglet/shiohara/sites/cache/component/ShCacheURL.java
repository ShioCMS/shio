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

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.sites.ShSitesContextURL;

@Component
public class ShCacheURL {
	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ShCacheURL.class);

	@CacheEvict(value = "url", key = "{#contextURL, #contextURLOriginal}")
	public void deleteCache(String contextURL, String contextURLOriginal) {
		System.out.println(String.format("deleteURL Cache: %s %s",contextURL,contextURLOriginal));
	}
}
