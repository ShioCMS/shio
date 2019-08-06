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

package com.viglet.shiohara.sites.component.form;

import java.util.UUID;

import org.apache.commons.lang3.StringUtils;
import org.json.JSONObject;
import org.springframework.http.HttpMethod;

public class ShFormConfiguration {

	private HttpMethod method;
	private UUID pageLayout;
	private boolean createPost;
	private UUID folder;

	public ShFormConfiguration(JSONObject setting) {
		super();
		this.setMethod(HttpMethod.valueOf(setting.getString("method")));
		this.setPageLayout(UUID.fromString(setting.getString("pageLayout")));
		this.setCreatePost(setting.getInt("createPost") == 1 ? true : false);
		this.setFolder(
				StringUtils.isNotBlank(setting.getString("folder")) ? UUID.fromString(setting.getString("folder"))
						: null);
	}

	public HttpMethod getMethod() {
		return method;
	}

	public void setMethod(HttpMethod method) {
		this.method = method;
	}

	public UUID getPageLayout() {
		return pageLayout;
	}

	public void setPageLayout(UUID pageLayout) {
		this.pageLayout = pageLayout;
	}

	public boolean isCreatePost() {
		return createPost;
	}

	public void setCreatePost(boolean createPost) {
		this.createPost = createPost;
	}

	public UUID getFolder() {
		return folder;
	}

	public void setFolder(UUID folder) {
		this.folder = folder;
	}

}
