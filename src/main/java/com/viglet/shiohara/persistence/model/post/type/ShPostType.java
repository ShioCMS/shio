package com.viglet.shiohara.persistence.model.post.type;

import javax.persistence.*;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;

import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ShPostType database table.
 * 
 */
@Entity
@NamedQuery(name="ShPostType.findAll", query="SELECT s FROM ShPostType s")
@JsonIgnoreProperties({ "shPosts", "shPostAttrs" })
public class ShPostType extends ShObject {
	private static final long serialVersionUID = 1L;
	
	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String description;

	private String name;

	private String title;
	
	private byte system;

	//bi-directional many-to-one association to ShPost
	@OneToMany(mappedBy = "shPostType")
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShPost> shPosts;

	//bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(mappedBy = "shPostType")
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@JsonView({ShJsonView.ShJsonViewPostType.class})
	private List<ShPostTypeAttr> shPostTypeAttrs;


	public ShPostType() {
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
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

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public List<ShPost> getShPosts() {
		return this.shPosts;
	}

	public void setShPosts(List<ShPost> shPosts) {
		this.shPosts = shPosts;
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

	public List<ShPostTypeAttr> getShPostTypeAttrs() {
		return this.shPostTypeAttrs;
	}

	public void setShPostTypeAttrs(List<ShPostTypeAttr> shPostTypeAttrs) {
		this.shPostTypeAttrs = shPostTypeAttrs;
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



}
