package com.viglet.shiohara.onstartup.site;

import java.io.File;
import java.io.IOException;

import org.apache.commons.compress.archivers.ArchiveException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShImportExchange;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShSiteOnStartup {

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShImportExchange shImportExchange;

	public void createDefaultRows() {

		if (shSiteRepository.findAll().isEmpty()) {
			Resource resource = new ClassPathResource("/imports" + File.separator + "sample-site.zip");
			try {
				File sampleSite;

				sampleSite = new File(resource.getURI());

				shImportExchange.importFromFile(sampleSite, "admin");
			} catch (ArchiveException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalStateException e1) {
				// TODO Auto-generated catch block
				e1.printStackTrace();
			} catch (IOException e2) {
				// TODO Auto-generated catch block
				e2.printStackTrace();
			}
		}

	}
}
