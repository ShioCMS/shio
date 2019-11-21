package com.viglet.shiohara.provider;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ShProviderFolder {

	private String id;

	private String name;

	private Date date;

	private String parentId;
	
	private List<ShProviderFolder> folders = new ArrayList<>();

	private List<ShProviderPost> posts = new ArrayList<>();

	private List<ShProviderBreadcrumbItem> breadcrumb = new ArrayList<>();

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

	public List<ShProviderFolder> getFolders() {
		return folders;
	}

	public void setFolders(List<ShProviderFolder> folders) {
		this.folders = folders;
	}

	public List<ShProviderPost> getPosts() {
		return posts;
	}

	public void setPosts(List<ShProviderPost> posts) {
		this.posts = posts;
	}

	public List<ShProviderBreadcrumbItem> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(List<ShProviderBreadcrumbItem> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public String getProviderName() {
		return providerName;
	}

	public void setProviderName(String providerName) {
		this.providerName = providerName;
	}
}
