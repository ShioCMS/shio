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
package com.viglet.shio.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.viglet.shio.exchange.post.ShPostImport;
import com.viglet.shio.exchange.post.type.ShPostTypeImport;
import com.viglet.shio.exchange.site.ShSiteImport;
import com.viglet.shio.exchange.utils.ShExchangeUtils;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShImportExchange {
	private static final Logger logger = LogManager.getLogger(ShImportExchange.class);
	@Autowired
	private ShSiteImport shSiteImport;
	@Autowired
	private ShPostTypeImport shPostTypeImport;
	@Autowired
	private ShPostImport shPostImport;
	@Autowired
	private ShExchangeUtils shExchangeUtils;
	private Map<String, Object> shObjects = new HashMap<>();
	private Map<String, List<String>> shChildObjects = new HashMap<>();

	public ShExchange importFromMultipartFile(MultipartFile multipartFile, String username) {
		logger.info("Unzip Package");
		ShExchangeFilesDirs shExchangeFilesDirs = this.extractZipFile(multipartFile);

		if (shExchangeFilesDirs.getExportDir() != null) {

			ShExchange shExchange = shExchangeFilesDirs.readExportFile();

			this.importObjects(username, shExchangeFilesDirs.getExportDir(), shExchange);

			shExchangeFilesDirs.deleteExport();
			return shExchange;
		} else {
			return null;
		}
	}

	private void importObjects(String username, File extractFolder, ShExchange shExchange) {
		if (shExchange != null) {
			if (shExchange.getPostTypes() != null && !shExchange.getPostTypes().isEmpty())
				shPostTypeImport.importPostType(shExchange, false);

			if (shExchange.getSites() != null && !shExchange.getSites().isEmpty()) {
				shSiteImport.importSite(shExchange, username, extractFolder, shObjects, shChildObjects);
			} else if (shExchange.getFolders() == null && shExchange.getPosts() != null) {
				File extractFolderInner = extractFolder;
				shExchange.getPosts().forEach(shPostExchange -> shPostImport.createShPost(
						new ShExchangeContext(extractFolderInner, username, false), shPostExchange, shObjects));
			}
		}
	}

	public ShExchange importFromFile(File file, String username) {

		MultipartFile multipartFile = null;
		try {
			FileInputStream input = new FileInputStream(file);
			multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));
		} catch (IOException e) {
			logger.error(e);
		}

		return this.importFromMultipartFile(multipartFile, username);
	}

	public ShExchangeFilesDirs extractZipFile(MultipartFile file) {
		shObjects.clear();
		shChildObjects.clear();
		return shExchangeUtils.extractZipFile(file);
	}
	
	public ShExchangeFilesDirs getExtratedImport(File directory) {
		shObjects.clear();
		shChildObjects.clear();
		return shExchangeUtils.getExtratedImport(directory);
	}
}
