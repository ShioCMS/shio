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
package com.viglet.shio.utils;

import java.io.File;
import java.security.Principal;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.viglet.shio.api.ShJsonView;
import com.viglet.shio.bean.ShPostTinyBean;
import com.viglet.shio.object.ShObjectType;
import com.viglet.shio.persistence.model.auth.ShUser;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.ShPostDraft;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;
import com.viglet.shio.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shio.persistence.model.post.relator.ShRelatorItemDraft;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.model.reference.ShReference;
import com.viglet.shio.persistence.model.reference.ShReferenceDraft;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.auth.ShGroupRepository;
import com.viglet.shio.persistence.repository.auth.ShUserRepository;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;
import com.viglet.shio.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shio.persistence.repository.post.ShPostDraftAttrRepository;
import com.viglet.shio.persistence.repository.post.ShPostDraftRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.post.relator.ShRelatorItemRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.persistence.repository.reference.ShReferenceDraftRepository;
import com.viglet.shio.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shio.post.type.ShSystemPostType;
import com.viglet.shio.widget.ShSystemWidget;

/**
 * Post Utils.
 *
 * @author Alexandre Oliveira
 * @since 0.3.0
 */
@Component
public class ShPostUtils {
	private static final Log logger = LogFactory.getLog(ShPostUtils.class);

	private static final String DATE_FORMAT = "dd/MM/yyyyy";

	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostDraftRepository shPostDraftRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostDraftAttrRepository shPostDraftAttrRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShReferenceDraftRepository shReferenceDraftRepository;
	@Autowired
	private ShRelatorItemRepository shRelatorItemRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShGroupRepository shGroupRepository;

	public ShPost getShPostFromObjectId(String objectId) {

		Optional<ShPost> shPostOpt = shPostRepository.findById(objectId);
		if (shPostOpt.isPresent())
			return shPostOpt.get();
		else
			return null;
	}

	public ShPost copy(ShPost shPost, ShFolder shFolderDest) {

		List<ShPostTinyBean> shPostTinyBeans = shPostRepository.findByShFolderTiny(shFolderDest.getId());

		Set<String> titles = new HashSet<>();
		int lowerPosition = 0;
		for (ShPostTinyBean shPostTinyBean : shPostTinyBeans) {
			titles.add(shPostTinyBean.getTitle());
			if (shPostTinyBean.getPosition() < lowerPosition)
				lowerPosition = shPostTinyBean.getPosition();

		}

		ShPost shPostCopy = new ShPost();
		shPostCopy.setDate(new Date());
		shPostCopy.setShFolder(shFolderDest);
		shPostCopy.setShPostType(shPost.getShPostType());
		shPostCopy.setSummary(shPost.getSummary());
		shPostCopy.setPosition(lowerPosition - 1);

		shPostCopy.setTitle(copyTitle(shPost, titles));

		shPostRepository.save(shPostCopy);

		copyAttributes(shPost, shPostCopy);

		return shPostCopy;
	}

