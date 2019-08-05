/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.persistence.model.post;

import java.io.Serializable;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItemDraft;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

/**
 * The persistent class for the ShPostAttr database table.
 * 
 */
@Entity
@NamedQuery(name = "ShPostDraftAttr.findAll", query = "SELECT pda FROM ShPostDraftAttr pda")
@JsonIgnoreProperties({ "shPostType", "shPost", "shParentRelatorItem", "tab" })
public class ShPostDraftAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_value")
	private Date dateValue;

	@Column(name = "int_value")
	private int intValue;

	@Column(name = "str_value", length = 5 * 1024 * 1024) // 5Mb
	private String strValue;

	@ElementCollection
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@CollectionTable(name = "sh_post_draft_attr_array_value")
	@JoinColumn(name = "post_attr_id")
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<String> arrayValue = new HashSet<String>();

	// bi-directional many-to-one association to shObject
	@ManyToOne
	@JoinColumn(name = "object_id")
	// Void Cyclic Reference
	@JsonIgnoreProperties({ "shPostAttrs" })
	private ShObject referenceObject;

	private int type;

	// bi-directional many-to-one association to ShRelatorItem
	@OneToMany(mappedBy = "shParentPostAttr", orphanRemoval = true, fetch = FetchType.LAZY)
	@Cascade({ CascadeType.ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShRelatorItemDraft> shChildrenRelatorItems = new HashSet<ShRelatorItemDraft>();

	// bi-directional many-to-one association to ShPost
	@ManyToOne(fetch = FetchType.LAZY) // (cascade = {CascadeType.ALL})
	@JoinColumn(name = "post_id")
	private ShPostDraft shPost;

	// bi-directional many-to-one association to ShPostTypeAttr
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_type_attr_id")
	private ShPostTypeAttr shPostTypeAttr;

	// bi-directional many-to-one association to ShPost
	// TODO: Change name of Column
	@ManyToOne(fetch = FetchType.LAZY) // (cascade = {CascadeType.ALL})
	@JoinColumn(name = "post_attr_id")
	private ShRelatorItemDraft shParentRelatorItem;

	public ShObject getReferenceObject() {
		return referenceObject;
	}

	public void setReferenceObject(ShObject referenceObject) {
		this.referenceObject = referenceObject;
	}

	public ShPostDraftAttr() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDateValue() {
		return this.dateValue;
	}

	public void setDateValue(Date dateValue) {
		this.dateValue = dateValue;
	}

	public int getIntValue() {
		return this.intValue;
	}

	public void setIntValue(int intValue) {
		this.intValue = intValue;
	}

	public String getStrValue() {
		return this.strValue;
	}

	public void setStrValue(String strValue) {
		this.strValue = strValue;
	}

	public Set<String> getArrayValue() {
		return arrayValue;
	}

	public void setArrayValue(Set<String> arrayValue) {
		this.arrayValue.clear();
		if (arrayValue != null) {
			this.arrayValue.addAll(arrayValue);
		}
	}

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ShPostDraft getShPost() {
		return this.shPost;
	}

	public void setShPost(ShPostDraft shPost) {
		this.shPost = shPost;
	}

	public ShPostTypeAttr getShPostTypeAttr() {
		return this.shPostTypeAttr;
	}

	public void setShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		this.shPostTypeAttr = shPostTypeAttr;
	}

	public Set<ShRelatorItemDraft> getShChildrenRelatorItems() {
		return shChildrenRelatorItems;
	}

	public void setShChildrenRelatorItems(Set<ShRelatorItemDraft> shChildrenRelatorItems) {
		this.shChildrenRelatorItems.clear();
		if (shChildrenRelatorItems != null) {
			this.shChildrenRelatorItems.addAll(shChildrenRelatorItems);
		}
	}

	public ShRelatorItemDraft getShParentRelatorItem() {
		return shParentRelatorItem;
	}

	public void setShParentRelatorItem(ShRelatorItemDraft shParentRelatorItem) {
		this.shParentRelatorItem = shParentRelatorItem;
	}

}
