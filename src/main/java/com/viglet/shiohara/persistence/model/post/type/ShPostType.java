/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.persistence.model.post.type;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.post.type.ShSystemPostType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.PrimaryKeyJoinColumn;

/**
 * The persistent class for the ShPostType database table.
 * 
 */
@Entity
@NamedQuery(name = "ShPostType.findAll", query = "SELECT s FROM ShPostType s")
@JsonIgnoreProperties({ "shPosts", "shPostAttrs","shPostAttrRefs" })
@PrimaryKeyJoinColumn(name = "object_id")
public class ShPostType extends ShObject {
	private static final long serialVersionUID = 1L;

	private String description;

	@Field
	private String name;

	private String title;

	private byte system;

	// bi-directional many-to-one association to ShPost
	@OneToMany(mappedBy = "shPostType", orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	private Set<ShPost> shPosts = new HashSet<ShPost>();

	// bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(mappedBy = "shPostType", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	@JsonView({ ShJsonView.ShJsonViewPostType.class })
	private Set<ShPostTypeAttr> shPostTypeAttrs = new HashSet<ShPostTypeAttr>();

	public ShPostType() {
		this.setObjectType(ShObjectType.POST_TYPE);
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public void setName(ShSystemPostType shSystemPostType) {
		this.name = shSystemPostType.toString();
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public Set<ShPost> getShPosts() {
		return this.shPosts;
	}

	public void setShPosts(Set<ShPost> shPosts) {
		this.shPosts.clear();
		if (shPosts != null) {
			this.shPosts.addAll(shPosts);
		}
	}

	public ShPost addShPost(ShPost shPost) {
		getShPosts().add(shPost);
		shPost.setShPostType(this);

		return shPost;
	}

	public ShPost removeShPost(ShPost shPost) {
		getShPosts().remove(shPost);
		shPost.setShPostType(null);

		return shPost;
	}

	public Set<ShPostTypeAttr> getShPostTypeAttrs() {
		return this.shPostTypeAttrs;
	}

	public void setShPostTypeAttrs(Set<ShPostTypeAttr> shPostTypeAttrs) {
		this.shPostTypeAttrs.clear();
		if (shPostTypeAttrs != null) {
			this.shPostTypeAttrs.addAll(shPostTypeAttrs);
		}
	}

	public ShPostTypeAttr addShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		getShPostTypeAttrs().add(shPostTypeAttr);
		shPostTypeAttr.setShPostType(this);

		return shPostTypeAttr;
	}

	public ShPostTypeAttr removeShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		getShPostTypeAttrs().remove(shPostTypeAttr);
		shPostTypeAttr.setShPostType(null);

		return shPostTypeAttr;
	}

	public byte getSystem() {
		return system;
	}

	public void setSystem(byte system) {
		this.system = system;
	}
	
	@Override
	public String getObjectType() {
		return ShObjectType.POST_TYPE;
	}

	@Override
	public void setObjectType(String objectType) {		
		super.setObjectType(ShObjectType.POST_TYPE);
	}
}
