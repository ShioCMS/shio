package com.viglet.shiohara.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;

@Component
public class ShStaticFileUtils {
	@Autowired
	ShFolderRepository shFolderRepository;
	@Autowired
	ShFolderUtils shFolderUtils;
	String fileSourceBase = File.separator + "store" + File.separator +"file_source";

	public File dirPath(ShFolder shFolder) {
		File directoryPath = null;
		ShSite shSite = shFolderUtils.getSite(shFolder);
		String folderPath = shFolderUtils.folderPath(shFolder, File.separator);
		String folderPathFile = fileSourceBase.concat(File.separator + shSite.getName() + folderPath);
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			directoryPath = new File(userDir.getAbsolutePath().concat(folderPathFile));

		}
		return directoryPath;
	}

	public File filePath(ShFolder shFolder, String fileName) {
		File file = null;
		File directoryPath = this.dirPath(shFolder);

		if (directoryPath != null) {
			file = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));

		}
		return file;
	}

	public File filePath(String url) {
		File file = null;
		File userDir = new File(System.getProperty("user.dir"));
		String fileRelativePath = url.replaceAll("/", File.separator);

		if (userDir.exists() && userDir.isDirectory()) {
			String fileSource = userDir.getAbsolutePath().concat(fileSourceBase);
			file = new File(fileSource.concat(fileRelativePath));

		}
		return file;
	}
	

	public File getFileSource() {
		File file = null;
		File userDir = new File(System.getProperty("user.dir"));

		if (userDir.exists() && userDir.isDirectory()) {
			String fileSource = userDir.getAbsolutePath().concat(fileSourceBase);
			file = new File(fileSource);

		}
		return file;
	}
}
