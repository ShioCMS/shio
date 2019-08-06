package com.viglet.shiohara.sites.utils;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
public class ShSitesObjectUtils {
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShSitesFolderUtils shSitesFolderUtils;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	
	public boolean isVisiblePage(ShObject shObject) {
		ShFolder shFolder = null;
		if (shObject instanceof ShFolder) {
			shFolder = (ShFolder) shObject;
			ShPost shFolderIndexPost = shSitesFolderUtils.getFolderIndex(shFolder);
			if (shFolderIndexPost != null) {
				Map<String, ShPostAttr> shFolderIndexPostMap = shSitesPostUtils.postToMap(shFolderIndexPost);
				if (shFolderIndexPostMap.get("IS_VISIBLE_PAGE") != null && shFolderIndexPostMap.get("IS_VISIBLE_PAGE").getStrValue() != null
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
			Optional<ShObject> shObjectOptional = shObjectRepository.findById(objectId);
			if (shObjectOptional.isPresent()) {
				ShObject shObject = shObjectOptional.get();
				if (shObject instanceof ShPost) {
					return shSitesPostUtils.generatePostLink((ShPost) shObject);
				} else if (shObject instanceof ShFolder) {
					return shSitesFolderUtils.generateFolderLink((ShFolder) shObject);
				}
			}

		}
		return null;
	}

	public String generateImageLinkById(String objectId, int scale) {
		if (objectId != null) {
			Optional<ShObject> shObjectOptional = shObjectRepository.findById(objectId);
			if (shObjectOptional.isPresent()) {
				ShObject shObject = shObjectOptional.get();
				if (shObject instanceof ShPost) {
					if (scale == 1) {
						return shSitesPostUtils.generatePostLink((ShPost) shObject);
					} else {
						return shSitesPostUtils.generatePostLink((ShPost) shObject).replaceAll("^/store/file_source",
								String.format("/image/scale/%d", scale));
					}
				} else {
					return null;
				}
			}

		}
		return null;
	}

	public String generateObjectLink(ShObject shObject) {
		return this.generateObjectLinkById(shObject.getId());
	}
}
