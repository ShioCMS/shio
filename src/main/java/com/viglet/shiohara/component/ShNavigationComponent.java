package com.viglet.shiohara.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShNavigationComponent {
	@Autowired
	ShSiteRepository shSiteRepository;
	@Autowired
	ShChannelRepository shChannelRepository;

	public List<ShChannel> navigation(String siteName, boolean home) {
		ShSite shSite = shSiteRepository.findByName(siteName);
		ShChannel homeChannel = shChannelRepository.findByShSiteAndName(shSite, "Home");
		List<ShChannel> shChannels = new ArrayList<ShChannel>();
		if (home) {
			shChannels.add(homeChannel);
		}
		shChannels.addAll(shChannelRepository.findByParentChannel(homeChannel));
		return shChannels;
	}
}
