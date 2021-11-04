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

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.impl.ShObjectImpl;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.ShPostDraft;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.site.ShSite;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

/**
 * The interface class for the ShPost.
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value=ShPost.class, name = "ShPost"),
  @JsonSubTypes.Type(value=ShPostDraft.class, name = "ShPostDraft")
})
public interface ShPostImpl extends ShObjectImpl {
	
	String getSummary();

	void setSummary(String summary);

	String getTitle();

	void setTitle(String title);

	ShPostType getShPostType();

	void setShPostType(ShPostType shPostType);

	Set<? extends ShPostAttrImpl> getShPostAttrs(); //NOSONAR

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