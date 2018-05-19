package com.viglet.shiohara.api.post;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.apache.commons.lang3.SerializationUtils;
import org.apache.commons.lang3.StringUtils;
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
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.history.ShHistory;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.history.ShHistoryRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.relator.ShRelatorItemRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/post")
@Api(tags = "Post", description = "Post API")
public class ShPostAPI {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShHistoryRepository shHistoryRepository;
	@Autowired
	private ShRelatorItemRepository shRelatorItemRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShPostUtils shPostUtils;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShPost> shPostList() throws Exception {
		return shPostRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostEdit(@PathVariable UUID id) throws Exception {
		ShPost shPost = shPostRepository.findById(id).get();
		List<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		shPost.setShPostAttrs(shPostAttrs);

		return shPost;
	}

	@GetMapping("/attr/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPostAttr shPostModel() throws Exception {
		ShPostAttr shPostAttr = new ShPostAttr();
		return shPostAttr;
	}

	
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostUpdate(@PathVariable UUID id, @RequestBody ShPost shPost, Principal principal)
			throws Exception {

		ShPost shPostEdit = shPostRepository.findById(id).get();

		String title = shPostEdit.getTitle();
		String summary = shPostEdit.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			this.postAttrUpdate(shPostAttr, shPostEdit);

		}
		
		shPostEdit.setDate(new Date());
		shPostEdit.setTitle(title);
		shPostEdit.setSummary(summary);
		shPostEdit.setFurl(shURLFormatter.format(title));
		shPostRepository.saveAndFlush(shPostEdit);

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPostEdit.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		// History
		ShHistory shHistory = new ShHistory();
		shHistory.setDate(new Date());
		shHistory.setDescription("Updated " + shPostEdit.getTitle() + " Post.");
		if (principal != null) {
			shHistory.setOwner(principal.getName());
		}
		shHistory.setShObject(shPostEdit);
		shHistory.setShSite(shPostUtils.getSite(shPostEdit));
		shHistoryRepository.saveAndFlush(shHistory);

		return this.shPostEdit(shPostEdit.getId());
	}

