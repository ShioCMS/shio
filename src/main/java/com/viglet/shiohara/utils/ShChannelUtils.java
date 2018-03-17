package com.viglet.shiohara.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
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
	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;
	public JSONObject toJSON(ShChannel shChannel) {
		JSONObject shChannelItemAttrs = new JSONObject();

		JSONObject shChannelItemSystemAttrs = new JSONObject();
		shChannelItemSystemAttrs.put("id", shChannel.getId());
		shChannelItemSystemAttrs.put("title", shChannel.getName());
		shChannelItemSystemAttrs.put("link", this.channelPath(shChannel));

		shChannelItemAttrs.put("system", shChannelItemSystemAttrs);

		return shChannelItemAttrs;
	}

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
		return this.channelPath(shChannel, "/");
	}

	public String channelPath(ShChannel shChannel, String separator) {
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
				path = context + separator + path;
			}
			path = separator + path;
			return path.replaceAll(" ", "-");
		} else {
			return separator;
		}

	}

	public ShSite getSite(ShChannel shChannel) {
		ShSite shSite = null;
		if (shChannel != null) {
			boolean rootChannel = false;
			if ((shChannel.getRootChannel() == (byte) 1) || (shChannel.getParentChannel() == null)) {
				shSite = shChannel.getShSite();
			} else {
				ShChannel parentChannel = shChannel.getParentChannel();
				while (parentChannel != null && !rootChannel) {
					if ((parentChannel.getRootChannel() == (byte) 1) || (parentChannel.getParentChannel() == null)) {
						rootChannel = true;
						shSite = parentChannel.getShSite();
					} else {
						parentChannel = parentChannel.getParentChannel();
					}
				}
			}
		}
		return shSite;
	}

	public ShChannel channelFromPath(ShSite shSite, String channelPath) {
		return this.channelFromPath(shSite, channelPath, "/");
	}

	public ShChannel channelFromPath(ShSite shSite, String channelPath, String separator) {
		ShChannel currentChannel = null;
		String[] contexts = channelPath.replaceAll("-", " ").split(separator);
		for (int i = 1; i < contexts.length; i++) {
			if (currentChannel == null) {
				// When is null channel, because is rootChannel and it contains shSite attribute
				currentChannel = shChannelRepository.findByShSiteAndName(shSite, contexts[i]);
			} else {
				currentChannel = shChannelRepository.findByParentChannelAndName(currentChannel, contexts[i]);
			}

		}
		return currentChannel;
	}

	public String generateChannelLink(String channelID) {
		String shContext = "sites";
		ShChannel shChannel = shChannelRepository.findById(UUID.fromString(channelID));
		ShSite shSite = this.getSite(shChannel);
		String link = "/" + shContext + "/";
		link = link + shSite.getName().replaceAll(" ", "-");
		link = link + "/default/pt-br";
		link = link + this.channelPath(shChannel);
		return link;
	}

	public boolean deleteChannel(ShChannel shChannel) {
		for (ShPost shPost : shChannel.getShPosts()) {
			for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}
			shGlobalIdRepository.delete(shPost.getShGlobalId().getId());
			shPostRepository.delete(shPost.getId());
		}
		for (ShChannel shChannelChild : shChannel.getShChannels()) {
			this.deleteChannel(shChannelChild);
		}
		shGlobalIdRepository.delete(shChannel.getShGlobalId().getId());
		shChannelRepository.delete(shChannel.getId());
		return true;
	}
}
