/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.utils;

import java.io.File;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.post.type.ShSystemPostType;

@Component
public class ShStaticFileUtils {
	@Autowired
	private ShFolderUtils shFolderUtils;

	private String fileSourceBase = File.separator + "store" + File.separator + "file_source";

	public File dirPath(ShFolder shFolder) {
		File directoryPath = null;
		ShSite shSite = shFolderUtils.getSite(shFolder);
		String folderPath = shFolderUtils.directoryPath(shFolder, File.separator);
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

	public File filePath(ShPost shPost) {
		File file = null;
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			File directoryPath = this.dirPath(shPost.getShFolder());

			if (directoryPath != null) {
				file = new File(directoryPath.getAbsolutePath().concat(File.separator + shPost.getTitle()));

			}
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

	public String getFileSourceBase() {
		return fileSourceBase;
	}
}
