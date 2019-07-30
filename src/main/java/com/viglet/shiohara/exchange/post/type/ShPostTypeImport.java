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

package com.viglet.shiohara.exchange.post.type;

import java.io.IOException;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShPostTypeExchange;
import com.viglet.shiohara.exchange.ShPostTypeFieldExchange;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

@Component
public class ShPostTypeImport {

	@Autowired
	ShWidgetRepository shWidgetRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;

	public void importPostType(ShExchange shExchange) throws IOException {
		for (ShPostTypeExchange shPostTypeExchange : shExchange.getPostTypes()) {
			if (shPostTypeRepository.findByName(shPostTypeExchange.getName()) == null) {
				ShPostType shPostType = new ShPostType();
				shPostType.setId(shPostTypeExchange.getId());
				shPostType.setTitle(shPostTypeExchange.getLabel());
				shPostType.setDate(shPostTypeExchange.getDate());
				shPostType.setDescription(shPostTypeExchange.getDescription());
				shPostType.setName(shPostTypeExchange.getName());
				shPostType.setOwner(shPostTypeExchange.getOwner());
				shPostType.setSystem(shPostTypeExchange.isSystem() ? (byte) 1 : (byte) 0);

				Set<ShPostTypeAttr> shPostTypeAttrs = new HashSet<ShPostTypeAttr>();
				if (shPostTypeExchange.getFields() != null && shPostTypeExchange.getFields().size() > 0) {
					for (Entry<String, ShPostTypeFieldExchange> postTypeField : shPostTypeExchange.getFields()
							.entrySet()) {
						ShPostTypeAttr shPostTypeAttr = this.importPostTypeField(postTypeField);
						shPostTypeAttr.setShPostType(shPostType);
						shPostTypeAttrs.add(shPostTypeAttr);
					}
				}
				shPostType.setShPostTypeAttrs(shPostTypeAttrs);

				shPostTypeRepository.save(shPostType);
			}
		}
	}

	public ShPostTypeAttr importPostTypeField(Entry<String, ShPostTypeFieldExchange> postTypeField) {
		ShPostTypeFieldExchange shPostTypeFieldExchange = postTypeField.getValue();
		ShWidget shWidget = shWidgetRepository.findByName(shPostTypeFieldExchange.getWidget());

		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		shPostTypeAttr.setId(shPostTypeFieldExchange.getId());
		shPostTypeAttr.setDescription(shPostTypeFieldExchange.getDescription());
		shPostTypeAttr.setLabel(shPostTypeFieldExchange.getLabel());
		shPostTypeAttr.setName(postTypeField.getKey());
		shPostTypeAttr.setOrdinal(shPostTypeFieldExchange.getOrdinal());
		shPostTypeAttr.setIsSummary(shPostTypeFieldExchange.isSummary() ? (byte) 1 : (byte) 0);
		shPostTypeAttr.setIsTitle(shPostTypeFieldExchange.isTitle() ? (byte) 1 : (byte) 0);
		shPostTypeAttr.setRequired(shPostTypeFieldExchange.isRequired() ? (byte) 1 : (byte) 0);
		shPostTypeAttr.setShWidget(shWidget);
		shPostTypeAttr.setWidgetSettings(shPostTypeFieldExchange.getWidgetSettings());
		Set<ShPostTypeAttr> shPostTypeAttrs = new HashSet<ShPostTypeAttr>();
		if (shPostTypeFieldExchange.getFields() != null && shPostTypeFieldExchange.getFields().size() > 0) {
			for (Entry<String, ShPostTypeFieldExchange> postTypeFieldChild : shPostTypeFieldExchange.getFields()
					.entrySet()) {
				ShPostTypeAttr shPostTypeAttrChild = this.importPostTypeField(postTypeFieldChild);
				shPostTypeAttrChild.setShParentPostTypeAttr(shPostTypeAttr);
				shPostTypeAttrs.add(shPostTypeAttrChild);
			}
		}
		shPostTypeAttr.setShPostTypeAttrs(shPostTypeAttrs);

		return shPostTypeAttr;
	}
}
