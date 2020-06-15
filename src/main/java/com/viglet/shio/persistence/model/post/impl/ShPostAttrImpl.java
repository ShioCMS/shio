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

	Set<? extends ShRelatorItemImpl> getShChildrenRelatorItems();

	void setShChildrenRelatorItems(Set<? extends ShRelatorItemImpl> shChildrenRelatorItems);

	ShRelatorItemImpl getShParentRelatorItem();

	void setShParentRelatorItem(ShRelatorItemImpl shParentRelatorItem);

}