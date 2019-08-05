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

package com.viglet.shiohara.component;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.DeserializationFeature;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.ShPostDraftAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostDraftAttrRepository;
import com.viglet.shiohara.property.ShMgmtProperties;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
public class ShGetRelationComponent {

	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostDraftAttrRepository shPostDraftAttrRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShMgmtProperties shMgmtProperties;

	// @Cacheable(value = "component", key = "{ #root.methodName, #postAttrId}")
	public List<Map<String, ShPostAttr>> findByPostAttrId(String postAttrId) {
		if (shMgmtProperties.isEnabled()) {
			ShPostDraftAttr shPostDraftAttr = shPostDraftAttrRepository.findById(postAttrId).get();
			if (shPostDraftAttr != null) {
				ObjectMapper mapper = new ObjectMapper();
				try {
					String jsonInString = mapper.configure(DeserializationFeature.FAIL_ON_UNKNOWN_PROPERTIES, false)
							.disable(SerializationFeature.FAIL_ON_EMPTY_BEANS)
							.writerWithView(ShJsonView.ShJsonViewObject.class).writeValueAsString(shPostDraftAttr);
					ShPostAttr shPostAttr = mapper.readValue(jsonInString, ShPostAttr.class);
					return shPostUtils.relationToMap(shPostAttr);
				} catch (JsonProcessingException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}

		ShPostAttr shPostAttr = shPostAttrRepository.findById(postAttrId).get();
		return shPostUtils.relationToMap(shPostAttr);
	}
}
