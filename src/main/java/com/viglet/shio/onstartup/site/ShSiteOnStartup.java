/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.onstartup.site;

import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.viglet.shio.exchange.ShCloneExchange;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShSiteOnStartup {
	private static final Log logger = LogFactory.getLog(ShSiteOnStartup.class);
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShCloneExchange shCloneExchange;
	@Autowired
	private ResourceLoader resourceloader;

	private static final String COULD_NOT_CREATE_SAMPLE_SITE = "Could not create sample site";

	public void createDefaultRows() {

		if (shSiteRepository.findAll().isEmpty()) {
				importSiteFromResource("/import/sample-site.zip", "sample-site-import");
				importSiteFromResource("/import/stock-site.zip", "sample-site-import");
		}
	}

	public void importSiteFromResource(String classpathFile, String slug) {

		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists())
				tmpDir.mkdirs();

			File siteFile = new File(
					tmpDir.getAbsolutePath().concat(File.separator + slug + UUID.randomUUID() + ".zip"));

			try {
				InputStream is = resourceloader.getResource("classpath:" + classpathFile).getInputStream();
				FileUtils.copyInputStreamToFile(is, siteFile);
				shCloneExchange.getTemplateAsCloneFromFile(siteFile, null);
			} catch (IllegalStateException | IOException e) {
				logger.error(COULD_NOT_CREATE_SAMPLE_SITE, e);
			}

			FileUtils.deleteQuietly(siteFile);
		}
	}

	public void importSite(URL siteRepository, String slug) {
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists())
				tmpDir.mkdirs();

			File siteFile = new File(
					tmpDir.getAbsolutePath().concat(File.separator + slug + UUID.randomUUID() + ".zip"));

			try {
				FileUtils.copyURLToFile(siteRepository, siteFile);
				shCloneExchange.getTemplateAsCloneFromFile(siteFile, null);
			} catch (IllegalStateException | IOException e) {
				logger.error(COULD_NOT_CREATE_SAMPLE_SITE, e);
			}

			FileUtils.deleteQuietly(siteFile);
		}
	}

	public void importSiteFromExtractedImport(File directory) {
		var userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			var tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists())
				tmpDir.mkdirs();

			shCloneExchange.cloneFromExtractedImport(directory, null);
		}
	}
}
