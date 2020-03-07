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
package com.viglet.shio.provider.exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShExchangeProviderFolder {

	private String id;

	private String name;

	private Date date;

	private String parentId;
	
	private List<ShExchangeProviderFolder> folders = new ArrayList<>();

	private List<ShExchangeProviderPost> posts = new ArrayList<>();

	private List<ShExchangeProviderBreadcrumbItem> breadcrumb = new ArrayList<>();

	private String providerName;
	
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getParentId() {
		return parentId;
	}

	public void setParentId(String parentId) {
		this.parentId = parentId;
	}

	public List<ShExchangeProviderFolder> getFolders() {
		return folders;
	}

	public void setFolders(List<ShExchangeProviderFolder> folders) {
		this.folders = folders;
	}

	public List<ShExchangeProviderPost> getPosts() {
		return posts;
	}

	public void setPosts(List<ShExchangeProviderPost> posts) {
		this.posts = posts;
	}

	public List<ShExchangeProviderBreadcrumbItem> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(List<ShExchangeProviderBreadcrumbItem> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
}
