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
import java.util.UUID;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shio.exchange.post.ShPostImport;
import com.viglet.shio.exchange.post.type.ShPostTypeImport;
import com.viglet.shio.exchange.site.ShSiteImport;
import com.viglet.shio.utils.ShUtils;
import com.viglet.shio.utils.ShUtilsException;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShImportExchange {
	static final Logger logger = LogManager.getLogger(ShImportExchange.class);
	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShSiteImport shSiteImport;
	@Autowired
	private ShPostTypeImport shPostTypeImport;
	@Autowired
	private ShPostImport shPostImport;

	private Map<String, Object> shObjects = new HashMap<>();
	private Map<String, List<String>> shChildObjects = new HashMap<>();

	public ShExchange importFromMultipartFile(MultipartFile multipartFile, String username) {
		logger.info("Unzip Package");
		File extractFolder = null;
		try {
			extractFolder = this.extractZipFile(multipartFile);
		} catch (IllegalStateException e1) {
			logger.error(e1);
		}
		File parentExtractFolder = null;

		if (extractFolder != null) {
			// Check if export.json exists, if it is not exist try access a sub directory
			if (!(new File(extractFolder, "export.json").exists()) && (extractFolder.listFiles().length == 1)) {
				for (File fileOrDirectory : extractFolder.listFiles()) {
					if (fileOrDirectory.isDirectory() && new File(fileOrDirectory, "export.json").exists()) {
						parentExtractFolder = extractFolder;
						extractFolder = fileOrDirectory;
					}
				}
			}
			ObjectMapper mapper = new ObjectMapper();

			ShExchange shExchange = null;
			try {
				shExchange = mapper.readValue(
						new FileInputStream(extractFolder.getAbsolutePath().concat(File.separator + "export.json")),
						ShExchange.class);
			} catch (IOException e1) {
				logger.error(e1);
			}
			if (shExchange != null) {
				if (shExchange.getPostTypes() != null && !shExchange.getPostTypes().isEmpty())
					shPostTypeImport.importPostType(shExchange, false);

				if (shExchange.getSites() != null && !shExchange.getSites().isEmpty()) {
					shSiteImport.importSite(shExchange, username, extractFolder, shObjects, shChildObjects);
				} else if (shExchange.getFolders() == null && shExchange.getPosts() != null) {
					File extractFolderInner = extractFolder;
					shExchange.getPosts().forEach(shPostExchange -> shPostImport.createShPost(shPostExchange,
							extractFolderInner, username, shObjects, false));
				}
			}
			try {
				FileUtils.deleteDirectory(extractFolder);
				if (parentExtractFolder != null)
					FileUtils.deleteDirectory(parentExtractFolder);
			} catch (IOException e) {
				logger.error(e);
			}
			return shExchange;
		} else {
			return null;
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

	public File extractZipFile(MultipartFile file) {
		shObjects.clear();
		shChildObjects.clear();

		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists())
				tmpDir.mkdirs();

			File zipFile = new File(tmpDir.getAbsolutePath()
					.concat(File.separator + "imp_" + file.getOriginalFilename() + UUID.randomUUID()));
			File extractFolder = null;
			try {
				file.transferTo(zipFile);
				extractFolder = new File(tmpDir.getAbsolutePath().concat(File.separator + "imp_" + UUID.randomUUID()));
				shUtils.unZipIt(zipFile, extractFolder);
			} catch (IllegalStateException | IOException | ShUtilsException e) {
				logger.error(e);
			}

			FileUtils.deleteQuietly(zipFile);
			return extractFolder;
		} else {
			return null;
		}
	}
}
