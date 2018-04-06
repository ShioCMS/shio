package com.viglet.shiohara.api.staticfile;

import java.io.File;
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShStaticFileUtils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/staticfile")
@Api(tags="Static File", description="Static File API")
public class ShStaticFileAPI {

	@Autowired
	private ShFolderRepository shFolderRepository;
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
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

	@PostMapping("/upload")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shStaticFileUpload(@RequestParam("file") MultipartFile file, @RequestParam("folderId") UUID folderId,
			@RequestParam("createPost") boolean createPost) throws URISyntaxException, IOException {

		ShFolder shFolder = shFolderRepository.findById(folderId).get();
		File directoryPath = shStaticFileUtils.dirPath(shFolder);
		ShPost shPost = new ShPost();
		String filePath = null;
		if (directoryPath != null) {
			if (!directoryPath.exists()) {
				directoryPath.mkdirs();
			}

			try {

				filePath = file.getOriginalFilename();

				String destFile = directoryPath.getAbsolutePath().concat("/" + filePath);

				file.transferTo(new File(destFile));

				if (createPost) {
					// Post File
					ShPostType shPostType = shPostTypeRepository.findByName("PT-FILE");

					shPost.setDate(new Date());
					shPost.setShPostType(shPostType);
					shPost.setSummary(null);
					shPost.setTitle(filePath);
					shPost.setShFolder(shFolder);

					shPostRepository.save(shPost);

					ShGlobalId shGlobalId = new ShGlobalId();
					shGlobalId.setShObject(shPost);
					shGlobalId.setType("POST");

					shGlobalIdRepository.saveAndFlush(shGlobalId);

					ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
							"FILE");

					ShPostAttr shPostAttr = new ShPostAttr();
					shPostAttr.setShPost(shPost);
					shPostAttr.setShPostTypeAttr(shPostTypeAttr);
					shPostAttr.setStrValue(shPost.getTitle());
					shPostAttr.setType(1);

					shPostAttrRepository.save(shPostAttr);

				} else {
					shPost.setTitle(filePath);
				}
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return shPost;
	}

}
