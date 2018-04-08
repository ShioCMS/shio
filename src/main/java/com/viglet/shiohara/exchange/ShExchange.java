package com.viglet.shiohara.exchange;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({ "files"})
public class ShExchange {

	private List<ShSiteExchange> sites;

	private List<ShFolderExchange> folders;
	
	private List<ShPostExchange> posts;
	
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

	
	
}
