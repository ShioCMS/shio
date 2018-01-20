package com.viglet.shiohara.api.channel;

import com.viglet.shiohara.persistence.model.channel.ShChannel;

public class ShChannelPath {

	String channelPath;
	
	ShChannel currentChannel;

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

}
