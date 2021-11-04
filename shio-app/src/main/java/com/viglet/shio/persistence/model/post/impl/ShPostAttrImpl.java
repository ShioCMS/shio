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
package com.viglet.shio.persistence.model.post.impl;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;
import com.viglet.shio.persistence.model.post.relator.impl.ShRelatorItemImpl;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;


/**
 * The interface class for the ShPostAttr.
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value=ShPostAttr.class, name = "ShPostAttr"),
  @JsonSubTypes.Type(value=ShPostDraftAttr.class, name = "ShPostDraftAttr")
})
public interface ShPostAttrImpl {

	ShObject getReferenceObject();

	void setReferenceObject(ShObject referenceObject);

	String getId();

	void setId(String id);

	Date getDateValue();

	void setDateValue(Date dateValue);

	int getIntValue();

	void setIntValue(int intValue);

	String getStrValue();

	void setStrValue(String strValue);

	Set<String> getArrayValue();

	void setArrayValue(Set<String> arrayValue);

	int getType();

	void setType(int type);

	ShPostImpl getShPost();

	void setShPost(ShPostImpl shPost);

	ShPostTypeAttr getShPostTypeAttr();

	void setShPostTypeAttr(ShPostTypeAttr shPostTypeAttr);

	Set<? extends ShRelatorItemImpl> getShChildrenRelatorItems(); //NOSONAR

	void setShChildrenRelatorItems(Set<? extends ShRelatorItemImpl> shChildrenRelatorItems);

	ShRelatorItemImpl getShParentRelatorItem();

	void setShParentRelatorItem(ShRelatorItemImpl shParentRelatorItem);

}