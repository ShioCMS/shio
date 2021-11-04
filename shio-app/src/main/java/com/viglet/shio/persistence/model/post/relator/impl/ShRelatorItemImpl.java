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
package com.viglet.shio.persistence.model.post.relator.impl;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;
import com.viglet.shio.persistence.model.post.impl.ShPostAttrImpl;
import com.viglet.shio.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shio.persistence.model.post.relator.ShRelatorItemDraft;

/**
 * The interface class for the ShRelatorItem.
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value=ShRelatorItem.class, name = "ShRelatorItem"),
  @JsonSubTypes.Type(value=ShRelatorItemDraft.class, name = "ShRelatorItemDraft")
})
public interface ShRelatorItemImpl {

	String getId();

	void setId(String id);

	Set<? extends ShPostAttrImpl> getShChildrenPostAttrs(); //NOSONAR
	
	Set<ShPostAttr> getShChildrenPostAttrsNonDraft();
	
	Set<ShPostDraftAttr> getShChildrenPostAttrsDraft();

	void setShChildrenPostAttrs(Set<? extends ShPostAttrImpl> shChildrenPostAttrs);

	ShPostAttrImpl getShParentPostAttr();

	void setShParentPostAttr(ShPostAttrImpl shParentPostAttr);

	String getTitle();

	void setTitle(String title);

	String getSummary();

	void setSummary(String summary);

	int getOrdinal();

	void setOrdinal(int ordinal);

}