/*
 * Copyright (C) 2016-2018 the original author or authors. 
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
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Set;

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
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.history.ShHistory;
import com.viglet.shiohara.persistence.model.post.ShPost;
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
		ShPost shPost = shPostRepository.findById(id).get();
		Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		shPost.setShPostAttrs(shPostAttrs);

		return shPost;
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
		shPostUtils.saveDoc(shPost);

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

		ShPost shPost = shPostRepository.findById(id).get();

		shTuringIntegration.deindexObject(shPost);

		Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		if (shPost.getShPostType().getName().equals(ShSystemPostType.FILE) && shPostAttrs.size() > 0) {
			File file = shStaticFileUtils.filePath(shPost.getShFolder(), shPostAttrs.iterator().next().getStrValue());
			if (file != null) {
				if (file.exists()) {
					if (!file.delete()) {
						logger.error(String.format("Could not delete the file: %s", file.getName()));
					}
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
				shPostAttr.setReferenceObjects(null);
			}
		}

		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);
		shPost.setFurl(shURLFormatter.format(title));

		for (ShPostAttr shPostAttr : shPostAttrs) {
			shPostAttr.setShPost(shPost);
			this.postAttrSave(shPostAttr, shPost);
		}

		shPostRepository.saveAndFlush(shPost);

		this.postReferenceSave(shPost);

		ShUser shUser = shUserRepository.findByUsername("admin");
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

	}

	private void postAttrSave(ShPostAttr shPostAttr, ShPost shPost) {
		for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
			shRelatorItem.setShParentPostAttr(shPostAttr);
			for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {

				if (shChildrenPostAttr.getShPostTypeAttr().getIsTitle() == 1)
					shRelatorItem.setTitle(StringUtils.abbreviate(shChildrenPostAttr.getStrValue(), 255));

				if (shChildrenPostAttr.getShPostTypeAttr().getIsSummary() == 1)
					shRelatorItem.setSummary(StringUtils.abbreviate(shChildrenPostAttr.getStrValue(), 255));

				shChildrenPostAttr.setShParentRelatorItem(shRelatorItem);
				this.postAttrSave(shChildrenPostAttr, shPost);
			}
		}
	}

	private void postReferenceSave(ShPost shPost) {
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostUtils.referencedObject(shPostAttr, shPost);
			for (ShRelatorItem shRelatorItem : shPostAttr.getShChildrenRelatorItems()) {
				for (ShPostAttr shChildrenPostAttr : shRelatorItem.getShChildrenPostAttrs()) {
					this.postAttrSave(shChildrenPostAttr, shPost);
				}
			}
		}
		shPostRepository.saveAndFlush(shPost);
	}
}