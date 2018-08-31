package com.viglet.shiohara.utils;

import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;

@Component
public class ShObjectUtils {
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public boolean isVisiblePage(ShObject shObject) {
		ShFolder shFolder = null;
		if (shObject instanceof ShFolder) {
			shFolder = (ShFolder) shObject;
			ShPost shFolderIndexPost = shFolderUtils.getFolderIndex(shFolder);
			if (shFolderIndexPost != null) {
				Map<String, ShPostAttr> shFolderIndexPostMap = shPostUtils.postToMap(shFolderIndexPost);
				if (shFolderIndexPostMap.get("IS_VISIBLE_PAGE") != null
						&& shFolderIndexPostMap.get("IS_VISIBLE_PAGE").getStrValue().equals("no")) {
					return false;
				}
			} else {
				return false;
			}
		} else if (shObject instanceof ShPost) {
			ShPost shPost = (ShPost) shObject;
			shFolder = shPost.getShFolder();
		}
		if (shFolder != null) {
			List<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
			if (breadcrumb.get(0).getName().equals("Home")) {
				return true;
			} else {
				return false;
			}
		} else {
			return false;
		}

	}

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

	public String generateObjectLink(ShObject shObject) {
		return this.generateObjectLinkById(shObject.getId());
	}

	public ShSite getSite(ShObject shObject) {
		if (shObject instanceof ShPost) {
			return shPostUtils.getSite((ShPost) shObject);
		} else if (shObject instanceof ShFolder) {
			return shFolderUtils.getSite((ShFolder) shObject);
		} else {
			return null;
		}
	}
}
