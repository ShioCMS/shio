package com.viglet.shiohara.utils;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;

@Component
public class ShObjectUtils {
	@Autowired
	private ShFolderUtils shFolderUtils;	
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public String generateObjectLinkById(String objectId) {
		if (objectId != null) {
			try {

				ShObject shObject = shObjectRepository.findById(objectId).get();
				if (shObject instanceof ShPost) {
					return shPostUtils.generatePostLink((ShPost) shObject);
				} else if (shObject instanceof ShFolder) {
					return shFolderUtils.generateFolderLink((ShFolder) shObject);
				}

			} catch (IllegalArgumentException exception) {
				return null;
			}
		}
		return null;
	}
}
