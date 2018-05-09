package com.viglet.shiohara.api.post.type;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectType;
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
@Api(tags = "Post Type", description = "PostType API")
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

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public List<ShPostType> shPostTypeList() throws Exception {
		return shPostTypeRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeEdit(@PathVariable UUID id) throws Exception {
		return shPostTypeRepository.findById(id).get();
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeStructure() throws Exception {
		ShPostType shPostType = new ShPostType();
		return shPostType;

	}

	@GetMapping("/{id}/post/model")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPost shPostTypePostStructure(@PathVariable UUID id) throws Exception {
		ShPost shPost = new ShPost();
		shPost.setShPostType(shPostTypeRepository.findById(id).get());
		List<ShPostAttr> shPostAttrs = new ArrayList<ShPostAttr>();
		for (ShPostTypeAttr shPostTypeAttr : shPost.getShPostType().getShPostTypeAttrs()) {
			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(shPostTypeAttr.getId());

			if (shPostTypeAttr.getShPostTypeAttrs().size() > 0) {
				List<ShPostAttr> shRelatorPostAttrs = new ArrayList<ShPostAttr>();
				for (ShPostTypeAttr shRelatorPostTypeAttr : shPostTypeAttr.getShPostTypeAttrs()) {
					ShPostAttr shRelatorPostAttr = new ShPostAttr();
					shRelatorPostAttr.setShPostTypeAttr(shRelatorPostTypeAttr);
					shRelatorPostAttr.setShPostTypeAttrId(shRelatorPostTypeAttr.getId());
					shRelatorPostAttrs.add(shRelatorPostAttr);
				}
				shPostAttr.setShRelatorPostAttrs(shRelatorPostAttrs);
			}
			shPostAttrs.add(shPostAttr);
		}
		shPost.setShPostAttrs(shPostAttrs);
		return shPost;

	}

	@GetMapping("/name/{postTypeName}/post/model")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPost shPostTypeByNamePostStructure(@PathVariable String postTypeName) throws Exception {
		ShPost shPost = new ShPost();
		shPost.setShPostType(shPostTypeRepository.findByName(postTypeName));
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

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeUpdate(@PathVariable UUID id, @RequestBody ShPostType shPostType) throws Exception {
		ShPostType shPostTypeEdit = shPostTypeRepository.findById(id).get();

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			ShPostTypeAttr shPostTypeAttrEdit = null;
			if (shPostTypeAttr.getId() != null) {
				shPostTypeAttrEdit = shPostTypeAttrRepository.findById(shPostTypeAttr.getId()).get();
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
				}
			} else {
				shPostTypeAttr.setShPostType(shPostType);
				shPostTypeAttrRepository.saveAndFlush(shPostTypeAttr);
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

	@DeleteMapping("/{id}")
	public boolean shPostTypeDelete(@PathVariable UUID id) throws Exception {
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

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeAdd(@RequestBody ShPostType shPostType) throws Exception {
		shPostTypeRepository.save(shPostType);
		shPostType.setDate(new Date());

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPostType);
		shGlobalId.setType(ShObjectType.POST_TYPE);

		shGlobalIdRepository.save(shGlobalId);

		return shPostType;

	}

	@PostMapping("/{id}/attr")
	public ShPostTypeAttr shPostTypeAttrAdd(@PathVariable UUID id, @RequestBody ShPostTypeAttr shPostTypeAttr)
			throws Exception {
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
