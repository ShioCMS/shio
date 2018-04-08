package com.viglet.shiohara.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
public class ShQueryComponent {

	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public List<Map<String, ShPostAttr>> findByFolderName(String folderId, String postTypeName ) {

		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		ShFolder shFolder = shFolderRepository.findById(UUID.fromString(folderId)).get();
		List<ShPost> shPostList = shPostRepository.findByShFolderAndShPostType(shFolder, shPostType);

		List<Map<String, ShPostAttr>> shPosts = new ArrayList<Map<String, ShPostAttr>>();
		for (ShPost shPost : shPostList) {
			Map<String, ShPostAttr> shPostObject = shPostUtils.postToMap(shPost);
			shPosts.add(shPostObject);
		}
		return shPosts;
	}
}
