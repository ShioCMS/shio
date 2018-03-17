package com.viglet.shiohara.exchange;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
@JsonIgnoreProperties({ "files"})
public class ShExchange {

	List<ShSiteExchange> sites;

	List<ShChannelExchange> channels;
	
	List<ShPostExchange> posts;
	
	List<ShFileExchange> files;
	
	public List<ShSiteExchange> getSites() {
		return sites;
	}

	public void setSites(List<ShSiteExchange> sites) {
		this.sites = sites;
	}

	public List<ShChannelExchange> getChannels() {
		return channels;
	}

	public void setChannels(List<ShChannelExchange> channels) {
		this.channels = channels;
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
