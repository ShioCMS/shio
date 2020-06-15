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
package com.viglet.shio.persistence.model.object;

import java.io.Serializable;
import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shio.persistence.model.post.ShPostDraftAttr;

/**
 * The persistent class for the ShObjectDraft database table.
 * 
 * @author Alexandre Oliveira
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(name = "ShObjectDraft.findAll", query = "SELECT od FROM ShObjectDraft od")
@JsonIgnoreProperties({ "shPostAttrRefs", "shGroups", "shUsers", "summary" })
public class ShObjectDraft implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shio.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	// bi-directional many-to-one association to ShObject
	@OneToMany(mappedBy = "referenceObject") // , orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@Cascade({ CascadeType.ALL })
//	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShPostDraftAttr> shPostAttrRefs = new HashSet<>();

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String owner;

	private String furl;

	private String modifier;

	private String publisher;

	private Date modifiedDate;

	private Date publicationDate;

	private int position;

	private String objectType;

	private boolean published;

	@Column(name = "draft", length = 5 * 1024 * 1024) // 5Mb
	private String draft;

	@ElementCollection
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@CollectionTable(name = "sh_object_draft_groups")
	@JoinColumn(name = "object_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<String> shGroups = new HashSet<>();

	@ElementCollection
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@CollectionTable(name = "sh_object_draft_users")
	@JoinColumn(name = "object_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<String> shUsers = new HashSet<>();

	private String publishStatus;

	private boolean pageAllowRegisterUser = false;

	private boolean pageAllowGuestUser = true;

	@ElementCollection
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@CollectionTable(name = "sh_object_draft_page_groups")
	@JoinColumn(name = "object_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<String> shPageGroups = new HashSet<>();

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public Set<ShPostDraftAttr> getShPostAttrRefs() {
		return shPostAttrRefs;
	}

	public void setShPostAttrRefs(Set<ShPostDraftAttr> shPostAttrRefs) {
		this.shPostAttrRefs.clear();
		if (shPostAttrRefs != null) {
			this.shPostAttrRefs.addAll(shPostAttrRefs);
		}
	}

	public Set<String> getShGroups() {
		return shGroups;
	}

	public void setShGroups(Set<String> shGroups) {
		this.shGroups = shGroups;
	}

	public String getDraft() {
		return draft;
	}

	public void setDraft(String draft) {
		this.draft = draft;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public Set<String> getShUsers() {
		return shUsers;
	}

	public void setShUsers(Set<String> shUsers) {
		this.shUsers = shUsers;
	}

	public boolean isPageAllowRegisterUser() {
		return pageAllowRegisterUser;
	}

	public void setPageAllowRegisterUser(boolean pageAllowRegisterUser) {
		this.pageAllowRegisterUser = pageAllowRegisterUser;
	}

	public boolean isPageAllowGuestUser() {
		return pageAllowGuestUser;
	}

	public void setPageAllowGuestUser(boolean pageAllowGuestUser) {
		this.pageAllowGuestUser = pageAllowGuestUser;
	}

	public Set<String> getShPageGroups() {
		return shPageGroups;
	}

	public void setShPageGroups(Set<String> shPageGroups) {
		this.shPageGroups = shPageGroups;
	}

}
