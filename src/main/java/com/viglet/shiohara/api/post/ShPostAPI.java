package com.viglet.shiohara.api.post;

import java.io.File;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

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
		Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
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

		this.postSave(shPost, principal);

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

		return this.shPostEdit(shPost.getId());

	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPost shPostAdd(@RequestBody ShPost shPost, Principal principal) throws Exception {

		this.postSave(shPost, principal);

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
	public boolean shPostDelete(@PathVariable UUID id, Principal principal) throws Exception {

		ShPost shPost = shPostRepository.findById(id).get();
		Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE) && shPostAttrs.size() > 0) {
			File file = shStaticFileUtils.filePath(shPost.getShFolder(), shPostAttrs.iterator().next().getStrValue());
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
		shHistory.setShObject(shPost.getId());
		shHistory.setShSite(shPostUtils.getSite(shPost).getId());
		shHistoryRepository.saveAndFlush(shHistory);

		shPostRepository.delete(id);

		return true;
	}

	private void postSave(ShPost shPost, Principal principal) {
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
				shPostAttr.setReferenceObjects(null);

			}
		}

		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);
		shPost.setFurl(shURLFormatter.format(title));

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPost);
		shGlobalId.setType(ShObjectType.POST);

		for (ShPostAttr shPostAttr : shPostAttrs) {
			shPostAttr.setShPost(shPost);
			this.postAttrSave(shPostAttr, shPost);
		}

		shPostRepository.saveAndFlush(shPost);

		shGlobalIdRepository.saveAndFlush(shGlobalId);

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

	}

	private void postAttrSave(ShPostAttr shPostAttr, ShPost shPost) {

		shPostUtils.referencedObject(shPostAttr, shPost);
		for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
				shChildrenPostAttr.setShParentRelatorItem(shRelatorItem);
				this.postAttrSave(shChildrenPostAttr, shPost);
			}
		}
	}
}