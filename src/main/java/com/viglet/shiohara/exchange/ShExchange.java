package com.viglet.shiohara.exchange;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

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
