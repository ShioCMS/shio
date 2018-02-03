package com.viglet.shiohara.persistence.model.post.type;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.region.ShRegion;

import java.util.Date;
import java.util.List;
import java.util.UUID;


/**
 * The persistent class for the ShPostType database table.
 * 
 */
@Entity
@NamedQuery(name="ShPostType.findAll", query="SELECT s FROM ShPostType s")
@JsonIgnoreProperties({ "shPosts", "shPostAttrs" })
public class ShPostType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(
			name = "UUID",
			strategy = "org.hibernate.id.UUIDGenerator"
		)
	@GeneratedValue(generator = "UUID")
	
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String description;

	private String name;

	private String title;
	
	private byte system;

	//bi-directional many-to-one association to ShPost
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shPostType", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShPost> shPosts;

	//bi-directional many-to-one association to ShPostAttr
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shPostType", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShPostAttr> shPostAttrs;

	//bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shPostType", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShPostTypeAttr> shPostTypeAttrs;

	//bi-directional many-to-one association to ShRegion
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shPostType", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShRegion> shRegions;

	public ShPostType() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public List<ShPostAttr> getShPostAttrs() {
		return this.shPostAttrs;
	}

	public void setShPostAttrs(List<ShPostAttr> shPostAttrs) {
		this.shPostAttrs = shPostAttrs;
	}

	public ShPostAttr addShPostAttr(ShPostAttr shPostAttr) {
		getShPostAttrs().add(shPostAttr);
		shPostAttr.setShPostType(this);

		return shPostAttr;
	}

	public ShPostAttr removeShPostAttr(ShPostAttr shPostAttr) {
		getShPostAttrs().remove(shPostAttr);
		shPostAttr.setShPostType(null);

		return shPostAttr;
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

	public List<ShRegion> getShRegions() {
		return this.shRegions;
	}

	public void setShRegions(List<ShRegion> shRegions) {
		this.shRegions = shRegions;
	}

	public ShRegion addShregion(ShRegion shRegion) {
		getShRegions().add(shRegion);
		shRegion.setShpostType(this);

		return shRegion;
	}

	public ShRegion removeShregion(ShRegion shRegion) {
		getShRegions().remove(shRegion);
		shRegion.setShpostType(null);

		return shRegion;
	}

	public byte getSystem() {
		return system;
	}

	public void setSystem(byte system) {
		this.system = system;
	}



}