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

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value=ShRelatorItem.class, name = "ShRelatorItem"),
  @JsonSubTypes.Type(value=ShRelatorItemDraft.class, name = "ShRelatorItemDraft")
})
public interface ShRelatorItemImpl {

	String getId();

	void setId(String id);

	Set<? extends ShPostAttrImpl> getShChildrenPostAttrs();
	
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