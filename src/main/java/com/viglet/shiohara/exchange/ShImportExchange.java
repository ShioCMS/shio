package com.viglet.shiohara.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

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
import com.viglet.shiohara.utils.ShUtils;

@Component
public class ShImportExchange {

	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShSiteImport shSiteImport;
	@Autowired
	private ShPostTypeImport shPostTypeImport;

	private Map<String, Object> shObjects = new HashMap<String, Object>();
	private Map<String, List<String>> shChildObjects = new HashMap<String, List<String>>();

	public ShExchange importFromMultipartFile(MultipartFile multipartFile, String username)
			throws IllegalStateException, IOException, ArchiveException {
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

			try {
				FileUtils.deleteDirectory(extractFolder);
				if (parentExtractFolder != null) {
					FileUtils.deleteDirectory(parentExtractFolder);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return shExchange;
		} else {
			return null;
		}
	}

	public ShExchange importFromFile(File file, String username)
			throws IOException, IllegalStateException, ArchiveException {

		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));

		return this.importFromMultipartFile(multipartFile, username);
	}

	public File extractZipFile(MultipartFile file) throws IllegalStateException, IOException, ArchiveException {
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
