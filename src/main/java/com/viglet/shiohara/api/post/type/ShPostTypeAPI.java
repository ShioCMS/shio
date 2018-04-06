package com.viglet.shiohara.api.post.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/post/type")
@Api(tags="Post Type", description="PostType API")
public class ShPostTypeAPI {
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	@JsonView({ShJsonView.ShJsonViewObject.class})
	public List<ShPostType> shPostTypeList() throws Exception {
		return shPostTypeRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@JsonView({ShJsonView.ShJsonViewPostType.class})
	public ShPostType  shPostTypeEdit(@PathVariable UUID id) throws Exception {
		return shPostTypeRepository.findById(id).get();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/model")	
	@JsonView({ShJsonView.ShJsonViewPostType.class})
	public ShPostType  shPostTypeStructure() throws Exception {
		ShPostType shPostType = new ShPostType();
		return shPostType;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/post/model")
	@JsonView({ShJsonView.ShJsonViewPostType.class})
	public ShPost  shPostTypePostStructure(@PathVariable UUID id) throws Exception {
		ShPost shPost = new ShPost();
		shPost.setShPostType(shPostTypeRepository.findById(id).get());
		List<ShPostAttr> shPostAttrs = new ArrayList<ShPostAttr>();
		for (ShPostTypeAttr shPostTypeAttr : shPost.getShPostType().getShPostTypeAttrs()) {
			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(shPostTypeAttr.getId());
			shPostAttrs.add(shPostAttr);
		}
		shPost.setShPostAttrs(shPostAttrs);
		return shPost;

	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@JsonView({ShJsonView.ShJsonViewPostType.class})
	public ShPostType  shPostTypeUpdate(@PathVariable UUID id, @RequestBody ShPostType shPostType) throws Exception {
		ShPostType shPostTypeEdit = shPostTypeRepository.findById(id).get();

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {

			ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrRepository.findById(shPostTypeAttr.getId()).get();

			if (shPostTypeAttrEdit != null) {
				shPostTypeAttrEdit.setDescription(shPostTypeAttr.getDescription());
				shPostTypeAttrEdit.setIsSummary(shPostTypeAttr.getIsSummary());
				shPostTypeAttrEdit.setIsTitle(shPostTypeAttr.getIsTitle());
				shPostTypeAttrEdit.setLabel(shPostTypeAttr.getLabel());
				shPostTypeAttrEdit.setMany(shPostTypeAttr.getMany());
				shPostTypeAttrEdit.setName(shPostTypeAttr.getName());
				shPostTypeAttrEdit.setOrdinal(shPostTypeAttr.getOrdinal());
				shPostTypeAttrEdit.setRequired(shPostTypeAttr.getRequired());
				shPostTypeAttrEdit.setShWidget(shPostTypeAttr.getShWidget());

				shPostTypeAttrRepository.saveAndFlush(shPostTypeAttrEdit);
			} else {
				if (shPostTypeAttr.getId() == null) {
					shPostTypeAttr.setShPostType(shPostType);
					shPostTypeAttrRepository.saveAndFlush(shPostTypeAttr);
				}
			}
		}

		shPostTypeEdit = shPostTypeRepository.findById(id).get();
		shPostTypeEdit.setDate(shPostType.getDate());
		shPostTypeEdit.setTitle(shPostType.getTitle());
		shPostTypeEdit.setDescription(shPostType.getDescription());
		shPostTypeEdit.setName(shPostType.getName());

		shPostTypeRepository.saveAndFlush(shPostTypeEdit);
		return shPostTypeEdit;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public boolean  shPostTypeDelete(@PathVariable UUID id) throws Exception {
		ShPostType shPostType = shPostTypeRepository.findById(id).get();

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			for (ShPostAttr shPostAttr : shPostTypeAttr.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}
			shPostTypeAttrRepository.delete(shPostTypeAttr.getId());
		}

		for (ShPost shPost : shPostType.getShPosts()) {
			for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}
			shGlobalIdRepository.delete(shPost.getShGlobalId().getId());
			shPostRepository.delete(shPost.getId());
		}

		shGlobalIdRepository.delete(shPostType.getShGlobalId().getId());
		
		shPostTypeRepository.delete(id);
		return true;
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView({ShJsonView.ShJsonViewPostType.class})
	public ShPostType shPostTypeAdd(@RequestBody ShPostType shPostType) throws Exception {
		shPostTypeRepository.save(shPostType);
		shPostType.setDate(new Date());
		
		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPostType);
		shGlobalId.setType("POST_TYPE");

		shGlobalIdRepository.save(shGlobalId);
		
		return shPostType;

	}

	@RequestMapping(method = RequestMethod.POST, value = "/{id}/attr")
	public ShPostTypeAttr  shPostTypeAttrAdd(@PathVariable UUID id, @RequestBody ShPostTypeAttr shPostTypeAttr) throws Exception {
		ShPostType shPostType = shPostTypeRepository.findById(id).get();
		if (shPostType != null) {
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttrRepository.save(shPostTypeAttr);
			return shPostTypeAttr;
		} else {
			return null;
		}

	}

}
