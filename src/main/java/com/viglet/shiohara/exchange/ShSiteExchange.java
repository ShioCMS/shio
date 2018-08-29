package com.viglet.shiohara.exchange;

import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

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
