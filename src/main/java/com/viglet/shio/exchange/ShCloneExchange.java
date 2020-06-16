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
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.core.JsonParseException;
import com.fasterxml.jackson.databind.JsonMappingException;
import com.fasterxml.jackson.databind.ObjectMapper;
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

	private static final String EXPORT_FILE = "export.json";
	private Map<String, Object> shObjects = new HashMap<String, Object>();
	private Map<String, List<String>> shChildObjects = new HashMap<String, List<String>>();

	public ShExchange cloneFromMultipartFile(MultipartFile multipartFile, String username, ShSite shSite)
			throws IllegalStateException, IOException, ArchiveException {
		File extractFolder = shImportExchange.extractZipFile(multipartFile);

		if (extractFolder != null) {
			ShExchange shExchangeModified = null;

			File parentExtractFolder = null;

			// Check if export.json exists, if it is not exist try access a sub directory
			if (!(new File(extractFolder, EXPORT_FILE).exists()) && (extractFolder.listFiles().length == 1))
				for (File fileOrDirectory : extractFolder.listFiles())
					if (fileOrDirectory.isDirectory() && new File(fileOrDirectory, EXPORT_FILE).exists()) {
						parentExtractFolder = extractFolder;
						extractFolder = fileOrDirectory;
					}

			ShExchange shExchange = readExportFile(extractFolder);

			if (shExchange.getPostTypes() != null && !shExchange.getPostTypes().isEmpty())
				shPostTypeImport.importPostType(shExchange, true);

			if (shExchange.getSites() != null && !shExchange.getSites().isEmpty())
				shExchangeModified = shSiteImport.cloneSite(shExchange, username, extractFolder, shObjects,
						shChildObjects, shSite);

			this.deleteTempoaryFile(extractFolder, parentExtractFolder);
			return shExchangeModified;
		} else {
			return null;
		}
	}

	private ShExchange readExportFile(File extractFolder)
			throws IOException, JsonParseException, JsonMappingException, FileNotFoundException {
		ObjectMapper mapper = new ObjectMapper();

		ShExchange shExchange = mapper.readValue(
				new FileInputStream(extractFolder.getAbsolutePath().concat(File.separator + EXPORT_FILE)),
				ShExchange.class);
		return shExchange;
	}

	private void deleteTempoaryFile(File extractFolder, File parentExtractFolder) {
		try {
			FileUtils.deleteDirectory(extractFolder);
			if (parentExtractFolder != null) {
				FileUtils.deleteDirectory(parentExtractFolder);
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

	public ShExchange cloneFromFile(File file, String username, ShSite shSite)
			throws IOException, IllegalStateException, ArchiveException {

		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));

		return this.cloneFromMultipartFile(multipartFile, username, shSite);
	}

}
