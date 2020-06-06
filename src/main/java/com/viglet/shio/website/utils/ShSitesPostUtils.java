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
package com.viglet.shio.sites.utils;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.ShPostDraft;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;
import com.viglet.shio.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shio.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shio.persistence.repository.post.ShPostDraftAttrRepository;
import com.viglet.shio.persistence.repository.post.ShPostDraftRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.post.type.ShSystemPostType;
import com.viglet.shio.property.ShMgmtProperties;
import com.viglet.shio.sites.ShContent;
import com.viglet.shio.utils.ShFolderUtils;
import com.viglet.shio.utils.ShPostUtils;
import com.viglet.shio.utils.ShStaticFileUtils;

/**
 * Site Post Utils.
 *
 * @author Alexandre Oliveira
 * @since 0.3.5
 */
@Component
public class ShSitesPostUtils {
	private static final Log logger = LogFactory.getLog(ShSitesPostUtils.class);

	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShSitesFolderUtils shSitesFolderUtils;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostDraftRepository shPostDraftRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostDraftAttrRepository shPostDraftAttrRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShSitesObjectUtils shSitesObjectUtils;
	@Autowired
	private ShMgmtProperties shMgmtProperties;
	@Autowired
	private ShPostUtils shPostUtils;

	public ShPost getPostByStage(ShPost shPost) {
		if (shPost != null) {
			if (shMgmtProperties.isEnabled()) {
				if (logger.isDebugEnabled())
					logger.debug("mgmt is enabled");
				Optional<ShPostDraft> shPostDraftOptional = shPostDraftRepository.findByIdFull(shPost.getId());
				if (shPostDraftOptional.isPresent()) {
					ShPost shPostDraft = shPostUtils.loadPostDraft(shPostDraftOptional.get());
					if (shPostDraft != null)
						return shPostDraft;
				}
				return shPost;
			} else {
				if (logger.isDebugEnabled())
					logger.debug("mgmt is not enabled: ");
				if (shPost.isPublished()) {
					if (logger.isDebugEnabled())
						logger.debug("is Published ");
					return shPost;
				} else {
					if (logger.isDebugEnabled())
						logger.debug("is not Published ");
				}

			}
		}
		return null;
	}

	public List<ShPost> getPostsByStage(List<ShPost> shPosts) {
		List<ShPost> shSelectedPosts = new ArrayList<>();
		for (ShPost shPost : shPosts) {
			ShPost shSelectedPost = this.getPostByStage(shPost);
			if (shSelectedPost != null)
				shSelectedPosts.add(shSelectedPost);
		}
		return shSelectedPosts;
	}

	public ShPostAttr getPostAttrByStage(ShPostAttr shPostAttr) {
		if (shPostAttr != null) {
			if (shMgmtProperties.isEnabled()) {
				Optional<ShPostDraftAttr> shPostDraftAttrOptional = shPostDraftAttrRepository
						.findById(shPostAttr.getId());
				if (shPostDraftAttrOptional.isPresent()) {
					return shPostUtils.loadPostDraftAttr(shPostDraftAttrOptional.get());
				}
				return shPostAttr;
			} else {
				ShPost shPost = this.getPost(shPostAttr);
				if (shPost != null && shPost.isPublished()) {
					return shPostAttr;
				}
			}

		}
		return null;

	}

	public ShPost getPost(ShPostAttr shPostAttr) {
		if (shPostAttr.getShPost() != null) {
			return shPostAttr.getShPost();
		} else {
			return this.getPostNested(shPostAttr);
		}
	}

	private ShPost getPostNested(ShPostAttr shPostAttr) {
		if (shPostAttr.getShParentRelatorItem() != null
				&& shPostAttr.getShParentRelatorItem().getShParentPostAttr() != null) {
			if (shPostAttr.getShParentRelatorItem().getShParentPostAttr().getShPost() != null)
				return shPostAttr.getShParentRelatorItem().getShParentPostAttr().getShPost();
			else
				return this.getPostNested(shPostAttr.getShParentRelatorItem().getShParentPostAttr());

		}
		return null;
	}

	public Map<String, ShPostAttr> toMap(String postId) {

		return this.postToMap(this.getPostByStage(shPostRepository.findById(postId).get()));
	}

