package com.viglet.shiohara.provider.exchange;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
