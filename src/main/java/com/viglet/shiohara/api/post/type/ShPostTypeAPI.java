/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.exchange.post.type.ShPostTypeExport;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
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
	private ShPostTypeExport shPostTypeExport;
	
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public List<ShPostType> shPostTypeList() throws Exception {
		return shPostTypeRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeEdit(@PathVariable String id) throws Exception {
		return shPostTypeRepository.findById(id).orElse(null);
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeStructure() throws Exception {
		ShPostType shPostType = new ShPostType();
		return shPostType;

	}

	@GetMapping("/{id}/post/model")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPost shPostTypePostStructure(@PathVariable String id) throws Exception {
		ShPost shPost = new ShPost();
		shPost.setShPostType(shPostTypeRepository.findById(id).orElse(null));
		Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();
		for (ShPostTypeAttr shPostTypeAttr : shPost.getShPostType().getShPostTypeAttrs()) {
			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
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
		Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();
		for (ShPostTypeAttr shPostTypeAttr : shPost.getShPostType().getShPostTypeAttrs()) {
			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttrs.add(shPostAttr);
		}
		shPost.setShPostAttrs(shPostAttrs);
		return shPost;

	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeUpdate(@PathVariable String id, @RequestBody ShPostType shPostType) throws Exception {
		this.postTypeSave(shPostType);
		return shPostType;
	}

	@Transactional
	@DeleteMapping("/{id}")
	public boolean shPostTypeDelete(@PathVariable String id) throws Exception {
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
			shPostRepository.delete(shPost.getId());
		}

		shPostTypeRepository.delete(id);
		return true;
	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostType shPostTypeAdd(@RequestBody ShPostType shPostType) throws Exception {
		
		this.postTypeSave(shPostType);

		return shPostType;

	}

	@PostMapping("/{id}/attr")
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	public ShPostTypeAttr shPostTypeAttrAdd(@PathVariable String id, @RequestBody ShPostTypeAttr shPostTypeAttr)
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
	
	@ResponseBody
	@GetMapping(value = "/export", produces = "application/zip")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public StreamingResponseBody shPostTypeExport(HttpServletResponse response) throws Exception {
		
		return shPostTypeExport.exportObject(response);

	}

	private void postTypeSave(ShPostType shPostType) {

		shPostType.setDate(new Date());

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			shPostTypeAttr.setShPostType(shPostType);
			this.postTypeAttrSave(shPostTypeAttr, shPostType);
		}

		shPostTypeRepository.saveAndFlush(shPostType);

	}

	private void postTypeAttrSave(ShPostTypeAttr shPostTypeAttr, ShPostType shPostType) {

		for (ShPostTypeAttr shChildPostTypeAttr : shPostTypeAttr.getShPostTypeAttrs()) {
			shChildPostTypeAttr.setShParentPostTypeAttr(shPostTypeAttr);
			this.postTypeAttrSave(shChildPostTypeAttr, shPostType);
		}
	}
}
