package com.viglet.shiohara.persistence.model.post;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;

import java.util.Date;
import java.util.List;
import java.util.Set;
import java.util.UUID;

/**
 * The persistent class for the ShPostAttr database table.
 * 
 */
@Entity
@NamedQuery(name = "ShPostAttr.findAll", query = "SELECT s FROM ShPostAttr s")
@JsonIgnoreProperties({ "shPostType", "shPost",  "shParentRelatorItem" })
public class ShPostAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Temporal(TemporalType.TIMESTAMP)
	@Column(name = "date_value")
	private Date dateValue;

	@Column(name = "int_value")
	private int intValue;

	@Field
	@Column(name = "str_value", length = 5 * 1024 * 1024) // 5Mb
	private String strValue;

	@ManyToMany(cascade = CascadeType.ALL, fetch = FetchType.EAGER)
	@JoinTable(name = "object_post_attr", joinColumns = @JoinColumn(name = "post_attr_id", referencedColumnName = "id"), inverseJoinColumns = @JoinColumn(name = "object_id", referencedColumnName = "id"))
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShObject> referenceObjects;

	private int type;

	// bi-directional many-to-one association to ShRelatorItem
	@OneToMany(mappedBy = "shParentPostAttr")
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	private List<ShRelatorItem> shChildrenRelatorItems;

	// bi-directional many-to-one association to ShPost
	@ManyToOne
	@JoinColumn(name = "post_id")
	private ShPost shPost;

	// bi-directional many-to-one association to ShPostTypeAttr
	@ManyToOne
	@JoinColumn(name = "post_type_attr_id")
	private ShPostTypeAttr shPostTypeAttr;

	// bi-directional many-to-one association to ShPost
	@ManyToOne
	@JoinColumn(name = "post_attr_id")
	private ShRelatorItem shParentRelatorItem;
		
	@Transient
	private UUID shPostTypeAttrId;

	public Set<ShObject> getReferenceObjects() {
		return referenceObjects;
	}

	public void setReferenceObjects(Set<ShObject> referenceObjects) {
		this.referenceObjects = referenceObjects;
	}

	public UUID getShPostTypeAttrId() {
		if (shPostTypeAttr.getId() != null) {
			shPostTypeAttrId = shPostTypeAttr.getId();
		}
		return shPostTypeAttrId;
	}

	public void setShPostTypeAttrId(UUID shPostTypeAttrId) {
		this.shPostTypeAttrId = shPostTypeAttrId;
	}

	public ShPostAttr() {
	}

	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
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

	public ShPostTypeAttr getShPostTypeAttr() {
		return this.shPostTypeAttr;
	}

	public void setShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		this.shPostTypeAttr = shPostTypeAttr;
	}

	public List<ShRelatorItem> getShChildrenRelatorItems() {
		return shChildrenRelatorItems;
	}

	public void setShChildrenRelatorItems(List<ShRelatorItem> shChildrenRelatorItems) {
		this.shChildrenRelatorItems = shChildrenRelatorItems;
	}

	public ShRelatorItem getShParentRelatorItem() {
		return shParentRelatorItem;
	}

	public void setShParentRelatorItem(ShRelatorItem shParentRelatorItem) {
		this.shParentRelatorItem = shParentRelatorItem;
	}


	
}
