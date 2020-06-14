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
package com.viglet.shio.persistence.model.post;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shio.object.ShObjectType;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.model.site.ShSite;

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
 * The persistent class for the ShPost database table.
 * 
 * @author Alexandre Oliveira
 */
@Indexed
@Entity
@NamedQuery(name = "ShPost.findAll", query = "SELECT s FROM ShPost s")
@PrimaryKeyJoinColumn(name = "object_id")
@JsonIgnoreProperties({ "shPostAttrRefs", "shGroups", "shUsers", "shPostDraftAttrRefs", "shWorkflowTasks" })
public class ShPost extends ShObject {
	private static final long serialVersionUID = 1L;

	@Field(store = Store.YES)
	private String summary;

	@Field(store = Store.YES)
	private String title;

	// bi-directional many-to-one association to ShPostType
	@IndexedEmbedded
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "post_type_id")
	private ShPostType shPostType;

	// bi-directional many-to-one association to ShFolder
	@IndexedEmbedded
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "folder_id")
	private ShFolder shFolder;

	// bi-directional many-to-one association to ShSite
	@IndexedEmbedded
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "site_id")
	private ShSite shSite;

	// bi-directional many-to-one association to ShPostAttr
	@IndexedEmbedded
	@OneToMany(mappedBy = "shPost", orphanRemoval = true, fetch = FetchType.LAZY)
	@Cascade({ CascadeType.ALL })
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShPostAttr> shPostAttrs = new HashSet<>();

	public ShPost() {
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

	public Set<ShPostAttr> getShPostAttrs() {
		return this.shPostAttrs;
	}

	public void setShPostAttrs(Set<ShPostAttr> shPostAttrs) {
		this.shPostAttrs.clear();
		if (shPostAttrs != null) {
			this.shPostAttrs.addAll(shPostAttrs);
		}
	}

	public ShPostAttr addShPostAttr(ShPostAttr shPostAttr) {
		getShPostAttrs().add(shPostAttr);
		shPostAttr.setShPost(this);

		return shPostAttr;
	}

	public ShPostAttr removeShPostAttr(ShPostAttr shPostAttr) {
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
	

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
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
