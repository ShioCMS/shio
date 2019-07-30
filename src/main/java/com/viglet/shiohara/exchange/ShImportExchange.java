/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.exchange;

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
import com.viglet.shiohara.exchange.post.ShPostImport;
import com.viglet.shiohara.exchange.post.type.ShPostTypeImport;
import com.viglet.shiohara.exchange.site.ShSiteImport;
import com.viglet.shiohara.utils.ShUtils;

@Component
public class ShImportExchange {
	static final Logger logger = LogManager.getLogger(ShImportExchange.class.getName());
	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShSiteImport shSiteImport;
	@Autowired
	private ShPostTypeImport shPostTypeImport;
	@Autowired
	private ShPostImport shPostImport;
	
	private Map<String, Object> shObjects = new HashMap<String, Object>();
	private Map<String, List<String>> shChildObjects = new HashMap<String, List<String>>();

	public ShExchange importFromMultipartFile(MultipartFile multipartFile, String username)
			throws IllegalStateException, IOException {
		File extractFolder = this.extractZipFile(multipartFile);
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

			ShExchange shExchange = mapper.readValue(
					new FileInputStream(extractFolder.getAbsolutePath().concat(File.separator + "export.json")),
					ShExchange.class);

			if (shExchange.getPostTypes() != null && shExchange.getPostTypes().size() > 0) {
				shPostTypeImport.importPostType(shExchange);
			}
			if (shExchange.getSites() != null && shExchange.getSites().size() > 0) {
				shSiteImport.importSite(shExchange, username, extractFolder, shObjects, shChildObjects);
			}
			else {
				//Folders depend site.
				if (shExchange.getFolders() == null && shExchange.getPosts() != null) {
					for (ShPostExchange shPostExchange : shExchange.getPosts()) {
						shPostImport.createShPost(shPostExchange, extractFolder, username, shObjects);
					}
				}
			}

			try {
				FileUtils.deleteDirectory(extractFolder);
				if (parentExtractFolder != null) {
					FileUtils.deleteDirectory(parentExtractFolder);
				}
			} catch (IOException e) {
				logger.error("importFromMultipartFileException", e);	
			}
			return shExchange;
		} else {
			return null;
		}
	}

	public ShExchange importFromFile(File file, String username)
			throws IOException, IllegalStateException {

		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));

		return this.importFromMultipartFile(multipartFile, username);
	}

	public File extractZipFile(MultipartFile file) throws IllegalStateException, IOException {
		shObjects.clear();
		shChildObjects.clear();

		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			File zipFile = new File(tmpDir.getAbsolutePath()
					.concat(File.separator + "imp_" + file.getOriginalFilename() + UUID.randomUUID()));

			file.transferTo(zipFile);
			File extractFolder = new File(tmpDir.getAbsolutePath().concat(File.separator + "imp_" + UUID.randomUUID()));
			shUtils.unZipIt(zipFile, extractFolder);
			FileUtils.deleteQuietly(zipFile);
			return extractFolder;
		} else {
			return null;
		}
	}
}
