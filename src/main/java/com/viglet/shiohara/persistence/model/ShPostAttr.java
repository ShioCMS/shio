package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.Date;


/**
 * The persistent class for the ShPostAttr database table.
 * 
 */
@Entity
@NamedQuery(name="ShPostAttr.findAll", query="SELECT s FROM ShPostAttr s")
public class ShPostAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name="date_value")
	private Date dateValue;

	@Column(name="int_value")
	private int intValue;

	@Lob
	@Column(name="str_value")
	private String strValue;

	private int type;

	//bi-directional many-to-one association to ShPost
	@ManyToOne
	@JoinColumn(name="post_id")
	private ShPost shpost;

	//bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name="post_type_id")
	private ShPostType shpostType;

	//bi-directional many-to-one association to ShPostTypeAttr
	@ManyToOne
	@JoinColumn(name="post_type_attr_id")
	private ShPostTypeAttr shpostTypeAttr;

	public ShPostAttr() {
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

	public int getType() {
		return this.type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public ShPost getShpost() {
		return this.shpost;
	}

	public void setShpost(ShPost shpost) {
		this.shpost = shpost;
	}

	public ShPostType getShpostType() {
		return this.shpostType;
	}

	public void setShpostType(ShPostType shpostType) {
		this.shpostType = shpostType;
	}

	public ShPostTypeAttr getShpostTypeAttr() {
		return this.shpostTypeAttr;
	}

	public void setShpostTypeAttr(ShPostTypeAttr shpostTypeAttr) {
		this.shpostTypeAttr = shpostTypeAttr;
	}

}