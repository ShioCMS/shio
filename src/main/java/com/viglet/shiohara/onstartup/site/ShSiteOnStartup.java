package com.viglet.shiohara.onstartup.site;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShSiteOnStartup {

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

	public void createDefaultRows() {

		if (shSiteRepository.findAll().isEmpty()) {

			ShSite shSite = new ShSite();
			UUID uuid = UUID.fromString("858db46f-4bf9-4caa-be08-359ba3d93be9");
			shSite.setId(uuid);
			shSite.setName("Sample");
			shSite.setDescription("Sample Site");
			shSite.setUrl("http://example.com");
			shSite.setPostTypeLayout("{\"PT-ARTICLE\" :  \"Post Page Layout\", \n"
					+ "\"PT-TEXT\": \"Post Page Layout\",\n" + "\"PT-TEXT-AREA\": \"Post Page Layout\"}");
			shSite.setOwner("admin");
			shSite.setDate(new Date());

			shSiteRepository.saveAndFlush(shSite);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shSite);
			shGlobalId.setType(ShObjectType.SITE);

			shGlobalIdRepository.save(shGlobalId);

		}

	}
}
