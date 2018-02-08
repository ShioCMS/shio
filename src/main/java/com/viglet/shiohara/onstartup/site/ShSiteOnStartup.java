package com.viglet.shiohara.onstartup.site;

import java.util.Date;
import java.util.UUID;

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
			UUID uuid = UUID.fromString("858db46f-4bf9-4caa-be08-359ba3d93be9");
			shSite.setId(uuid);
			shSite.setName("Sample");
			shSite.setDescription("Sample Site");
			shSite.setUrl("http://example.com");
			shSite.setDate(new Date());
					
			shSiteRepository.save(shSite);
		}

	}
}
