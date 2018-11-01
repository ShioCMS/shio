/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.api.post;

import java.util.ArrayList;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;

public class ShPostWithBreadcrumb {

	private ShPost shPost;
	
	private ArrayList<ShFolder> breadcrumb;
	
	private ShSite shSite;

	public ShPost getShPost() {
		return shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public ArrayList<ShFolder> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(ArrayList<ShFolder> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

	
	
}
