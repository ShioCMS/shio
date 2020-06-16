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

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * @author Alexandre Oliveira
 */
@JsonIgnoreProperties(ignoreUnknown = true)
public class ShSiteExchange {
	
	private String id;

	private String name;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
	private Date date;

	private String description;

	private String url;

	private String postTypeLayout;
	
	private String searchablePostTypes;
	
	private String owner;
	
	private String furl;
	
	private List<String> rootFolders;

	public ShSiteExchange() {
		super();
	}
	
	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public List<String> getRootFolders() {
		return rootFolders;
	}

	public void setRootFolders(List<String> rootFolders) {
		this.rootFolders = rootFolders;
	}

	public String getPostTypeLayout() {
		return postTypeLayout;
	}

	public void setPostTypeLayout(String postTypeLayout) {
		this.postTypeLayout = postTypeLayout;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public String getSearchablePostTypes() {
		return searchablePostTypes;
	}

	public void setSearchablePostTypes(String searchablePostTypes) {
		this.searchablePostTypes = searchablePostTypes;
	}

}
