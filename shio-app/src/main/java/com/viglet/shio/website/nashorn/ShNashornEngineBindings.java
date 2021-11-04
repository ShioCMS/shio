/*
 * Copyright (C) 2016-2021 the original author or authors. 
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
package com.viglet.shio.website.nashorn;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.property.ShWebsiteProperties;
import com.viglet.shio.website.component.ShGetRelationComponent;
import com.viglet.shio.website.component.ShNavigationComponent;
import com.viglet.shio.website.component.ShQueryComponent;
import com.viglet.shio.website.component.ShSearchComponent;
import com.viglet.shio.website.component.form.ShFormComponent;
import com.viglet.shio.website.utils.ShSitesFolderUtils;
import com.viglet.shio.website.utils.ShSitesObjectUtils;
import com.viglet.shio.website.utils.ShSitesPostUtils;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.9
 */
@Component
public class ShNashornEngineBindings {
	@Autowired
	private ShNavigationComponent shNavigationComponent;
	@Autowired
	private ShQueryComponent shQueryComponent;
	@Autowired
	private ShSearchComponent shSearchComponent;
	@Autowired
	private ShFormComponent shFormComponent;
	@Autowired
	private ShSitesFolderUtils shSitesFolderUtils;
	@Autowired
	private ShSitesObjectUtils shSitesObjectUtils;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	@Autowired
	private ShGetRelationComponent shGetRelationComponent;
	@Autowired
	private ShWebsiteProperties shWebsiteProperties;

	public ShNavigationComponent getShNavigationComponent() {
		return shNavigationComponent;
	}

	public ShQueryComponent getShQueryComponent() {
		return shQueryComponent;
	}

	public ShSearchComponent getShSearchComponent() {
		return shSearchComponent;
	}

	public ShFormComponent getShFormComponent() {
		return shFormComponent;
	}

	public ShSitesFolderUtils getShSitesFolderUtils() {
		return shSitesFolderUtils;
	}

	public ShSitesObjectUtils getShSitesObjectUtils() {
		return shSitesObjectUtils;
	}

	public ShSitesPostUtils getShSitesPostUtils() {
		return shSitesPostUtils;
	}

	public ShGetRelationComponent getShGetRelationComponent() {
		return shGetRelationComponent;
	}

	public ShWebsiteProperties getShWebsiteProperties() {
		return shWebsiteProperties;
	}

}
