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
package com.viglet.shiohara.api.post.type;

import java.util.List;
import java.util.Optional;

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

/**
 * @author Alexandre Oliveira
 */
@RestController
@RequestMapping("/api/v2/post/type/attr")
@Api(tags = "Post Type Attribute", description = "Post Type Attribute API")
public class ShPostTypeAttrAPI {
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;

	@GetMapping
	public List<ShPostTypeAttr> shPostTypeAttrList() throws Exception {
		return shPostTypeAttrRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewPostTypeAttr.class })
	public ShPostTypeAttr shPostTypeAttrEdit(@PathVariable String id) throws Exception {
		return shPostTypeAttrRepository.findById(id).orElse(null);
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewPostTypeAttr.class })
	public ShPostTypeAttr shPostTypeAttrUpdate(@PathVariable String id, @RequestBody ShPostTypeAttr shPostTypeAttr)
			throws Exception {
		Optional<ShPostTypeAttr> shPostTypeAttrOptional = shPostTypeAttrRepository.findById(id);
		if (shPostTypeAttrOptional.isPresent()) {
			ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrOptional.get();
			shPostTypeAttrEdit.setIsSummary(shPostTypeAttr.getIsSummary());
			shPostTypeAttrEdit.setIsTitle(shPostTypeAttr.getIsTitle());
			shPostTypeAttrEdit.setLabel(shPostTypeAttr.getLabel());
			shPostTypeAttrEdit.setName(shPostTypeAttr.getName());
			shPostTypeAttrEdit.setOrdinal(shPostTypeAttr.getOrdinal());
			shPostTypeAttrEdit.setRequired(shPostTypeAttr.getRequired());
			shPostTypeAttrEdit.setDescription(shPostTypeAttr.getDescription());
			shPostTypeAttrEdit.setShWidget(shPostTypeAttr.getShWidget());
			shPostTypeAttrRepository.save(shPostTypeAttrEdit);
			return shPostTypeAttrEdit;
		}
		return null;

	}

	@DeleteMapping("/{id}")
	public boolean shPostTypeAttrDelete(@PathVariable String id) throws Exception {
		Optional<ShPostTypeAttr> shPostTypeAttrOptional = shPostTypeAttrRepository.findById(id);
		if (shPostTypeAttrOptional.isPresent()) {
			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrOptional.get();
			for (ShPostAttr shPostAttr : shPostTypeAttr.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}
			shPostTypeAttrRepository.delete(id);
			return true;
		}
		return false;
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewPostTypeAttr.class })
	public ShPostTypeAttr shPostTypeAttrStructure() throws Exception {
		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		return shPostTypeAttr;

	}

}
