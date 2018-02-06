package com.viglet.shiohara.exchange;

import java.util.List;

public class ShExchange {

	List<ShSiteExchange> sites;

	List<ShChannelExchange> channels;
	
	List<ShPostExchange> posts;
	
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

	
	
}
