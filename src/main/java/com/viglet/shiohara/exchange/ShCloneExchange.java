package com.viglet.shiohara.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.exchange.post.type.ShPostTypeImport;
import com.viglet.shiohara.exchange.site.ShSiteImport;
import com.viglet.shiohara.persistence.model.site.ShSite;

@Component
public class ShCloneExchange {

	@Autowired
	private ShSiteImport shSiteImport;
	@Autowired
	private ShPostTypeImport shPostTypeImport;
	@Autowired
	private ShImportExchange shImportExchange;
	
	private Map<String, Object> shObjects = new HashMap<String, Object>();
	private Map<String, List<String>> shChildObjects = new HashMap<String, List<String>>();

	public ShExchange cloneFromMultipartFile(MultipartFile multipartFile, String username, ShSite shSite)
			throws IllegalStateException, IOException, ArchiveException {
		File extractFolder = shImportExchange.extractZipFile(multipartFile);
		File parentExtractFolder = null;
		ShExchange shExchangeModified = null;
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
				shExchangeModified = shSiteImport.cloneSite(shExchange, username, extractFolder, shObjects, shChildObjects, shSite);
			}

			try {
				FileUtils.deleteDirectory(extractFolder);
				if (parentExtractFolder != null) {
					FileUtils.deleteDirectory(parentExtractFolder);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return shExchangeModified;
		} else {
			return null;
		}
	}

	public ShExchange cloneFromFile(File file, String username, ShSite shSite)
			throws IOException, IllegalStateException, ArchiveException {

		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));

		return this.cloneFromMultipartFile(multipartFile, username, shSite);
	}

}
