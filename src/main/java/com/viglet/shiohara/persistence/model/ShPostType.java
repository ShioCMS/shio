package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ShPostType database table.
 * 
 */
@Entity
@NamedQuery(name="ShPostType.findAll", query="SELECT s FROM ShPostType s")
public class ShPostType implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String description;

	private String name;

	private String title;

	//bi-directional many-to-one association to ShPost
	@OneToMany(mappedBy="shpostType")
	private List<ShPost> shposts;

	//bi-directional many-to-one association to ShPostAttr
	@OneToMany(mappedBy="shpostType")
	private List<ShPostAttr> shpostAttrs;

	//bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(mappedBy="shpostType")
	private List<ShPostTypeAttr> shpostTypeAttrs;

	//bi-directional many-to-one association to ShRegion
	@OneToMany(mappedBy="shpostType")
	private List<ShRegion> shregions;

	public ShPostType() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
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

	public List<ShPost> getShposts() {
		return this.shposts;
	}

	public void setShposts(List<ShPost> shposts) {
		this.shposts = shposts;
	}

	public ShPost addShpost(ShPost shpost) {
		getShposts().add(shpost);
		shpost.setShpostType(this);

		return shpost;
	}

	public ShPost removeShpost(ShPost shpost) {
		getShposts().remove(shpost);
		shpost.setShpostType(null);

		return shpost;
	}

	public List<ShPostAttr> getShpostAttrs() {
		return this.shpostAttrs;
	}

	public void setShpostAttrs(List<ShPostAttr> shpostAttrs) {
		this.shpostAttrs = shpostAttrs;
	}

	public ShPostAttr addShpostAttr(ShPostAttr shpostAttr) {
		getShpostAttrs().add(shpostAttr);
		shpostAttr.setShpostType(this);

		return shpostAttr;
	}

	public ShPostAttr removeShpostAttr(ShPostAttr shpostAttr) {
		getShpostAttrs().remove(shpostAttr);
		shpostAttr.setShpostType(null);

		return shpostAttr;
	}

	public List<ShPostTypeAttr> getShpostTypeAttrs() {
		return this.shpostTypeAttrs;
	}

	public void setShpostTypeAttrs(List<ShPostTypeAttr> shpostTypeAttrs) {
		this.shpostTypeAttrs = shpostTypeAttrs;
	}

	public ShPostTypeAttr addShpostTypeAttr(ShPostTypeAttr shpostTypeAttr) {
		getShpostTypeAttrs().add(shpostTypeAttr);
		shpostTypeAttr.setShpostType(this);

		return shpostTypeAttr;
	}

	public ShPostTypeAttr removeShpostTypeAttr(ShPostTypeAttr shpostTypeAttr) {
		getShpostTypeAttrs().remove(shpostTypeAttr);
		shpostTypeAttr.setShpostType(null);

		return shpostTypeAttr;
	}

	public List<ShRegion> getShregions() {
		return this.shregions;
	}

	public void setShregions(List<ShRegion> shregions) {
		this.shregions = shregions;
	}

	public ShRegion addShregion(ShRegion shregion) {
		getShregions().add(shregion);
		shregion.setShpostType(this);

		return shregion;
	}

	public ShRegion removeShregion(ShRegion shregion) {
		getShregions().remove(shregion);
		shregion.setShpostType(null);

		return shregion;
	}

}