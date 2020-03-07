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
package com.viglet.shio.exchange;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

/**
 * @author Alexandre Oliveira
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShExchange {

	@JsonInclude(Include.NON_NULL)
	private List<ShSiteExchange> sites;

	@JsonInclude(Include.NON_NULL)
	private List<ShFolderExchange> folders;

	@JsonInclude(Include.NON_NULL)
	private List<ShPostExchange> posts;

	@JsonInclude(Include.NON_NULL)
	private List<ShPostTypeExchange> postTypes;

	@JsonInclude(Include.NON_NULL)
	private List<ShFileExchange> files;

	public List<ShSiteExchange> getSites() {
		return sites;
	}

	public void setSites(List<ShSiteExchange> sites) {
		this.sites = sites;
	}

	public List<ShFolderExchange> getFolders() {
		return folders;
	}

	public void setFolders(List<ShFolderExchange> folders) {
		this.folders = folders;
	}

	public List<ShPostExchange> getPosts() {
		return posts;
	}

	public void setPosts(List<ShPostExchange> posts) {
		this.posts = posts;
	}

	public List<ShFileExchange> getFiles() {
		return files;
	}

	public void setFiles(List<ShFileExchange> files) {
		this.files = files;
	}

	public List<ShPostTypeExchange> getPostTypes() {
		return postTypes;
	}

	public void setPostTypes(List<ShPostTypeExchange> postTypes) {
		this.postTypes = postTypes;
	}

}
