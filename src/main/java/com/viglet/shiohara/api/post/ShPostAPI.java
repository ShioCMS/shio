/*
 * Copyright (C) 2016-2019 the original author or authors. 
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

package com.viglet.shiohara.api.post;

import java.io.File;
import java.nio.file.Files;
import java.security.Principal;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.history.ShHistory;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.history.ShHistoryRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.turing.ShTuringIntegration;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

import io.swagger.annotations.Api;

/**
 * Post API.
 *
 * @author Alexandre Oliveira
 * @since 0.3.0
 */
@RestController
@RequestMapping("/api/v2/post")
@Api(tags = "Post", description = "Post API")
public class ShPostAPI {

	private static final Log logger = LogFactory.getLog(ShPostAPI.class);

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShHistoryRepository shHistoryRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShTuringIntegration shTuringIntegration;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShPost> shPostList() throws Exception {
		return shPostRepository.findAll();
	}

	@GetMapping("/post-type/{postTypeName}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShPost> shPostListByPostType(@PathVariable String postTypeName) throws Exception {
		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		return shPostRepository.findByShPostType(shPostType);
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostEdit(@PathVariable String id) throws Exception {
		Optional<ShPost> shPostOptional = shPostRepository.findById(id);
		if (shPostOptional.isPresent()) {
			ShPost shPost = shPostOptional.get();
			Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();

			// Sync Post Attributes with Post Type
			Map<String, ShPostAttr> shPostAttrMap = new HashMap<String, ShPostAttr>();
			for (ShPostAttr shPostAttr : shPostAttrRepository.findByShPost(shPost))
				shPostAttrMap.put(shPostAttr.getShPostTypeAttr().getId(), shPostAttr);

			Map<String, ShPostTypeAttr> shPostTypeAttrMap = new HashMap<String, ShPostTypeAttr>();
			for (ShPostTypeAttr shPostTypeAttr : shPost.getShPostType().getShPostTypeAttrs())
				shPostTypeAttrMap.put(shPostTypeAttr.getId(), shPostTypeAttr);

			// Add only PostAttr that contains in Post Type
			for (ShPostAttr shPostAttr : shPostAttrMap.values()) {
				String postTypeAttrId = shPostAttr.getShPostTypeAttr().getId();
				if (shPostTypeAttrMap.containsKey(postTypeAttrId)) {
					ShPostAttr shPostAttrSync = new ShPostAttr();
					shPostAttrSync.setShPostTypeAttr(shPostTypeAttrMap.get(postTypeAttrId));
					shPostAttrs.add(shPostAttr);
				}			
			}
			
			// Add new PostAttrs that not contain into Post
			for (ShPostTypeAttr shPostTypeAttr : shPostTypeAttrMap.values()) {
				String postTypeAttrId = shPostTypeAttr.getId();
				if (!shPostAttrMap.containsKey(postTypeAttrId)) {
					ShPostAttr shPostAttrSync = new ShPostAttr();
					shPostAttrSync.setShPostTypeAttr(shPostTypeAttr);
					shPostAttrs.add(shPostAttrSync);
				}
			}

			shPost.setShPostAttrs(shPostAttrs);
			return shPost;
		} else {
			return null;
		}
	}

	@GetMapping("/attr/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPostAttr shPostAttrModel() throws Exception {
		ShPostAttr shPostAttr = new ShPostAttr();
		return shPostAttr;
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostUpdate(@PathVariable String id, @RequestBody ShPost shPost, Principal principal)
			throws Exception {

		this.postSave(shPost);
		// shPostUtils.saveDoc(shPost);

		// History
		ShHistory shHistory = new ShHistory();
		shHistory.setDate(new Date());
		shHistory.setDescription("Updated " + shPost.getTitle() + " Post.");
		if (principal != null) {
			shHistory.setOwner(principal.getName());
		}

		shTuringIntegration.indexObject(shPost);

		shHistory.setShObject(shPost.getId());
		shHistory.setShSite(shPostUtils.getSite(shPost).getId());
		shHistoryRepository.saveAndFlush(shHistory);

		return this.shPostEdit(shPost.getId());

	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostAdd(@RequestBody ShPost shPost, Principal principal) throws Exception {

		this.postSave(shPost);

		shTuringIntegration.indexObject(shPost);

		// History
		ShHistory shHistory = new ShHistory();
		shHistory.setDate(new Date());
		shHistory.setDescription("Created " + shPost.getTitle() + " Post.");
		if (principal != null) {
			shHistory.setOwner(principal.getName());
		}
		shHistory.setShObject(shPost.getId());
		shHistory.setShSite(shPostUtils.getSite(shPost).getId());
		shHistoryRepository.saveAndFlush(shHistory);

		return this.shPostEdit(shPost.getId());

	}

	@Transactional
	@DeleteMapping("/{id}")
	public boolean shPostDelete(@PathVariable String id, Principal principal) throws Exception {

		Optional<ShPost> shPostOptional = shPostRepository.findById(id);

		if (shPostOptional.isPresent()) {
			ShPost shPost = shPostOptional.get();

			shTuringIntegration.deindexObject(shPost);

			Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
			if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE) && shPostAttrs.size() > 0) {
				File file = shStaticFileUtils.filePath(shPost.getShFolder(),
						shPostAttrs.iterator().next().getStrValue());
				if (file != null) {
					if (file.exists()) {
						Files.delete(file.toPath());
					}
				}
			}

			shPostAttrRepository.deleteInBatch(shPostAttrs);

			shReferenceRepository.deleteInBatch(shReferenceRepository.findByShObjectFrom(shPost));

			shReferenceRepository.deleteInBatch(shReferenceRepository.findByShObjectTo(shPost));

			// History
			ShHistory shHistory = new ShHistory();
			shHistory.setDate(new Date());
			shHistory.setDescription("Deleted " + shPost.getTitle() + " Post.");
			if (principal != null) {
				shHistory.setOwner(principal.getName());
			}
			shHistory.setShObject(shPost.getId());
			shHistory.setShSite(shPostUtils.getSite(shPost).getId());
			shHistoryRepository.saveAndFlush(shHistory);

			shPostRepository.delete(id);

			return true;
		} else {
			return false;
		}
	}

	public void postSave(ShPost shPost) {
		String title = shPost.getTitle();
		String summary = shPost.getSummary();
		// Get PostAttrs before save, because JPA Lazy
		Set<ShPostAttr> shPostAttrs = shPost.getShPostAttrs();

		for (ShPostAttr shPostAttr : shPostAttrs) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr != null) {
				shPostAttr.setReferenceObject(null);
			}
		}

		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);
		shPost.setFurl(shURLFormatter.format(title));

		for (ShPostAttr shPostAttr : shPostAttrs) {
			shPostAttr.setShPost(shPost);
			this.updateRelatorParent(shPostAttr, shPost);
		}

		shPostRepository.saveAndFlush(shPost);

		this.postReferenceSave(shPost);

		ShUser shUser = shUserRepository.findByUsername("admin");
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

	}

	private void updateRelatorParent(ShPostAttr shPostAttr, ShPost shPost) {
		for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
				shChildrenPostAttr.setShParentRelatorItem(shRelatorItem);
				this.updateRelatorParent(shChildrenPostAttr, shPost);
			}
		}
	}

	private void updateRelatorInfo(ShPostAttr shPostAttr, ShPost shPost) {
		for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
				ShPostTypeAttr shPostTypeAttr = shChildrenPostAttr.getShPostTypeAttr();
				if (shPostTypeAttr.getIsTitle() == 1) {
					String widgetName = shPostTypeAttr.getShWidget().getName();
					String title = shChildrenPostAttr.getStrValue();
					if (shChildrenPostAttr.getReferenceObject() != null
							&& widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						ShObject shObject = shChildrenPostAttr.getReferenceObject();
						if (shObject != null) {
							if (shObject.getObjectType().equals(ShObjectType.POST)) {
								ShPost shPostReferenced = shPostRepository.findById(shObject.getId()).get();
								title = shPostReferenced.getTitle();
							} else if (shObject.getObjectType().equals(ShObjectType.FOLDER)) {
								ShFolder shFolderReferenced = shFolderRepository.findById(shObject.getId()).get();
								title = shFolderReferenced.getName();
							}
						}

					}
					shRelatorItem.setTitle(StringUtils.abbreviate(title, 255));
				}

				if (shPostTypeAttr.getIsSummary() == 1) {
					String widgetName = shPostTypeAttr.getShWidget().getName();
					String summary = shChildrenPostAttr.getStrValue();
					if (shChildrenPostAttr.getReferenceObject() != null
							&& widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						ShObject shObject = shChildrenPostAttr.getReferenceObject();
						if (shObject != null) {
							if (shObject.getObjectType().equals(ShObjectType.POST)) {
								ShPost shPostReferenced = shPostRepository.findById(shObject.getId()).get();
								summary = shPostReferenced.getTitle();
							} else if (shObject.getObjectType().equals(ShObjectType.FOLDER)) {
								ShFolder shFolderReferenced = shFolderRepository.findById(shObject.getId()).get();
								summary = shFolderReferenced.getName();
							}
						}
					}
					shRelatorItem.setSummary(StringUtils.abbreviate(summary, 255));
				}

				this.updateRelatorInfo(shChildrenPostAttr, shPost);
			}
		}
	}

	private void postReferenceSave(ShPost shPost) {

		// Delete all old references to recreate in next step
		List<ShReference> shOldReferences = shReferenceRepository.findByShObjectFrom(shPost);
		shReferenceRepository.deleteInBatch(shOldReferences);

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostUtils.referencedObject(shPostAttr, shPost);
			this.nestedReferenceSave(shPostAttr, shPost);
		}

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs())
			this.updateRelatorInfo(shPostAttr, shPost);

		shPostRepository.saveAndFlush(shPost);
	}

	private void nestedReferenceSave(ShPostAttr shPostAttr, ShPost shPost) {
		for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
				shPostUtils.referencedObject(shChildrenPostAttr, shPost);
				this.nestedReferenceSave(shChildrenPostAttr, shPost);
			}
		}
	}
}
