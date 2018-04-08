package com.viglet.shiohara.api.post.type;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/post/type/attr")
@Api(tags="Post Type Attribute", description="Post Type Attribute API")
public class ShPostTypeAttrAPI {
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	
	@GetMapping
	public List<ShPostTypeAttr>  shPostTypeAttrList() throws Exception {
		return shPostTypeAttrRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ShJsonView.ShJsonViewPostTypeAttr.class})
	public ShPostTypeAttr  shPostTypeAttrEdit(@PathVariable UUID id) throws Exception {
		return shPostTypeAttrRepository.findById(id).get();
	}

	@PutMapping("/{id}")
	@JsonView({ShJsonView.ShJsonViewPostTypeAttr.class})
	public ShPostTypeAttr  shPostTypeAttrUpdate(@PathVariable UUID id, @RequestBody ShPostTypeAttr shPostTypeAttr) throws Exception {
		ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrRepository.findById(id).get();
		shPostTypeAttrEdit.setIsSummary(shPostTypeAttr.getIsSummary());
		shPostTypeAttrEdit.setIsTitle(shPostTypeAttr.getIsTitle());
		shPostTypeAttrEdit.setLabel(shPostTypeAttr.getLabel());
		shPostTypeAttrEdit.setMany(shPostTypeAttr.getMany());
		shPostTypeAttrEdit.setName(shPostTypeAttr.getName());
		shPostTypeAttrEdit.setOrdinal(shPostTypeAttr.getOrdinal());
		shPostTypeAttrEdit.setRequired(shPostTypeAttr.getRequired());
		shPostTypeAttrEdit.setDescription(shPostTypeAttr.getDescription());
		shPostTypeAttrEdit.setShWidget(shPostTypeAttr.getShWidget());
		shPostTypeAttrRepository.save(shPostTypeAttrEdit);
		return shPostTypeAttrEdit;
	}

	@DeleteMapping("/{id}")
	public boolean  shPostTypeAttrDelete(@PathVariable UUID id) throws Exception {
		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findById(id).get();
		for ( ShPostAttr shPostAttr : shPostTypeAttr.getShPostAttrs()) {
			shPostAttrRepository.delete(shPostAttr.getId());
		}
		shPostTypeAttrRepository.delete(id);
		return true;
	}
	
	@GetMapping("/model")
	@JsonView({ShJsonView.ShJsonViewPostTypeAttr.class})
	public ShPostTypeAttr  shPostTypeAttrStructure() throws Exception {
		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		return shPostTypeAttr;

	}

}
