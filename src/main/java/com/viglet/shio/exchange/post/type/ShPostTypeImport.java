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
package com.viglet.shio.exchange.post.type;

import java.util.Date;
import java.util.HashSet;
import java.util.Map.Entry;
import java.util.Set;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.exchange.ShExchange;
import com.viglet.shio.exchange.ShPostTypeExchange;
import com.viglet.shio.exchange.ShPostTypeFieldExchange;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.model.widget.ShWidget;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.persistence.repository.widget.ShWidgetRepository;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShPostTypeImport {
	private static final Logger logger = LogManager.getLogger(ShPostTypeImport.class);
	@Autowired
	ShWidgetRepository shWidgetRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;

	public void importPostType(ShExchange shExchange, boolean isCloned){
		logger.info("1 of 4 - Importing Post Types");
		for (ShPostTypeExchange shPostTypeExchange : shExchange.getPostTypes()) {
			if (shPostTypeRepository.findByName(shPostTypeExchange.getName()) == null) {
				logger.info(String.format(".. %s Post Type (%s)", shPostTypeExchange.getName(), shPostTypeExchange.getId()));
				ShPostType shPostType = new ShPostType();
				shPostType.setId(shPostTypeExchange.getId());
				shPostType.setTitle(shPostTypeExchange.getLabel());
				shPostType.setDate(isCloned ? new Date() : shPostTypeExchange.getDate());
				shPostType.setDescription(shPostTypeExchange.getDescription());
				shPostType.setName(shPostTypeExchange.getName());
				shPostType.setNamePlural(shPostTypeExchange.getNamePlural());
				shPostType.setOwner(shPostTypeExchange.getOwner());
				shPostType.setSystem(shPostTypeExchange.isSystem() ? (byte) 1 : (byte) 0);

				this.importPostTypeAttr(shPostTypeExchange, shPostType);

				shPostTypeRepository.save(shPostType);
			}
		}
	}

	private void importPostTypeAttr(ShPostTypeExchange shPostTypeExchange, ShPostType shPostType) {
		Set<ShPostTypeAttr> shPostTypeAttrs = new HashSet<>();
		if (shPostTypeExchange.getFields() != null && shPostTypeExchange.getFields().size() > 0) {
			for (Entry<String, ShPostTypeFieldExchange> postTypeField : shPostTypeExchange.getFields()
					.entrySet()) {
				ShPostTypeAttr shPostTypeAttr = this.importPostTypeField(postTypeField);
				shPostTypeAttr.setShPostType(shPostType);
				shPostTypeAttrs.add(shPostTypeAttr);
			}
		}
		shPostType.setShPostTypeAttrs(shPostTypeAttrs);
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
		Set<ShPostTypeAttr> shPostTypeAttrs = new HashSet<>();
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
