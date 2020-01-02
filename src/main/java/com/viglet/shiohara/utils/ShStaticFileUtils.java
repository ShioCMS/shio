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
package com.viglet.shiohara.utils;

import java.io.File;
import java.io.IOException;
import java.util.Date;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShStaticFileUtils {
	private static final Log logger = LogFactory.getLog(ShStaticFileUtils.class);
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;

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

	public ShPost createFilePost(MultipartFile file, String fileName, ShFolder shFolder, boolean createPost) {
		File directoryPath = shStaticFileUtils.dirPath(shFolder);
		ShPost shPost = new ShPost();
		if (directoryPath != null) {
			if (!directoryPath.exists()) {
				directoryPath.mkdirs();
			}

			try {
				
				String destFile = directoryPath.getAbsolutePath().concat("/" + fileName);

				file.transferTo(new File(destFile));

				if (createPost) {
					// Post File
					ShPostType shPostType = shPostTypeRepository.findByName(ShSystemPostType.FILE);

					shPost.setDate(new Date());
					shPost.setShPostType(shPostType);
					shPost.setSummary(null);
					shPost.setTitle(fileName);
					shPost.setShFolder(shFolder);
					shPost.setPublished(true);
					shPostRepository.save(shPost);

					ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
							ShSystemPostTypeAttr.FILE);

					ShPostAttr shPostAttr = new ShPostAttr();
					shPostAttr.setShPost(shPost);
					shPostAttr.setShPostTypeAttr(shPostTypeAttr);
					shPostAttr.setStrValue(shPost.getTitle());
					shPostAttr.setType(1);

					shPostAttrRepository.save(shPostAttr);

				} else {
					shPost.setTitle(fileName);
				}
			} catch (IOException e) {
				logger.error("shStaticFileUploadException", e);
			}
		}
		return shPost;
	}
}
