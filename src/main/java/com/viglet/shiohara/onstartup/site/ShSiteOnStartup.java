package com.viglet.shiohara.onstartup.site;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShImportExchange;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShSiteOnStartup {

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShImportExchange shImportExchange;

	public void createDefaultRows() throws IOException, IllegalStateException, ArchiveException {

		if (shSiteRepository.findAll().isEmpty()) {

			URL sampleSiteRepository = new URL("https://github.com/openshio/sample-site/archive/master.zip");

			File userDir = new File(System.getProperty("user.dir"));
			if (userDir.exists() && userDir.isDirectory()) {
				File tmpDir = new File(
						userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
				if (!tmpDir.exists()) {
					tmpDir.mkdirs();
				}

				File sampleSiteFile = new File(tmpDir.getAbsolutePath().concat(File.separator + "sample-site-" + UUID.randomUUID() + ".zip"));

				FileUtils.copyURLToFile(sampleSiteRepository, sampleSiteFile);

				shImportExchange.importFromFile(sampleSiteFile, "admin");

				FileUtils.deleteQuietly(sampleSiteFile);
			}
		}
	}
}
