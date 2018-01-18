package com.viglet.shiohara.persistence.model.post;

import java.io.Serializable;
import javax.persistence.*;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

import java.util.Date;

/**
 * The persistent class for the ShPostAttr database table.
 * 
 */
@Entity
@NamedQuery(name = "ShPostAttr.findAll", query = "SELECT s FROM ShPostAttr s")
@JsonIgnoreProperties({ "shPostType", "shPost" })
public class ShPostAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_value")
	private Date dateValue;

	@Column(name = "int_value")
	private int intValue;

	@Lob
	@Column(name = "str_value", length = 20971520)
	private String strValue;

	private int type;

	// bi-directional many-to-one association to ShPost
	@ManyToOne
	@JoinColumn(name = "post_id")
	private ShPost shPost;

	// bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name = "post_type_id")
	private ShPostType shPostType;

	// bi-directional many-to-one association to ShPostTypeAttr
	@ManyToOne
	@JoinColumn(name = "post_type_attr_id")
	private ShPostTypeAttr shPostTypeAttr;

	@Transient
	private int shPostTypeAttrId;

	public int getShPostTypeAttrId() {
		if (shPostTypeAttr.getId() >= 0) {
			shPostTypeAttrId = shPostTypeAttr.getId();
		}
		return shPostTypeAttrId;
	}

	public void setShPostTypeAttrId(int shPostTypeAttrId) {
		this.shPostTypeAttrId = shPostTypeAttrId;
	}

	public ShPostAttr() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public ShPost getShPost() {
		return this.shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public ShPostType getShPostType() {
		return this.shPostType;
	}

	public void setShPostType(ShPostType shPostType) {
		this.shPostType = shPostType;
	}

	public ShPostTypeAttr getShPostTypeAttr() {
		return this.shPostTypeAttr;
	}

	public void setShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		this.shPostTypeAttr = shPostTypeAttr;
	}

}