package com.viglet.shiohara.api.channel;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShChannelOutput extends ShChannel {
	private static final long serialVersionUID = 1L;

	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;

	public ShChannelOutput newInstance(ShChannel shChannel) {
		this.setDate(shChannel.getDate());
		this.setId(shChannel.getId());
		this.setName(shChannel.getName());
		this.setParentChannel(shChannel.getParentChannel());
		this.setRootChannel(shChannel.getRootChannel());
		this.setShChannels(shChannel.getShChannels());
		this.setShPosts(shChannel.getShPosts());
		this.setShSite(shChannel.getShSite());
		return this;

	}

	public UUID getGlobalId() {
		return shGlobalIdRepository.findByObjectId(this.getId()).getId();
	}
}