	private void copyAttributes(ShPost shPost, ShPost shPostCopy) {
		Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		for (ShPostAttr shPostAttr : shPostAttrs) {
			ShPostAttr shPostAttrClone = new ShPostAttr();
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1 && shPostAttr.getStrValue() != null) {
				shPostAttrClone.setStrValue(shPostCopy.getTitle());
			} else {
				shPostAttrClone.setStrValue(shPostAttr.getStrValue());
			}
			shPostAttrClone.setDateValue(shPostAttr.getDateValue());
			shPostAttrClone.setIntValue(shPostAttr.getIntValue());
			shPostAttrClone.setArrayValue(shPostAttr.getArrayValue());
			shPostAttrClone.setReferenceObject(shPostAttr.getReferenceObject());
			shPostAttrClone.setShPost(shPostCopy);
			shPostAttrClone.setShPostTypeAttr(shPostAttr.getShPostTypeAttr());
			shPostAttrClone.setType(shPostAttr.getType());
			shPostAttrRepository.save(shPostAttrClone);
		}
	}

	private String copyTitle(ShPost shPost, Set<String> titles) {
		String title = shPost.getTitle();
		if (titles.contains(String.format("Copy of %s", shPost.getTitle()))) {
			int countSameTitle = 0;
			boolean uniqueTitle = false;
			while (!uniqueTitle) {
				title = String.format("Copy (%d) of %s", countSameTitle + 1, shPost.getTitle());
				if (!titles.contains(title) || (countSameTitle > titles.size())) {
					uniqueTitle = true;
				}
				countSameTitle++;
			}
		} else {
			title = String.format("Copy of %s", shPost.getTitle());
		}
		return title;
	}

	public void referencedFile(ShPostAttr shPostAttr, ShPost shPost) {
		if (!shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			this.referencedPost(shPostAttr, shPost);
		}
	}

	@Transactional
	public void referencedPost(ShPostAttr shPostAttr, ShPost shPost) {
		if (shPostAttr.getStrValue() == null) {
			shPostAttr.setReferenceObject(null);
		} else {
			removeOldReference(shPostAttr, shPost);
			createNewReference(shPostAttr, shPost);
		}
	}

	private void createNewReference(ShPostAttr shPostAttr, ShPost shPost) {
		try {
			ShObject shObjectReferenced = shObjectRepository.findById(shPostAttr.getStrValue()).orElse(null);
			// Create new reference
			if (shPost != null && shObjectReferenced != null) {
				ShReference shReference = new ShReference();
				shReference.setShObjectFrom(shPost);
				shReference.setShObjectTo(shObjectReferenced);
				shReferenceRepository.saveAndFlush(shReference);
				shPostAttr.setReferenceObject(shObjectReferenced);
			}
		} catch (IllegalArgumentException iae) {
			// Re-thing about ignore this
			shPostAttr.setReferenceObject(null);
		}
	}

	private void removeOldReference(ShPostAttr shPostAttr, ShPost shPost) {
		// Two or more attributes with FILE Widget and same file, it cannot remove
		// a valid reference
		// Remove old references
		List<ShReference> shOldReferences = shReferenceRepository.findByShObjectFrom(shPost);
		if (!shOldReferences.isEmpty()) {
			for (ShReference shOldReference : shOldReferences) {
				// Find by shPostAttr.getStrValue()
				if (shOldReference.getShObjectTo().getId().equals(shPostAttr.getStrValue())) {
					shReferenceRepository.delete(shOldReference);

				}

				// Find by shPostAttr.getReferenceObject()
				if (shPostAttr.getReferenceObject() != null) {
					ShObject shObject = shPostAttr.getReferenceObject();
					if (shOldReference.getShObjectTo().getId().equals(shObject.getId())) {
						shReferenceRepository.delete(shOldReference);
					}

				}
			}

		}
	}

	public void referencedObject(ShPostAttr shPostAttr, ShPost shPost) {
		if (shPostAttr.getShPostTypeAttr().getShWidget() != null) {
			if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.FILE)) {
				this.referencedFile(shPostAttr, shPost);
			} else if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT)) {
				this.referencedPost(shPostAttr, shPost);
			}
		}
	}

	public void referencedObject(ShPostAttr shPostAttrEdit, ShPostAttr shPostAttr, ShPost shPost) {
		if (shPostAttr.getShPostTypeAttr().getShWidget() != null) {
			if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.FILE)) {
				this.referencedFile(shPostAttrEdit, shPostAttr, shPost);
			} else if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT)) {
				this.referencedPost(shPostAttrEdit, shPostAttr, shPost);
			}
		}
	}

	public void referencedFile(ShPostAttr shPostAttrEdit, ShPostAttr shPostAttr, ShPost shPost) {
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			File fileFrom = shStaticFileUtils.filePath(shPost.getShFolder(), shPostAttrEdit.getStrValue());
			File fileTo = shStaticFileUtils.filePath(shPost.getShFolder(), shPostAttr.getStrValue());
			if (fileFrom != null && fileTo != null && fileFrom.exists() && !fileFrom.renameTo(fileTo)) {
				logger.error(String.format("Can't rename the file %s", fileFrom.getName()));
			}
		} else {
			this.referencedPost(shPostAttrEdit, shPostAttr, shPost);
		}

	}

	public void referencedObjectDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		if (shPostAttr.getShPostTypeAttr().getShWidget() != null) {
			if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.FILE)) {
				this.referencedFileDraft(shPostAttr, shPost);
			} else if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT)) {
				this.referencedPostDraft(shPostAttr, shPost);
			}
		}
	}

	public void referencedFileDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		if (!shPost.getShPostType().getName().equals(ShSystemPostType.FILE)) {
			this.referencedPostDraft(shPostAttr, shPost);
		}
	}

	@Transactional
	public void referencedPostDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		if (shPostAttr.getStrValue() == null) {
			shPostAttr.setReferenceObject(null);
		} else {
			removeOldReferenceDraft(shPostAttr, shPost);
			createNewReferenceDraft(shPostAttr, shPost);
		}
	}

	private void createNewReferenceDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		try {
			ShObject shObjectReferenced = shObjectRepository.findById(shPostAttr.getStrValue()).orElse(null);
			// Create new reference
			if (shPost != null && shObjectReferenced != null) {
				ShReferenceDraft shReference = new ShReferenceDraft();
				shReference.setShObjectFrom(shPost);
				shReference.setShObjectTo(shObjectReferenced);
				shReferenceDraftRepository.saveAndFlush(shReference);
				shPostAttr.setReferenceObject(shObjectReferenced);
			}
		} catch (IllegalArgumentException iae) {
			// Re-thing about ignore this
			shPostAttr.setReferenceObject(null);
		}
	}

	private void removeOldReferenceDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		// Two or more attributes with FILE Widget and same file, it cannot remove
		// a valid reference
		// Remove old references
		List<ShReferenceDraft> shOldReferences = shReferenceDraftRepository.findByShObjectFrom(shPost);
		if (!shOldReferences.isEmpty()) {
			for (ShReferenceDraft shOldReference : shOldReferences) {
				// Find by shPostAttr.getStrValue()
				if (shOldReference.getShObjectTo().getId().equals(shPostAttr.getStrValue())) {
					shReferenceDraftRepository.delete(shOldReference);

				}

				// Find by shPostAttr.getReferenceObject()
				if (shPostAttr.getReferenceObject() != null) {
					ShObject shObject = shPostAttr.getReferenceObject();
					if (shOldReference.getShObjectTo().getId().equals(shObject.getId())) {
						shReferenceDraftRepository.delete(shOldReference);
					}
				}
			}
		}
	}

	@Transactional
	public void referencedPost(ShPostAttr shPostAttrEdit, ShPostAttr shPostAttr, ShPost shPost) {
		if (shPostAttr.getStrValue() == null) {
			shPostAttr.setReferenceObject(null);
		} else {

			removeOldReferenceEdit(shPostAttrEdit, shPost);
			createNewReferenceEdit(shPostAttr, shPost);
		}
	}

	private void createNewReferenceEdit(ShPostAttr shPostAttr, ShPost shPost) {
		try {
			ShObject shObjectReferenced = shObjectRepository.findById(shPostAttr.getStrValue()).orElse(null);

			ShReference shReference = new ShReference();
			shReference.setShObjectFrom(shPost);
			shReference.setShObjectTo(shObjectReferenced);
			shReferenceRepository.saveAndFlush(shReference);
			shPostAttr.setReferenceObject(shObjectReferenced);
		} catch (IllegalArgumentException iae) {
			// Re-thing about ignore this
			shPostAttr.setReferenceObject(null);
		}
	}

	private void removeOldReferenceEdit(ShPostAttr shPostAttrEdit, ShPost shPost) {
		// Two or more attributes with FILE Widget and same file, it cannot remove
		// a valid reference
		// Remove old references
		List<ShReference> shOldReferences = shReferenceRepository.findByShObjectFrom(shPost);
		if (!shOldReferences.isEmpty()) {
			for (ShReference shOldReference : shOldReferences) {
				if (shPostAttrEdit.getReferenceObject() != null) {
					ShObject shObject = shPostAttrEdit.getReferenceObject();
					if (shOldReference.getShObjectTo().getId().equals(shObject.getId())) {
						shReferenceRepository.delete(shOldReference);
						break;
					}

				}
			}
		}
	}

	public ShSite getSite(ShPost shPost) {
		return shFolderUtils.getSite(shPost.getShFolder());
	}

	public void updateRelatorInfo(ShPostAttr shPostAttr, ShPost shPost) {
		for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			StringBuilder title = new StringBuilder();
			StringBuilder summary = new StringBuilder();

			List<ShPostAttr> shPostAttrsByOrdinal = postAttrsSort(shRelatorItem.getShChildrenPostAttrs());
			for (ShPostAttr shChildrenPostAttr : shPostAttrsByOrdinal) {
				ShPostTypeAttr shPostTypeAttr = shChildrenPostAttr.getShPostTypeAttr();
				if (shPostTypeAttr.getIsTitle() == 1) {
					String widgetName = shPostTypeAttr.getShWidget().getName();

					if (shChildrenPostAttr.getReferenceObject() != null
							&& widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						ShObject shObject = shChildrenPostAttr.getReferenceObject();
						if (shObject != null) {
							if (shObject.getObjectType().equals(ShObjectType.POST)) {

								Optional<ShPost> shPostReferenced = shPostRepository.findById(shObject.getId());
								if (shPostReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										title.append(", ");
									title.append(shPostReferenced.get().getTitle());
								}
							} else if (shObject.getObjectType().equals(ShObjectType.FOLDER)) {
								Optional<ShFolder> shFolderReferenced = shFolderRepository.findById(shObject.getId());
								if (shFolderReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										title.append(", ");
									title.append(shFolderReferenced.get().getName());
								}
							}
						}
					} else if (widgetName.equals(ShSystemWidget.DATE)) {
						SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						title.append(dt.format(shChildrenPostAttr.getDateValue()));
					} else {
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						title.append(shChildrenPostAttr.getStrValue());
					}

					String titleStr = title.toString();
					if (StringUtils.isEmpty(titleStr) || titleStr.equals("null")) {
						title = new StringBuilder();
						titleStr = shChildrenPostAttr.getId();
						if (StringUtils.isEmpty(titleStr) || titleStr.equals("null")) {
							titleStr = "Untitled";
						}
					}

					shRelatorItem.setTitle(StringUtils.abbreviate(titleStr, 255));
				}

				if (shPostTypeAttr.getIsSummary() == 1) {
					String widgetName = shPostTypeAttr.getShWidget().getName();
					if (shChildrenPostAttr.getReferenceObject() != null
							&& widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						ShObject shObject = shChildrenPostAttr.getReferenceObject();
						if (shObject != null) {
							if (shObject.getObjectType().equals(ShObjectType.POST)) {
								Optional<ShPost> shPostReferenced = shPostRepository.findById(shObject.getId());
								if (shPostReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										summary.append(", ");
									summary.append(shPostReferenced.get().getTitle());
								}
							} else if (shObject.getObjectType().equals(ShObjectType.FOLDER)) {
								Optional<ShFolder> shFolderReferenced = shFolderRepository.findById(shObject.getId());
								if (shFolderReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										summary.append(", ");
									summary.append(shFolderReferenced.get().getName());
								}
							}
						}
					} else if (widgetName.equals(ShSystemWidget.DATE)) {
						SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
						if (!StringUtils.isEmpty(title.toString()))
							summary.append(", ");
						summary.append(dt.format(shPostAttr.getDateValue()));
					} else {
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						summary.append(shChildrenPostAttr.getStrValue());
					}

					shRelatorItem.setSummary(StringUtils.abbreviate(summary.toString(), 255));
				}

				this.updateRelatorInfo(shChildrenPostAttr, shPost);
			}
		}
	}

	public List<ShPostAttr> postAttrsSort(Set<ShPostAttr> shPostAttrs) {
		List<ShPostAttr> shPostAttrsByOrdinal = new ArrayList<>(shPostAttrs);
		Collections.sort(shPostAttrsByOrdinal, new Comparator<ShPostAttr>() {

			public int compare(ShPostAttr o1, ShPostAttr o2) {
				return o1.getShPostTypeAttr().getOrdinal() - o2.getShPostTypeAttr().getOrdinal();
			}
		});
		return shPostAttrsByOrdinal;
	}

	public void updateRelatorInfoDraft(ShPostDraftAttr shPostAttr, ShPostDraft shPost) {
		for (ShRelatorItemDraft shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			StringBuilder title = new StringBuilder();
			StringBuilder summary = new StringBuilder();

			List<ShPostDraftAttr> shPostAttrsByOrdinal = new ArrayList<>(shRelatorItem.getShChildrenPostAttrs());
			Collections.sort(shPostAttrsByOrdinal, new Comparator<ShPostDraftAttr>() {

				public int compare(ShPostDraftAttr o1, ShPostDraftAttr o2) {
					return o1.getShPostTypeAttr().getOrdinal() - o2.getShPostTypeAttr().getOrdinal();
				}
			});
			for (ShPostDraftAttr shChildrenPostAttr : shPostAttrsByOrdinal) {
				ShPostTypeAttr shPostTypeAttr = shChildrenPostAttr.getShPostTypeAttr();
				if (shPostTypeAttr.getIsTitle() == 1) {
					String widgetName = shPostTypeAttr.getShWidget().getName();

					if (shChildrenPostAttr.getReferenceObject() != null
							&& widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						ShObject shObject = shChildrenPostAttr.getReferenceObject();
						if (shObject != null) {
							if (shObject.getObjectType().equals(ShObjectType.POST)) {

								Optional<ShPost> shPostReferenced = shPostRepository.findById(shObject.getId());
								if (shPostReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										title.append(", ");
									title.append(shPostReferenced.get().getTitle());
								}
							} else if (shObject.getObjectType().equals(ShObjectType.FOLDER)) {
								Optional<ShFolder> shFolderReferenced = shFolderRepository.findById(shObject.getId());
								if (shFolderReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										title.append(", ");
									title.append(shFolderReferenced.get().getName());
								}
							}
						}
					} else if (widgetName.equals(ShSystemWidget.DATE)) {
						SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						title.append(dt.format(shChildrenPostAttr.getDateValue()));
					} else {
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						title.append(shChildrenPostAttr.getStrValue());
					}

					String titleStr = title.toString();
					if (StringUtils.isEmpty(titleStr) || titleStr.equals("null")) {
						title = new StringBuilder();
						titleStr = shChildrenPostAttr.getId();
						if (StringUtils.isEmpty(titleStr) || titleStr.equals("null")) {
							titleStr = "Untitled";
						}
					}

					shRelatorItem.setTitle(StringUtils.abbreviate(titleStr, 255));
				}

				if (shPostTypeAttr.getIsSummary() == 1) {
					String widgetName = shPostTypeAttr.getShWidget().getName();
					if (shChildrenPostAttr.getReferenceObject() != null
							&& widgetName.equals(ShSystemWidget.CONTENT_SELECT)
							|| widgetName.equals(ShSystemWidget.FILE)) {
						ShObject shObject = shChildrenPostAttr.getReferenceObject();
						if (shObject != null) {
							if (shObject.getObjectType().equals(ShObjectType.POST)) {
								Optional<ShPost> shPostReferenced = shPostRepository.findById(shObject.getId());
								if (shPostReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										title.append(", ");
									summary.append(shPostReferenced.get().getTitle());
								}
							} else if (shObject.getObjectType().equals(ShObjectType.FOLDER)) {
								Optional<ShFolder> shFolderReferenced = shFolderRepository.findById(shObject.getId());
								if (shFolderReferenced.isPresent()) {
									if (!StringUtils.isEmpty(title.toString()))
										title.append(", ");
									summary.append(shFolderReferenced.get().getName());
								}
							}
						}
					} else if (widgetName.equals(ShSystemWidget.DATE)) {
						SimpleDateFormat dt = new SimpleDateFormat(DATE_FORMAT);
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						summary.append(dt.format(shPostAttr.getDateValue()));
					} else {
						if (!StringUtils.isEmpty(title.toString()))
							title.append(", ");
						summary.append(shChildrenPostAttr.getStrValue());
					}
					shRelatorItem.setSummary(StringUtils.abbreviate(summary.toString(), 255));
				}

				this.updateRelatorInfoDraft(shChildrenPostAttr, shPost);
			}
		}
	}

	public ShPost loadLazyPost(String id, boolean getPostPublished) {
		Optional<ShPost> shPostOptional = shPostRepository.findByIdFull(id);
		Optional<ShPostDraft> shPostDraftOptional = shPostDraftRepository.findByIdFull(id);
		if (!shPostOptional.isPresent())
			return null;

		ShPost shPost = shPostOptional.get();

		if (!getPostPublished && shPost.isPublished() && shPostDraftOptional.isPresent()) {
			ShPost shPostDraft = loadPostDraft(shPostDraftOptional.get());
			if (shPostDraft != null) {
				shPost = shPostDraft;
			}
		} else {
			logger.debug("Get Publish Content");
			shPost.setShPostAttrs(shPostAttrRepository.findByShPostAll(shPost));
			this.loadPostAttribs(shPost.getShPostAttrs());

		}

		return shPost;
	}

	public ShPost loadPostDraft(ShPostDraft shPostDraft) {
		logger.debug("Get Draft");
		ShPost shPost = null;
		if (shPostDraft != null) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				shPostDraft.setShPostAttrs(shPostDraftAttrRepository.findByShPostAll(shPostDraft));
				String jsonInString = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
						.writerWithView(ShJsonView.ShJsonViewObject.class).writeValueAsString(shPostDraft);
				shPost = mapper.readValue(jsonInString, ShPost.class);
				this.loadPostDraftAttribs(shPost.getShPostAttrs());

			} catch (JsonProcessingException e) {
				logger.error("loadPostDraft", e);
			}
		}
		return shPost;
	}

	public ShPostAttr loadPostDraftAttr(ShPostDraftAttr shPostDraftAttr) {
		logger.debug("Get Draft");
		ShPostAttr shPostAttr = null;
		if (shPostDraftAttr != null) {

			ObjectMapper mapper = new ObjectMapper();
			try {
				String jsonInString = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
						.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
						.writerWithView(ShJsonView.ShJsonViewObject.class).writeValueAsString(shPostDraftAttr);
				shPostAttr = mapper.readValue(jsonInString, ShPostAttr.class);

			} catch (JsonProcessingException e) {
				logger.error("loadPostDraftAttr", e);
			}
		}
		return shPostAttr;
	}

	private void loadPostAttribs(Set<ShPostAttr> shPostAttrs) {
		for (ShPostAttr shPostAttr : shPostAttrs) {
			shPostAttr.setShChildrenRelatorItems(shRelatorItemRepository.findByShParentPostAttr(shPostAttr));
			shPostAttr.getShPostTypeAttr().setShPostTypeAttrs(
					shPostTypeAttrRepository.findByShParentPostTypeAttr(shPostAttr.getShPostTypeAttr()));
			if (shPostAttr.getReferenceObject() != null) {
				if (shPostAttr.getReferenceObject() instanceof ShPost) {
					shPostAttr.setReferenceObject(
							shPostRepository.findByIdFull(shPostAttr.getReferenceObject().getId()).orElse(null));
				} else if (shPostAttr.getReferenceObject() instanceof ShFolder) {
					shPostAttr.setReferenceObject(
							shFolderRepository.findById(shPostAttr.getReferenceObject().getId()).orElse(null));
				}
			}
			if (!shPostAttr.getShChildrenRelatorItems().isEmpty()) {
				shPostAttr.getShChildrenRelatorItems().forEach(shRelatorItem -> {
					shRelatorItem
							.setShChildrenPostAttrs(shPostAttrRepository.findByShParentRelatorItemJoin(shRelatorItem));
					this.loadPostAttribs(shRelatorItem.getShChildrenPostAttrs());
				});

			}
		}
	}

	private void loadPostDraftAttribs(Set<ShPostAttr> shPostAttrs) {
		for (ShPostAttr shPostAttr : shPostAttrs) {
			if (shPostAttr.getReferenceObject() != null) {
				if (shPostAttr.getReferenceObject().getObjectType().equals(ShObjectType.POST)) {
					shPostAttr.setReferenceObject(
							shPostRepository.findByIdFull(shPostAttr.getReferenceObject().getId()).orElse(null));
				} else if (shPostAttr.getReferenceObject().getObjectType().equals(ShObjectType.FOLDER)) {
					shPostAttr.setReferenceObject(
							shFolderRepository.findById(shPostAttr.getReferenceObject().getId()).orElse(null));
				}
			}
			if (!shPostAttr.getShChildrenRelatorItems().isEmpty()) {
				shPostAttr.getShChildrenRelatorItems()
						.forEach(shRelatorItem -> this.loadPostDraftAttribs(shRelatorItem.getShChildrenPostAttrs()));

			}
		}
	}

	/**
	 * Sync with Post Type
	 * 
	 * @param shPost Post Object
	 */
	public void syncWithPostType(ShPost shPost) {
		if (shPost != null) {
			Set<ShPostAttr> shPostAttrs = new HashSet<>();

			Map<String, ShPostAttr> shPostAttrMap = this.postAttrMap(shPost);
			Map<String, ShPostTypeAttr> shPostTypeAttrMap = this.postTypeAttrMap(shPost);

			this.postAttrInPostType(shPostAttrs, shPostAttrMap, shPostTypeAttrMap);
			this.postAttrNotInPostType(shPostAttrs, shPostAttrMap, shPostTypeAttrMap);

			shPost.setShPostAttrs(shPostAttrs);
		}
	}

	/**
	 * Convert ShPostTypeAttr List in Map
	 * 
	 * @param shPost Post Object
	 * @return
	 */
	private Map<String, ShPostTypeAttr> postTypeAttrMap(ShPost shPost) {
		Map<String, ShPostTypeAttr> shPostTypeAttrMap = new HashMap<>();
		if (shPost != null) {
			ShPostType shPostType = shPostTypeRepository.findByName(shPost.getShPostType().getName());
			shPostType.getShPostTypeAttrs()
					.forEach(shPostTypeAttr -> shPostTypeAttrMap.put(shPostTypeAttr.getId(), shPostTypeAttr));
		}
		return shPostTypeAttrMap;
	}

	private Map<String, ShPostTypeAttr> postTypeAttrMap(ShPostTypeAttr shPostTypeAttr) {
		Map<String, ShPostTypeAttr> shPostTypeAttrMap = new HashMap<>();
		Set<ShPostTypeAttr> shPostTypeAttrs = shPostTypeAttrRepository.findByShParentPostTypeAttr(shPostTypeAttr);
		if (shPostTypeAttrs != null) {
			shPostTypeAttrs.forEach(
					shPostTypeAttrChild -> shPostTypeAttrMap.put(shPostTypeAttrChild.getId(), shPostTypeAttrChild));
		}
		return shPostTypeAttrMap;
	}

	/**
	 * Convert ShPostAttr List in Map
	 * 
	 * @param shPost Post Object
	 * @return Post Attribute Map
	 */
	private Map<String, ShPostAttr> postAttrMap(ShPost shPost) {

		Map<String, ShPostAttr> shPostAttrMap = new HashMap<>();
		if (shPost != null) {
			shPost.getShPostAttrs()
					.forEach(shPostAttr -> shPostAttrMap.put(shPostAttr.getShPostTypeAttr().getId(), shPostAttr));

		}
		return shPostAttrMap;
	}

	private Map<String, ShPostAttr> postAttrMap(ShRelatorItem shRelatorItem) {

		Map<String, ShPostAttr> shPostAttrMap = new HashMap<>();
		if (shRelatorItem.getShChildrenPostAttrs() != null) {
			shRelatorItem.getShChildrenPostAttrs()
					.forEach(shPostAttr -> shPostAttrMap.put(shPostAttr.getShPostTypeAttr().getId(), shPostAttr));
		}
		return shPostAttrMap;
	}

	/**
	 * Add new PostAttrs that not contain into Post
	 * 
	 * @param shPostAttrs       List of PostAttr Object
	 * @param shPostAttrMap     PostAttrMap Object
	 * @param shPostTypeAttrMap PostTypeAttrMap Object
	 */
	private void postAttrNotInPostType(Set<ShPostAttr> shPostAttrs, Map<String, ShPostAttr> shPostAttrMap,
			Map<String, ShPostTypeAttr> shPostTypeAttrMap) {
		for (ShPostTypeAttr shPostTypeAttr : shPostTypeAttrMap.values()) {
			String postTypeAttrId = shPostTypeAttr.getId();
			if (!shPostAttrMap.containsKey(postTypeAttrId)) {
				ShPostAttr shPostAttrSync = new ShPostAttr();
				shPostAttrSync.setShPostTypeAttr(shPostTypeAttr);
				shPostAttrs.add(shPostAttrSync);

			}
		}

		shPostAttrs.forEach(this::postAttrNotInPostTypeNested);
	}

	private void postAttrNotInPostTypeNested(ShPostAttr shPostAttr) {
		if (shPostAttr.getShChildrenRelatorItems() != null) {
			for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
				if (shRelatorItem.getShChildrenPostAttrs() != null) {
					Map<String, ShPostTypeAttr> shPostTypeAttrChildMap = this
							.postTypeAttrMap(shPostAttr.getShPostTypeAttr());
					Map<String, ShPostAttr> shPostAttrChildMap = this.postAttrMap(shRelatorItem);

					for (ShPostTypeAttr shPostTypeAttr : shPostTypeAttrChildMap.values()) {
						String postTypeAttrId = shPostTypeAttr.getId();
						if (!shPostAttrChildMap.containsKey(postTypeAttrId)) {
							ShPostAttr shPostAttrSync = new ShPostAttr();
							shPostAttrSync.setShPostTypeAttr(shPostTypeAttr);
							shRelatorItem.getShChildrenPostAttrs().add(shPostAttrSync);
						}
					}

					shRelatorItem.getShChildrenPostAttrs()
							.forEach(this::postAttrNotInPostTypeNested);

				}
			}
		}
	}

	/**
	 * Add only PostAttr that contains in Post Type
	 *
	 * @param shPostAttrs       List of PostAttr Object
	 * @param shPostAttrMap     PostAttrMap Object
	 * @param shPostTypeAttrMap PostTypeAttrMap Object
	 */
	private void postAttrInPostType(Set<ShPostAttr> shPostAttrs, Map<String, ShPostAttr> shPostAttrMap,
			Map<String, ShPostTypeAttr> shPostTypeAttrMap) {
		for (ShPostAttr shPostAttr : shPostAttrMap.values()) {
			String postTypeAttrId = shPostAttr.getShPostTypeAttr().getId();
			if (shPostTypeAttrMap.containsKey(postTypeAttrId)) {
				shPostAttrs.add(shPostAttr);
			}
			this.postAttrInPostTypeNested(shPostAttr);
		}
	}

	private void postAttrInPostTypeNested(ShPostAttr shPostAttr) {
		if (shPostAttr.getShChildrenRelatorItems() != null) {
			for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
				Set<ShPostAttr> shChildrenPostAttrs = new HashSet<>();
				if (shRelatorItem.getShChildrenPostAttrs() != null) {
					Map<String, ShPostTypeAttr> shPostTypeAttrChildMap = this
							.postTypeAttrMap(shPostAttr.getShPostTypeAttr());
					for (ShPostAttr shPostAttrRelator : shRelatorItem.getShChildrenPostAttrs()) {
						String postTypeAttrRelatorId = shPostAttrRelator.getShPostTypeAttr().getId();
						if (shPostTypeAttrChildMap.containsKey(postTypeAttrRelatorId)) {
							shChildrenPostAttrs.add(shPostAttrRelator);
							this.postAttrInPostTypeNested(shPostAttrRelator);
						}
					}
				}
				shRelatorItem.setShChildrenPostAttrs(shChildrenPostAttrs);
			}
		}
	}

	public boolean allowPublish(ShPostType shPostType, Principal principal) {
		if (principal == null || (shPostType != null && StringUtils.isEmpty(shPostType.getWorkflowPublishEntity()))) {
			return true;
		} else {
			ShUser shUser = shUserRepository.findByUsername(principal.getName());
			List<ShUser> shUsers = new ArrayList<>();
			shUsers.add(shUser);

			return isPublished(shPostType, shUsers);
		}

	}

	private boolean isPublished(ShPostType shPostType, List<ShUser> shUsers) {
		return shPostType != null && !StringUtils.isEmpty(shPostType.getWorkflowPublishEntity())
				&& shGroupRepository.countByNameAndShUsersIn(shPostType.getWorkflowPublishEntity(), shUsers) > 0;
	}

}
