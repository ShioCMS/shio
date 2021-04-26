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

import com.viglet.shio.exchange.post.type.ShPostTypeImport;
import com.viglet.shio.exchange.site.ShSiteImport;
import com.viglet.shio.persistence.model.site.ShSite;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShCloneExchange {
	private static final Logger logger = LogManager.getLogger(ShCloneExchange.class);
	@Autowired
	private ShSiteImport shSiteImport;
	@Autowired
	private ShPostTypeImport shPostTypeImport;
	@Autowired
	private ShImportExchange shImportExchange;

	private Map<String, Object> shObjects = new HashMap<>();
	private Map<String, List<String>> shChildObjects = new HashMap<>();

	public ShExchange cloneFromMultipartFile(MultipartFile multipartFile, String username, ShSite shSite) {
		ShExchangeFilesDirs shExchangeFilesDirs = shImportExchange.extractZipFile(multipartFile);

		if (shExchangeFilesDirs.getExportDir() != null) {
			ShExchange shExchangeModified = null;

			shExchangeModified = cloneObjects(username, shSite, shExchangeModified, shExchangeFilesDirs);

			shExchangeFilesDirs.deleteExport();

			return shExchangeModified;
		} else {
			return null;
		}
	}

	private ShExchange cloneObjects(String username, ShSite shSite, ShExchange shExchangeModified,
			ShExchangeFilesDirs shExchangeFilesDirs) {

		ShExchange shExchange = shExchangeFilesDirs.readExportFile();
		if (hasPostTypes(shExchange))
			shPostTypeImport.importPostType(shExchange, true);

		if (hasSites(shExchange))
			shExchangeModified = shSiteImport.cloneSite(shExchange, username, shExchangeFilesDirs.getExportDir(),
					shObjects, shChildObjects, shSite);

		return shExchangeModified;
	}

	private boolean hasSites(ShExchange shExchange) {
		return shExchange != null && shExchange.getSites() != null && !shExchange.getSites().isEmpty();
	}

	private boolean hasPostTypes(ShExchange shExchange) {
		return shExchange != null && shExchange.getPostTypes() != null && !shExchange.getPostTypes().isEmpty();
	}

	public ShExchange cloneFromFile(File file, String username, ShSite shSite) {

		MultipartFile multipartFile = null;
		try {
			FileInputStream input = new FileInputStream(file);
			multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));
		} catch (IOException e) {
			logger.error(e);
		}

		return this.cloneFromMultipartFile(multipartFile, username, shSite);
	}

	public ShExchange cloneFromExtractedImport(File directory, String username, ShSite shSite) {

		ShExchangeFilesDirs shExchangeFilesDirs = shImportExchange.getExtratedImport(directory);

		if (shExchangeFilesDirs.getExportDir() != null) {
			ShExchange shExchangeModified = null;

			shExchangeModified = cloneObjects(username, shSite, shExchangeModified, shExchangeFilesDirs);

			shExchangeFilesDirs.deleteExport();

			return shExchangeModified;
		} else {
			return null;
		}
	}
}