	@Transactional
	@DeleteMapping("/{id}")
	public boolean shPostDelete(@PathVariable UUID id, Principal principal) throws Exception {

		ShPost shPost = shPostRepository.findById(id).get();
		List<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE) && shPostAttrs.size() > 0) {
			File file = shStaticFileUtils.filePath(shPost.getShFolder(), shPostAttrs.get(0).getStrValue());
			if (file != null) {
				if (file.exists()) {
					file.delete();
				}
			}
		}

		shPostAttrRepository.deleteInBatch(shPostAttrs);

		shReferenceRepository.deleteInBatch(shReferenceRepository.findByShGlobalFromId(shPost.getShGlobalId()));

		shReferenceRepository.deleteInBatch(shReferenceRepository.findByShGlobalToId(shPost.getShGlobalId()));

		shGlobalIdRepository.delete(shPost.getShGlobalId().getId());

		// History
		ShHistory shHistory = new ShHistory();
		shHistory.setDate(new Date());
		shHistory.setDescription("Deleted " + shPost.getTitle() + " Post.");
		shHistory.setOwner(principal.getName());
		shHistory.setShObject(shPost);
		shHistory.setShSite(shPostUtils.getSite(shPost));
		shHistoryRepository.saveAndFlush(shHistory);

		shPostRepository.delete(id);

		return true;
	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostAdd(@RequestBody ShPost shPost, Principal principal) throws Exception {

		String title = shPost.getTitle();
		String summary = shPost.getSummary();
		// Get PostAttrs before save, because JPA Lazy
		List<ShPostAttr> shPostAttrs = shPost.getShPostAttrs();

		for (ShPostAttr shPostAttr : shPostAttrs) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr != null) {
				shPostAttr.setReferenceObjects(null);

			}
		}

		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);
		shPost.setFurl(shURLFormatter.format(title));

		shPostRepository.saveAndFlush(shPost);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPost);
		shGlobalId.setType(ShObjectType.POST);

		shGlobalIdRepository.saveAndFlush(shGlobalId);

		ShPost shPostWithGlobalId = shPost;
		shPostWithGlobalId.setShGlobalId(shGlobalId);

		for (ShPostAttr shPostAttr : shPostAttrs) {
			shPostAttr.setShPost(shPost);
			this.postAttrSave(shPostAttr, shPostWithGlobalId);
		}

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		// History
		ShHistory shHistory = new ShHistory();
		shHistory.setDate(new Date());
		shHistory.setDescription("Created " + shPost.getTitle() + " Post.");
		if (principal != null) {
			shHistory.setOwner(principal.getName());
		}
		shHistory.setShObject(shPost);
		shHistory.setShSite(shPostUtils.getSite(shPost));
		shHistoryRepository.saveAndFlush(shHistory);

		return this.shPostEdit(shPost.getId());

	}
	
	private void postAttrUpdate(ShPostAttr shPostAttr, ShPost shPost) {

		ShPostAttr shPostAttrEdit = shPostAttrRepository.findById(shPostAttr.getId()).get();
		shPostUtils.referencedObject(shPostAttrEdit, shPostAttr, shPost);
		if (shPostAttrEdit != null) {
			shPostAttrEdit.setDateValue(shPostAttr.getDateValue());
			shPostAttrEdit.setIntValue(shPostAttr.getIntValue());
			shPostAttrEdit.setStrValue(shPostAttr.getStrValue());
			shPostAttrEdit.setReferenceObjects(shPostAttr.getReferenceObjects());
		}

		boolean savedPostAttr = false;
		if (shPostAttr.getShChildrenRelatorItems() != null) {
			ShPostAttr shPostAttrClone = SerializationUtils.clone(shPostAttr);
			for (ShRelatorItem shRelatorItem : shPostAttrClone.getShChildrenRelatorItems()) {
				boolean savedRelator = false;
				if (shRelatorItem.getShChildrenPostAttrs() != null
						&& shRelatorItem.getShChildrenPostAttrs().size() > 0) {
					
					ShRelatorItem shRelatorItemClone = SerializationUtils.clone(shRelatorItem);
					
					for (ShPostAttr shChildrenPostAttr : shRelatorItemClone.getShChildrenPostAttrs()) {
						if (!savedPostAttr) {
							shPostAttrRepository.save(shPostAttrEdit);
							savedPostAttr = true;
						}
						if (!savedRelator) {
/*
							System.out.println("I " + shChildrenPostAttr.getStrValue());
							shRelatorItem.setShParentPostAttr(shPostAttrEdit);
							shRelatorItemRepository.saveAndFlush(shRelatorItem);
*/							
							savedRelator = true;
						}
	//					shChildrenPostAttr.setShParentRelatorItem(shRelatorItem);

						if (shChildrenPostAttr.getId() != null) {
							this.postAttrUpdate(shChildrenPostAttr, shPost);
						} else {
							this.postAttrSave(shChildrenPostAttr, shPost);
						}

					}
				} else {
					if (!savedPostAttr) {
						shPostAttrRepository.save(shPostAttrEdit);
						savedPostAttr = true;
					}
					if (!savedRelator) {
/*						shRelatorItem.setShParentPostAttr(shPostAttr);
						shRelatorItemRepository.saveAndFlush(shRelatorItem);
*/						
						savedRelator = true;
					}
				}
			}
		} else {
			shPostAttrRepository.save(shPostAttrEdit);
		}
	}

	private void postAttrSave(ShPostAttr shPostAttr, ShPost shPost) {
		shPostUtils.referencedObject(shPostAttr, shPost);
		boolean savedPostAttr = false;
		if (shPostAttr.getShChildrenRelatorItems() != null) {
			ShPostAttr shPostAttrClone = SerializationUtils.clone(shPostAttr);
			for (ShRelatorItem shRelatorItem : shPostAttrClone.getShChildrenRelatorItems()) {
				ShRelatorItem shRelatorItemCopy = new ShRelatorItem();
				boolean savedRelator = false;
				if (shRelatorItem.getShChildrenPostAttrs() != null
						&& shRelatorItem.getShChildrenPostAttrs().size() > 0) {
					for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
						if (!savedPostAttr) {
							shPostAttrRepository.saveAndFlush(shPostAttr);
							savedPostAttr = true;
						}
						if (!savedRelator) {
							shRelatorItemCopy.setShParentPostAttr(shPostAttr);
							shRelatorItemRepository.saveAndFlush(shRelatorItemCopy);
							savedRelator = true;
						}
						shChildrenPostAttr.setShParentRelatorItem(shRelatorItemCopy);
						this.postAttrSave(shChildrenPostAttr, shPost);

					}
				} else {
					if (!savedPostAttr) {
						shPostAttrRepository.saveAndFlush(shPostAttr);
						savedPostAttr = true;
					}
					if (!savedRelator) {
						shRelatorItemCopy.setShParentPostAttr(shPostAttr);
						shRelatorItemRepository.saveAndFlush(shRelatorItemCopy);
						savedRelator = true;
					}
				}
			}
		} else {
			shPostAttrRepository.saveAndFlush(shPostAttr);
		}
	}
}
