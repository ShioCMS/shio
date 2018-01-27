package com.viglet.shiohara.api.channel;

import java.util.ArrayList;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.site.ShSite;

public class ShChannelPath {

	String channelPath;
	
	ShChannel currentChannel;
	
	ArrayList<ShChannel> breadcrumb;
	
	ShSite shSite;

	public String getChannelPath() {
		return channelPath;
	}

	public void setChannelPath(String channelPath) {
		this.channelPath = channelPath;
	}

	public ShChannel getCurrentChannel() {
		return currentChannel;
	}

	public void setCurrentChannel(ShChannel currentChannel) {
		this.currentChannel = currentChannel;
	}

	public ArrayList<ShChannel> getBreadcrumb() {
		return breadcrumb;
	}

	public void setBreadcrumb(ArrayList<ShChannel> breadcrumb) {
		this.breadcrumb = breadcrumb;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

}
