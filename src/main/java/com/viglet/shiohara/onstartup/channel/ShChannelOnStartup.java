package com.viglet.shiohara.onstartup.channel;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShChannelOnStartup {

	@Autowired
	private ShChannelRepository shChannelRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	
	public void createDefaultRows() {

		if (shChannelRepository.findAll().isEmpty()) {

			ShSite shSite = shSiteRepository.findByName("Sample");

			// System Channel
			ShChannel shChannelSystem = new ShChannel();
			shChannelSystem.setName("System");
			shChannelSystem.setParentChannel(null);
			shChannelSystem.setShSite(shSite);
			shChannelSystem.setDate(new Date());
			shChannelSystem.setRootChannel((byte) 1);

			shChannelRepository.save(shChannelSystem);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelSystem.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// System > Templates Channel
			ShChannel shChannelSystemTemplates = new ShChannel();
			shChannelSystemTemplates.setName("Templates");
			shChannelSystemTemplates.setParentChannel(shChannelSystem);
			shChannelSystemTemplates.setShSite(shSite);
			shChannelSystemTemplates.setDate(new Date());
			shChannelSystemTemplates.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelSystemTemplates);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelSystemTemplates.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// System > Layouts Channel
			ShChannel shChannelSystemLayouts = new ShChannel();
			shChannelSystemLayouts.setName("Layouts");
			shChannelSystemLayouts.setParentChannel(shChannelSystem);
			shChannelSystemLayouts.setShSite(shSite);
			shChannelSystemLayouts.setDate(new Date());
			shChannelSystemLayouts.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelSystemLayouts);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelSystemLayouts.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// System > Themes Channel
			ShChannel shChannelSystemThemes = new ShChannel();
			shChannelSystemThemes.setName("Themes");
			shChannelSystemThemes.setParentChannel(shChannelSystem);
			shChannelSystemThemes.setShSite(shSite);
			shChannelSystemThemes.setDate(new Date());
			shChannelSystemThemes.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelSystemThemes);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelSystemThemes.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// Home Channel
			ShChannel shChannelHome = new ShChannel();
			shChannelHome.setName("Home");
			shChannelHome.setParentChannel(null);
			shChannelHome.setShSite(shSite);
			shChannelHome.setDate(new Date());
			shChannelHome.setRootChannel((byte) 1);

			shChannelRepository.save(shChannelHome);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelHome.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// Article Channel
			ShChannel shChannelArticle = new ShChannel();
			shChannelArticle.setName("Article");
			shChannelArticle.setParentChannel(shChannelHome);
			shChannelArticle.setShSite(shSite);
			shChannelArticle.setDate(new Date());
			shChannelArticle.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelArticle);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelArticle.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// Text Channel
			ShChannel shChannelText = new ShChannel();
			shChannelText.setName("Text");
			shChannelText.setParentChannel(shChannelHome);
			shChannelText.setShSite(shSite);
			shChannelText.setDate(new Date());
			shChannelText.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelText);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelText.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
			// News Channel
			ShChannel shChannelNews = new ShChannel();
			shChannelNews.setName("News");
			shChannelNews.setParentChannel(shChannelArticle);
			shChannelNews.setShSite(shSite);
			shChannelNews.setDate(new Date());
			shChannelNews.setRootChannel((byte) 0);

			shChannelRepository.save(shChannelNews);

			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shChannelNews.getId());
			shGlobalId.setType("CHANNEL");
			
			shGlobalIdRepository.save(shGlobalId);
			
		}

	}
}
