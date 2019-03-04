/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.api.reference;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/reference")
@Api(tags = "Reference", description = "Reference API")
public class ShReferenceAPI {

	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceList() throws Exception {
		return shReferenceRepository.findAll();
	}

	@GetMapping("/from/{fromId}")
	@JsonView({ ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceFrom(@PathVariable String fromId) throws Exception {
		ShObject shObject = shObjectRepository.findById(fromId).orElse(null);
		return shReferenceRepository.findByShObjectFrom(shObject);
	}

	@GetMapping("/to/{toId}")
	@JsonView({ ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceTo(@PathVariable String toId) throws Exception {
		ShObject shObject = shObjectRepository.findById(toId).orElse(null);
		return shReferenceRepository.findByShObjectTo(shObject);
	}

	@PostMapping("/to/{toId}/replace/{otherId}")
	@JsonView({ ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceToReplace(@PathVariable String toId, @PathVariable String otherId)
			throws Exception {
		ShObject shObject = shObjectRepository.findById(toId).orElse(null);
		ShObject shObjectOther = shObjectRepository.findById(otherId).orElse(null);
		if (shObject != null && shObjectOther != null) {
			List<ShReference> shReferences = shReferenceRepository.findByShObjectTo(shObject);

			for (ShReference shReference : shReferences) {
				if (shReference != null && shReference.getShObjectFrom() != null
						&& shReference.getShObjectFrom() instanceof ShPost) {
					ShPost shPost = (ShPost) shReference.getShObjectFrom();
					for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
						ShObject shObjectReference = shPostAttr.getReferenceObject();
						if (shObject.getId() != null
								&& shObjectReference.getId().toString().equals(shObject.getId().toString())) {
							shPostAttr.setReferenceObject(shObjectOther);
							shPostAttrRepository.saveAndFlush(shPostAttr);
						}
					}
				}
				shReference.setShObjectTo(shObjectOther);
				shReferenceRepository.saveAndFlush(shReference);
			}
			return shReferenceRepository.findByShObjectTo(shObject);
		} else {
			return null;
		}
	}
}