	public JSONObject toJSON(ShPost shPost) {
		JSONObject shPostItemAttrs = new JSONObject();

		JSONObject shPostObject = new JSONObject();
		shPostObject.put("id", shPost.getId());
		shPostObject.put("postTypeName", shPost.getShPostType().getName());
		shPostObject.put("title", shPost.getTitle());
		shPostObject.put("summary", shPost.getSummary());
		shPostObject.put("link", this.generatePostLink(shPost));
		shPostObject.put("parentFolder", shPost.getShFolder().getId());
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getName() != null) {
				shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
			}
		}

		shPostItemAttrs.put("system", shPostObject);

		return shPostItemAttrs;
	}

	public ShContent toSystemMap(ShPost shPost) {
		ShContent shPostItemAttrs = new ShContent();

		Map<String, Object> shPostObject = new HashMap<>();
		shPostObject.put("id", shPost.getId());
		shPostObject.put("postTypeName", shPost.getShPostType().getName());
		shPostObject.put("title", shPost.getTitle());
		shPostObject.put("summary", shPost.getSummary());
		shPostObject.put("link", this.generatePostLink(shPost));
		shPostObject.put("parentFolder", shPost.getShFolder().getId());
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getName() != null) {
				shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
			}
		}

		shPostItemAttrs.put("system", shPostObject);

		return shPostItemAttrs;
	}

	public Map<String, ShPostAttr> postToMap(ShPost shPost) {

		if (shPost != null) {
			Set<ShPostAttr> shPostAttrList = (shPostAttrRepository.findByShPost(shPost));

			Map<String, ShPostAttr> shPostMap = new HashMap<String, ShPostAttr>();
			ShPostAttr shPostAttrId = new ShPostAttr();
			shPostAttrId.setStrValue(shPost.getId().toString());
			ShPostAttr shPostAttrType = new ShPostAttr();
			shPostAttrType.setStrValue(shPost.getShPostType().getName());
			shPostMap.put("__type__", shPostAttrType);
			shPostMap.put("id", shPostAttrId);
			for (ShPostAttr shPostAttr : shPostAttrList) {
				if (shPostAttr != null)
					shPostMap.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr);
			}
			return shPostMap;
		} else {
			return null;
		}

	}

	public List<Map<String, ShPostAttr>> relationToMap(ShPostAttr shPostAttr) {

		if (shPostAttr != null) {
			List<Map<String, ShPostAttr>> relations = new ArrayList<Map<String, ShPostAttr>>();

			Set<ShRelatorItem> shRelatorItems = shPostAttr.getShChildrenRelatorItems();
			List<ShRelatorItem> shRelatorItemsByOrdinal = new ArrayList<ShRelatorItem>();
			shRelatorItemsByOrdinal.addAll(shRelatorItems);

			Collections.sort(shRelatorItemsByOrdinal, new Comparator<ShRelatorItem>() {
				public int compare(ShRelatorItem o1, ShRelatorItem o2) {
					return o1.getOrdinal() - o2.getOrdinal();
				}
			});

			for (ShRelatorItem shRelatorItem : shRelatorItemsByOrdinal) {
				Map<String, ShPostAttr> shRelationMap = new HashMap<String, ShPostAttr>();
				ShPostAttr shPostAttrId = new ShPostAttr();
				shPostAttrId.setStrValue(shRelatorItem.getId());
				shRelationMap.put("id", shPostAttrId);
				for (ShPostAttr shPostAttrRelation : shRelatorItem.getShChildrenPostAttrs()) {
					shRelationMap.put(shPostAttrRelation.getShPostTypeAttr().getName(), shPostAttrRelation);
				}
				relations.add(shRelationMap);
			}

			return relations;
		} else {
			return null;
		}

	}

	public String generatePostLink(ShPost shPost) {
		ShFolder shFolder = shPost.getShFolder();
		String link = null;
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE.toString())) {
			link = shStaticFileUtils.getFileSourceBase() + "/" + shFolderUtils.getSite(shFolder).getName()
					+ shFolderUtils.folderPath(shFolder, false, true) + shPost.getTitle();
		} else if (shSitesObjectUtils.isVisiblePage(shPost)) {
			link = shSitesFolderUtils.generateFolderLink(shFolder);
			link = link + shPost.getFurl();
		}
		return link;
	}

	public String generatePostLinkById(String postID) {
		if (postID != null) {
			try {
				ShPost shPost = shPostRepository.findById(postID).orElse(null);
				return this.generatePostLink(shPost);

			} catch (IllegalArgumentException exception) {
				return null;
			}
		} else {
			return null;
		}
	}

}
