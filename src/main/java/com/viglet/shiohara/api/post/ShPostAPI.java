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
package com.viglet.shiohara.api.post;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.CacheEvict;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
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
import com.viglet.shiohara.persistence.model.post.ShPostDraft;
import com.viglet.shiohara.persistence.model.post.ShPostDraftAttr;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItemDraft;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.model.reference.ShReferenceDraft;
import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectPublishStatus;
import com.viglet.shiohara.persistence.model.auth.ShUser;
import com.viglet.shiohara.persistence.model.history.ShHistory;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.auth.ShUserRepository;
import com.viglet.shiohara.persistence.repository.history.ShHistoryRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostDraftAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostDraftRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceDraftRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shiohara.persistence.repository.workflow.ShWorkflowTaskRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.sites.cache.component.ShCacheObject;
import com.viglet.shiohara.turing.ShTuringIntegration;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShObjectUtils;
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
	private ShPostDraftRepository shPostDraftRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostDraftAttrRepository shPostDraftAttrRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShReferenceDraftRepository shReferenceDraftRepository;
	@Autowired
	private ShHistoryRepository shHistoryRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShTuringIntegration shTuringIntegration;
	@Autowired
	private ShCacheObject shCacheObject;
	@Autowired
	private ShWorkflowTaskRepository shWorkflowTaskRepository;
	@Autowired
	private ShObjectUtils shObjectUtils;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShPost> shPostList() {
		return shPostRepository.findAll();
	}

	@GetMapping("/post-type/{postTypeName}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShPost> shPostListByPostType(@PathVariable String postTypeName) {
		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		return shPostRepository.findByShPostType(shPostType);
	}

	/**
	 * Post Edit API
	 * 
	 * @param id Post Id
	 * @param principal Logged User
	 * @return ShPost
	 */
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ResponseEntity<?> shPostEdit(@PathVariable String id, Principal principal) {
		if (shObjectUtils.canAccess(principal, id)) {
			ShPost shPost = shPostUtils.loadLazyPost(id, false);
			shPostUtils.syncWithPostType(shPost);

			return new ResponseEntity<>(shPost, HttpStatus.OK);
		}

		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@GetMapping("/attr/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPostAttr shPostAttrModel() {
		ShPostAttr shPostAttr = new ShPostAttr();
		return shPostAttr;
	}

	@Transactional
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ResponseEntity<?> shPostUpdate(@PathVariable String id, @RequestBody ShPost shPost, Principal principal) {
		if (shObjectUtils.canAccess(principal, id)) {
			shCacheObject.deleteCache(id);

			this.postSave(shPost);

			// History
			ShHistory shHistory = new ShHistory();
			shHistory.setDate(new Date());
			shHistory.setDescription("Updated " + shPost.getTitle() + " Post.");
			if (principal != null) {
				shHistory.setOwner(principal.getName());
			}

			shHistory.setShObject(shPost.getId());
			shHistory.setShSite(shPostUtils.getSite(shPost).getId());
			shHistoryRepository.saveAndFlush(shHistory);

			return this.shPostEdit(shPost.getId(), principal);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

	}

	@Transactional
	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	@CacheEvict(value = { "page", "pageLayout", "region" }, allEntries = true)
	public ResponseEntity<?> shPostAdd(@RequestBody ShPost shPost, Principal principal) {
		if (shObjectUtils.canAccess(principal, shPost.getShFolder().getId())) {
			if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
				shPost.setPublishStatus("PUBLISH");
				shPost.setPublished(true);
			}

			ShObject shObject = shObjectRepository.findById(shPost.getShFolder().getId()).get();
			List<ShObject> shObjects = new ArrayList<>();
			shObjects.add(shObject);

			shPost.setShGroups(new HashSet<String>(shObject.getShGroups()));
			shPost.setShUsers(new HashSet<String>(shObject.getShUsers()));

			this.postSave(shPost);

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

			return this.shPostEdit(shPost.getId(), principal);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@Transactional
	@DeleteMapping("/{id}")
	public ResponseEntity<?> shPostDelete(@PathVariable String id, Principal principal) {
		if (shObjectUtils.canAccess(principal, id)) {
			shCacheObject.deleteCache(id);

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
							try {
								Files.delete(file.toPath());
							} catch (IOException e) {
								// TODO Auto-generated catch block
								e.printStackTrace();
							}
						}
					}
				}

				shPostAttrRepository.deleteInBatch(shPostAttrs);

				shReferenceRepository.deleteInBatch(shReferenceRepository.findByShObjectFrom(shPost));

				shReferenceRepository.deleteInBatch(shReferenceRepository.findByShObjectTo(shPost));

				shReferenceDraftRepository.deleteInBatch(shReferenceDraftRepository.findByShObjectTo(shPost));

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

				return new ResponseEntity<>(true, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
	}

	public void postSave(ShPost shPost) {
		// Get PostAttrs before save, because JPA Lazy
		Set<ShPostAttr> shPostAttrs = shPost.getShPostAttrs();

		StringBuffer title = new StringBuffer();
		StringBuffer summary = new StringBuffer();

		List<ShPostAttr> shPostAttrsByOrdinal = new ArrayList<ShPostAttr>(shPostAttrs);

		Collections.sort(shPostAttrsByOrdinal, new Comparator<ShPostAttr>() {

			public int compare(ShPostAttr o1, ShPostAttr o2) {
				return o1.getShPostTypeAttr().getOrdinal() - o2.getShPostTypeAttr().getOrdinal();
			}
		});
		for (ShPostAttr shPostAttr : shPostAttrsByOrdinal) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1) {
				if (!StringUtils.isEmpty(title.toString()))
					title.append(", ");
				if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.DATE)) {
					SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyyy");
					title.append(dt.format(shPostAttr.getDateValue()));
				} else
					title.append(StringUtils.abbreviate(shPostAttr.getStrValue(), 255));
			}
			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1) {
				if (!StringUtils.isEmpty(summary.toString()))
					summary.append(", ");
				if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.DATE)) {
					SimpleDateFormat dt = new SimpleDateFormat("dd/MM/yyyyy");
					summary.append(dt.format(shPostAttr.getDateValue()));
				} else
					summary.append(StringUtils.abbreviate(shPostAttr.getStrValue(), 255));
			}
			if (shPostAttr != null) {
				shPostAttr.setReferenceObject(null);
			}
		}

		shPost.setDate(new Date());
		shPost.setTitle(title.toString());
		shPost.setSummary(summary.toString());
		if (shPost.getPublicationDate() == null) {
			shPost.setFurl(shURLFormatter.format(title.toString()));
		}
		shPost.setModifiedDate(new Date());

		for (ShPostAttr shPostAttr : shPostAttrs) {
			shPostAttr.setShPost(shPost);
			this.updateRelatorParent(shPostAttr, shPost);
		}

		if (shPost.getPublishStatus() != null) {
			if (shPost.getPublishStatus().equals(ShObjectPublishStatus.PUBLISH))
				this.postPublishSave(shPost);
			else if (shPost.getPublishStatus().equals(ShObjectPublishStatus.UNPUBLISH))
				this.postUnpublishSave(shPost);
			else if (shPost.getPublishStatus().equals(ShObjectPublishStatus.DRAFT))
				this.postDraftSave(shPost);

		} else {
			if (shPost.isPublished())
				this.postDraftSave(shPost);
			else
				this.postUnpublishSave(shPost);

		}

		ShUser shUser = shUserRepository.findByUsername("admin");
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

	}

	public void postPublishSave(ShPost shPost) {
		shPost.setPublicationDate(new Date());
		shPost.setPublished(true);

		shPostRepository.saveAndFlush(shPost);

		this.postReferenceSave(shPost);

		this.postDraftDelete(shPost.getId());

		shWorkflowTaskRepository.deleteInBatch(shWorkflowTaskRepository.findByShObject(shPost));

		shTuringIntegration.indexObject(shPost);

	}

	public void postUnpublishSave(ShPost shPost) {
		shPost.setPublished(false);

		shPostRepository.saveAndFlush(shPost);

		this.postReferenceSave(shPost);

		this.postDraftDelete(shPost.getId());

		shWorkflowTaskRepository.deleteInBatch(shWorkflowTaskRepository.findByShObject(shPost));

		shTuringIntegration.deindexObject(shPost);
	}

	private void postDraftDelete(String id) {
		Optional<ShPostDraft> shPostOptional = shPostDraftRepository.findById(id);

		if (shPostOptional.isPresent()) {
			ShPostDraft shPost = shPostOptional.get();

			Set<ShPostDraftAttr> shPostAttrs = shPostDraftAttrRepository.findByShPost(shPost);

			shPostDraftAttrRepository.deleteInBatch(shPostAttrs);

			shReferenceDraftRepository.deleteInBatch(shReferenceDraftRepository.findByShObjectFrom(shPost));

			shPostDraftRepository.delete(id);
		}
	}

	public void postDraftSave(ShPost shPost) {
		if (shPost.getId() == null) {
			this.postUnpublishSave(shPost);
		} else {
			if (shPost.isPublished()) {
				ShPost shPostEdit = shPostRepository.findById(shPost.getId()).orElse(null);
				if (shPostEdit != null) {
					ObjectMapper mapper = new ObjectMapper();
					try {
						String jsonInString = mapper.writeValueAsString(shPost);
						ShPostDraft shPostDraft = mapper.readValue(jsonInString, ShPostDraft.class);
						Set<ShPostDraftAttr> shPostAttrs = shPostDraft.getShPostAttrs();
						for (ShPostDraftAttr shPostAttr : shPostAttrs) {
							shPostAttr.setShPost(shPostDraft);
							this.updateRelatorParentDraft(shPostAttr, shPostDraft);
						}

						shPostDraftRepository.saveAndFlush(shPostDraft);
						this.postReferenceSaveDraft(shPostDraft);
						shPostEdit.setPublishStatus("DRAFT");
						shPostRepository.saveAndFlush(shPostEdit);
					} catch (JsonProcessingException e) {
						logger.error("postDraftSave JsonProcessingException:", e);
					}
				}
			} else {
				this.postUnpublishSave(shPost);
			}
		}
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

	private void updateRelatorParentDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		for (ShRelatorItemDraft shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			for (ShPostDraftAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
				shChildrenPostAttr.setShParentRelatorItem(shRelatorItem);
				this.updateRelatorParentDraft(shChildrenPostAttr, shPost);
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
			shPostUtils.updateRelatorInfo(shPostAttr, shPost);

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

	private void postReferenceSaveDraft(ShPostDraft shPost) {

		// Delete all old references to recreate in next step
		List<ShReferenceDraft> shOldReferences = shReferenceDraftRepository.findByShObjectFrom(shPost);
		shReferenceDraftRepository.deleteInBatch(shOldReferences);

		for (ShPostDraftAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostUtils.referencedObjectDraft(shPostAttr, shPost);
			this.nestedReferenceSaveDraft(shPostAttr, shPost);
		}

		for (ShPostDraftAttr shPostAttr : shPost.getShPostAttrs())
			shPostUtils.updateRelatorInfoDraft(shPostAttr, shPost);

		shPostDraftRepository.saveAndFlush(shPost);
	}

	private void nestedReferenceSaveDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		for (ShRelatorItemDraft shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			for (ShPostDraftAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
				shPostUtils.referencedObjectDraft(shChildrenPostAttr, shPost);
				this.nestedReferenceSaveDraft(shChildrenPostAttr, shPost);
			}
		}
	}
}
