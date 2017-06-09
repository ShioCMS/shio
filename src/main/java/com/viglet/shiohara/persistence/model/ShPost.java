package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;
import java.util.List;


/**
 * The persistent class for the ShPost database table.
 * 
 */
@Entity
@NamedQuery(name="ShPost.findAll", query="SELECT s FROM ShPost s")
public class ShPost implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String summary;

	private String title;

	//bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name="post_type_id")
	private ShPostType shpostType;

	//bi-directional many-to-one association to ShPostAttr
	@OneToMany(mappedBy="shpost")
	private List<ShPostAttr> shpostAttrs;

	//bi-directional many-to-one association to ShRegion
	@OneToMany(mappedBy="shpost")
	private List<ShRegion> shregions;

	public ShPost() {
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

	public ShPostType getShpostType() {
		return this.shpostType;
	}

	public void setShpostType(ShPostType shpostType) {
		this.shpostType = shpostType;
	}

	public List<ShPostAttr> getShpostAttrs() {
		return this.shpostAttrs;
	}

	public void setShpostAttrs(List<ShPostAttr> shpostAttrs) {
		this.shpostAttrs = shpostAttrs;
	}

	public ShPostAttr addShpostAttr(ShPostAttr shpostAttr) {
		getShpostAttrs().add(shpostAttr);
		shpostAttr.setShpost(this);

		return shpostAttr;
	}

	public ShPostAttr removeShpostAttr(ShPostAttr shpostAttr) {
		getShpostAttrs().remove(shpostAttr);
		shpostAttr.setShpost(null);

		return shpostAttr;
	}

	public List<ShRegion> getShregions() {
		return this.shregions;
	}

	public void setShregions(List<ShRegion> shregions) {
		this.shregions = shregions;
	}

	public ShRegion addShregion(ShRegion shregion) {
		getShregions().add(shregion);
		shregion.setShpost(this);

		return shregion;
	}

	public ShRegion removeShregion(ShRegion shregion) {
		getShregions().remove(shregion);
		shregion.setShpost(null);

		return shregion;
	}

}