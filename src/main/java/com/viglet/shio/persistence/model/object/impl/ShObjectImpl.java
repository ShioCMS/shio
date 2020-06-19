package com.viglet.shio.persistence.model.object.impl;

import static com.fasterxml.jackson.annotation.JsonTypeInfo.As.PROPERTY;
import static com.fasterxml.jackson.annotation.JsonTypeInfo.Id.NAME;

import java.util.Date;
import java.util.Set;

import com.fasterxml.jackson.annotation.JsonSubTypes;
import com.fasterxml.jackson.annotation.JsonTypeInfo;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.object.ShObjectDraft;
import com.viglet.shio.persistence.model.post.impl.ShPostAttrImpl;

@JsonTypeInfo(use = NAME, include = PROPERTY)
@JsonSubTypes({
  @JsonSubTypes.Type(value=ShObject.class, name = "ShObject"),
  @JsonSubTypes.Type(value=ShObjectDraft.class, name = "ShObjectDraft")
})
public interface ShObjectImpl {

	String getId();

	void setId(String id);

	String getOwner();

	void setOwner(String owner);

	String getFurl();

	void setFurl(String furl);

	Date getDate();

	void setDate(Date date);

	String getModifier();

	void setModifier(String modifier);

	String getPublisher();

	void setPublisher(String publisher);

	Date getModifiedDate();

	void setModifiedDate(Date modifiedDate);

	Date getPublicationDate();

	void setPublicationDate(Date publicationDate);

	int getPosition();

	void setPosition(int position);

	String getObjectType();

	void setObjectType(String objectType);

	Set<? extends ShPostAttrImpl> getShPostAttrRefs();

	void setShPostAttrRefs(Set<? extends ShPostAttrImpl> shPostAttrRefs);

	boolean isPublished();

	void setPublished(boolean published);

	String getPublishStatus();

	void setPublishStatus(String publishStatus);

	Set<String> getShUsers();

	void setShUsers(Set<String> shUsers);

	Set<String> getShGroups();

	void setShGroups(Set<String> shGroups);

	boolean isPageAllowRegisterUser();

	void setPageAllowRegisterUser(boolean pageAllowRegisterUser);

	boolean isPageAllowGuestUser();

	void setPageAllowGuestUser(boolean pageAllowGuestUser);

	Set<String> getShPageGroups();

	void setShPageGroups(Set<String> shPageGroups);

}