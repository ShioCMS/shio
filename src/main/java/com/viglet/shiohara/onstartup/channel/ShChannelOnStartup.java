package com.viglet.shiohara.onstartup.channel;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShChannelOnStartup {

	@Autowired
	private ShChannelRepository shChannelRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;

	public void createDefaultRows() {

		if (shChannelRepository.findAll().isEmpty()) {

			ShSite shSite = shSiteRepository.findById(1);

			// System Channel
			ShChannel shChannelSystem = new ShChannel();
			shChannelSystem.setName("System");
			shChannelSystem.setSummary("System Channel");
			shChannelSystem.setParentChannel(null);
			shChannelSystem.setShSite(shSite);
			shChannelSystem.setDate(new Date());
			shChannelSystem.setRootChannel((byte) 1);

			shChannelRepository.save(shChannelSystem);

			// Home Channel
			ShChannel shChannelHome = new ShChannel();
			shChannelHome.setName("Home");
			shChannelHome.setSummary("Home Channel");
			shChannelHome.setParentChannel(null);
			shChannelHome.setShSite(shSite);
			shChannelHome.setDate(new Date());
			shChannelHome.setRootChannel((byte) 1);

			shChannelRepository.save(shChannelHome);

			// Article Channel
			ShChannel shChannelArticle = new ShChannel();
			shChannelArticle.setName("Article");
			shChannelArticle.setSummary("Article Channel");
			shChannelArticle.setParentChannel(shChannelHome);
			shChannelArticle.setShSite(shSite);
			shChannelArticle.setDate(new Date());
			shChannelArticle.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelArticle);

			// Text Channel
			ShChannel shChannelText = new ShChannel();
			shChannelText.setName("Text");
			shChannelText.setSummary("Text Channel");
			shChannelText.setParentChannel(shChannelHome);
			shChannelText.setShSite(shSite);
			shChannelText.setDate(new Date());
			shChannelText.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelText);

			// News Channel
			ShChannel shChannelNews = new ShChannel();
			shChannelNews.setName("News");
			shChannelNews.setSummary("News Channel");
			shChannelNews.setParentChannel(shChannelArticle);
			shChannelNews.setShSite(shSite);
			shChannelNews.setDate(new Date());
			shChannelNews.setRootChannel((byte) 0);
			
			shChannelRepository.save(shChannelNews);
		}

	}
}
