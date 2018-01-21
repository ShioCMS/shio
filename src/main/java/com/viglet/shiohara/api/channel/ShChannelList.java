package com.viglet.shiohara.api.channel;

import java.util.ArrayList;
import java.util.List;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;

public class ShChannelList {

	List<ShChannel> shChannels;

	List<ShPost> shPosts;
	
	String channelPath;

	ArrayList<ShChannel> breadcrumb;
	
	public List<ShChannel> getShChannels() {
		return shChannels;
	}

	public void setShChannels(List<ShChannel> shChannels) {
		this.shChannels = shChannels;
	}

	public List<ShPost> getShPosts() {
		return shPosts;
	}

	public void setShPosts(List<ShPost> shPosts) {
		this.shPosts = shPosts;
	}

	public String getChannelPath() {
		return channelPath;
	}

	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}

	public ArrayList<ShChannel> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(ArrayList<ShChannel> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

}
