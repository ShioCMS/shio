package com.viglet.shio.persistence.model.post.impl;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.ShPostDraft;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.site.ShSite;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value=ShPost.class, name = "ShPost"),
  @JsonSubTypes.Type(value=ShPostDraft.class, name = "ShPostDraft")
})
public interface ShPostImpl {
	
	String getSummary();

	void setSummary(String summary);

	String getTitle();

	void setTitle(String title);

	ShPostType getShPostType();

	void setShPostType(ShPostType shPostType);

	Set<?> getShPostAttrs();

	void setShPostAttrs(Set<? extends ShPostAttrImpl> shPostAttrs);

	ShPostAttrImpl addShPostAttr(ShPostAttrImpl shPostAttr);

	ShPostAttrImpl removeShPostAttr(ShPostAttrImpl shPostAttr);

	ShFolder getShFolder();

	void setShFolder(ShFolder shFolder);

	ShSite getShSite();

	void setShSite(ShSite shSite);

	String getObjectType();

	void setObjectType(String objectType);
	
	Set<ShPostAttr> getShPostAttrsNonDraft();
		
	Set<ShPostDraftAttr> getShPostAttrsDraft();
	
}