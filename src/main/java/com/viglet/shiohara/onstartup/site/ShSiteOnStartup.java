package com.viglet.shiohara.onstartup.site;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShSiteOnStartup {

	@Autowired
	ShSiteRepository shSiteRepository;

	public void createDefaultRows() {

		if (shSiteRepository.findAll().isEmpty()) {

			ShSite shSite = new ShSite();
			shSite.setName("Sample");
			shSite.setDescription("Sample Site");
			shSite.setUrl("http://example.com");
					
			shSiteRepository.save(shSite);
			
			shSite = new ShSite();
			shSite.setName("Viglet");
			shSite.setDescription("Viglet Site");
			shSite.setUrl("http://viglet.com");
					
			shSiteRepository.save(shSite);

		}

	}
}
