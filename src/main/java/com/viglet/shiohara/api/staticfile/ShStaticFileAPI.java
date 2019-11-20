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

package com.viglet.shiohara.api.staticfile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FilenameUtils;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.utils.ShStaticFileUtils;

import io.swagger.annotations.Api;
import net.coobird.thumbnailator.Thumbnails;

@RestController
@RequestMapping("/api/v2/staticfile")
@Api(tags = "Static File", description = "Static File API")
public class ShStaticFileAPI {
	static final Logger logger = LogManager.getLogger(ShStaticFileAPI.class.getName());
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ResourceLoader resourceloader;
	
	@PostMapping("/upload")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shStaticFileUpload(@RequestParam("file") MultipartFile file,
			@RequestParam("folderId") String folderId, @RequestParam("createPost") boolean createPost)
			throws URISyntaxException, IOException {

		ShFolder shFolder = shFolderRepository.findById(folderId).orElse(null);
		
		return shStaticFileUtils.createFilePost(file, file.getOriginalFilename(), shFolder, createPost);
	}

	@RequestMapping("/{id}/thumbnail")
	public void resize(HttpServletRequest request, HttpServletResponse response, @PathVariable String id) {
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
		response.setContentType(MediaType.IMAGE_PNG_VALUE);
		if (shObject instanceof ShSite) {
			//
		} else if (shObject instanceof ShPost) {
			List<String> extensions = new ArrayList<>();
			extensions.add("png");
			extensions.add("jpg");
			extensions.add("jpeg");
			extensions.add("gif");
			File filePath = shStaticFileUtils.filePath((ShPost) shObject);
			String extension = FilenameUtils.getExtension(filePath.getAbsolutePath());

			if (extensions.contains(extension.toLowerCase())) {
				try {
					Thumbnails.of(shStaticFileUtils.filePath((ShPost) shObject)).size(230, 230).outputFormat("png")
							.outputQuality(1).toOutputStream(response.getOutputStream());
				} catch (IOException e) {

					logger.error("Image Resize", e);
				}
			} else {
				try {
					Thumbnails.of(resourceloader.getResource("classpath:/ui/public/img/file.png").getInputStream()).scale(1)
							.outputFormat("png").outputQuality(1)
							.toOutputStream(response.getOutputStream());
				} catch (IOException e) {
					logger.error("No Image Resize", e);
				}

			}
		} else if (shObject instanceof ShFolder) {
			//
		}

	}
}
