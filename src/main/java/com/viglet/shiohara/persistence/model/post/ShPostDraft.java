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
package com.viglet.shiohara.persistence.model.post;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObjectDraft;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * The persistent class for the ShPostDraft database table.
 * 
 * @author Alexandre Oliveira
 */
@Entity
@NamedQuery(name = "ShPostDraft.findAll", query = "SELECT pd FROM ShPostDraft pd")
@PrimaryKeyJoinColumn(name = "object_id")
@JsonIgnoreProperties({ "shPostAttrRefs", "shGroups", "shUsers", "shPostDraftAttrRefs" })
public class ShPostDraft extends ShObjectDraft {
	private static final long serialVersionUID = 1L;

	private String summary;

	private String title;

	// bi-directional many-to-one association to ShPostType
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_type_id")
	private ShPostType shPostType;

	// bi-directional many-to-one association to ShFolder
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folder_id")
	private ShFolder shFolder;

	// bi-directional many-to-one association to ShPostAttr
	@OneToMany(mappedBy = "shPost", orphanRemoval = true, fetch = FetchType.LAZY)
	@Cascade({CascadeType.ALL})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShPostDraftAttr> shPostAttrs = new HashSet<ShPostDraftAttr>();
	
	public ShPostDraft() {
		this.setObjectType(ShObjectType.POST);
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ShPostType getShPostType() {
		return this.shPostType;
	}

	public void setShPostType(ShPostType shPostType) {
		this.shPostType = shPostType;
	}

	public Set<ShPostDraftAttr> getShPostAttrs() {
		return this.shPostAttrs;
	}

	public void setShPostAttrs(Set<ShPostDraftAttr> shPostAttrs) {
		this.shPostAttrs.clear();
		if (shPostAttrs != null) {
			this.shPostAttrs.addAll(shPostAttrs);
		}
	}

	public ShPostDraftAttr addShPostAttr(ShPostDraftAttr shPostAttr) {
		getShPostAttrs().add(shPostAttr);
		shPostAttr.setShPost(this);

		return shPostAttr;
	}

	public ShPostDraftAttr removeShPostAttr(ShPostDraftAttr shPostAttr) {
		getShPostAttrs().remove(shPostAttr);
		shPostAttr.setShPost(null);

		return shPostAttr;
	}

	public ShFolder getShFolder() {
		return shFolder;
	}

	public void setShFolder(ShFolder shFolder) {
		this.shFolder = shFolder;
	}

	@Override
	public String getObjectType() {
		return ShObjectType.POST;
	}

	@Override
	public void setObjectType(String objectType) {		
		super.setObjectType(ShObjectType.POST);
	}	
}
