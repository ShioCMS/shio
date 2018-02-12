package com.viglet.shiohara.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.ShPostUtils;

@Component
public class ShQueryComponent {

	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostUtils shPostUtils;

	public List<Map<String, ShPostAttr>> findByChannelName(String channelId, String postTypeName ) {

		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		ShChannel shChannel = shChannelRepository.findById(UUID.fromString(channelId));
		List<ShPost> shPostList = shPostRepository.findByShChannelAndShPostType(shChannel, shPostType);

		List<Map<String, ShPostAttr>> shPosts = new ArrayList<Map<String, ShPostAttr>>();
		for (ShPost shPost : shPostList) {
			Map<String, ShPostAttr> shPostObject = shPostUtils.postToMap(shPost);
			shPosts.add(shPostObject);
		}
		return shPosts;
	}
}
