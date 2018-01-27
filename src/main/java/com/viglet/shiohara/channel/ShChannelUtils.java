package com.viglet.shiohara.channel;

import java.util.ArrayList;
import java.util.Collections;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@Component
public class ShChannelUtils {
	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;

	public ArrayList<ShChannel> breadcrumb(ShChannel shChannel) {
		if (shChannel != null) {
			boolean rootChannel = false;
			ArrayList<ShChannel> channelBreadcrumb = new ArrayList<ShChannel>();
			channelBreadcrumb.add(shChannel);
			ShChannel parentChannel = shChannel.getParentChannel();
			while (parentChannel != null && !rootChannel) {
				channelBreadcrumb.add(parentChannel);
				if ((parentChannel.getRootChannel() == (byte) 1) || (parentChannel.getParentChannel() == null)) {
					rootChannel = true;
				} else {
					parentChannel = parentChannel.getParentChannel();
				}
			}

			Collections.reverse(channelBreadcrumb);
			return channelBreadcrumb;
		} else {
			return null;
		}
	}

	public String channelPath(ShChannel shChannel) {
		if (shChannel != null) {
			boolean rootChannel = false;
			ArrayList<String> pathContexts = new ArrayList<String>();
			pathContexts.add(shChannel.getName());
			ShChannel parentChannel = shChannel.getParentChannel();
			while (parentChannel != null && !rootChannel) {
				pathContexts.add(parentChannel.getName());
				if ((parentChannel.getRootChannel() == (byte) 1) || (parentChannel.getParentChannel() == null)) {
					rootChannel = true;
				} else {
					parentChannel = parentChannel.getParentChannel();
				}
			}

			String path = "";

			for (String context : pathContexts) {
				path = context + "/" + path;
			}
			path = "/" + path;
			return path;
		} else {
			return "/";
		}

	}

	public ShChannel channelFromPath(String channelPath) {
		ShChannel currentChannel = null;
		String[] contexts = channelPath.split("/");
		for (int i = 1; i < contexts.length; i++) {
			currentChannel = shChannelRepository.findByParentChannelAndName(currentChannel, contexts[i]);

		}
		return currentChannel;
	}

	public boolean deleteChannel(ShChannel shChannel) {
		for (ShPost shPost : shChannel.getShPosts()) {
			for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}
			shPostRepository.delete(shPost.getId());
		}
		for (ShChannel shChannelChild : shChannel.getShChannels()) {
			this.deleteChannel(shChannelChild);
		}
		
		shChannelRepository.delete(shChannel.getId());
		return true;
	}
}
