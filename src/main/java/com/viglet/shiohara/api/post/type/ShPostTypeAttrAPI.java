package com.viglet.shiohara.api.post.type;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;

@RestController
@RequestMapping("/api/v2/post/type/attr")
public class ShPostTypeAttrAPI {
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	
	@RequestMapping(method = RequestMethod.GET)
	public List<ShPostTypeAttr>  shPostTypeAttrList() throws Exception {
		return shPostTypeAttrRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ShPostTypeAttr  shPostTypeAttrEdit(@PathVariable UUID id) throws Exception {
		return shPostTypeAttrRepository.findById(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ShPostTypeAttr  shPostTypeAttrUpdate(@PathVariable UUID id, @RequestBody ShPostTypeAttr shPostTypeAttr) throws Exception {
		ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrRepository.findById(id);
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

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public boolean  shPostTypeAttrDelete(@PathVariable UUID id) throws Exception {
		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findById(id);
		for ( ShPostAttr shPostAttr : shPostTypeAttr.getShPostAttrs()) {
			shPostAttrRepository.delete(shPostAttr.getId());
		}
		shPostTypeAttrRepository.delete(id);
		return true;
	}

	@Deprecated
	@RequestMapping(method = RequestMethod.POST)
	public ShPostTypeAttr  shPostTypeAttrPost(@RequestBody ShPostTypeAttr shPostTypeAttr) throws Exception {
		shPostTypeAttrRepository.save(shPostTypeAttr);
		return shPostTypeAttr;

	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/model")
	public ShPostTypeAttr  shPostTypeAttrStructure() throws Exception {
		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		return shPostTypeAttr;

	}

}
