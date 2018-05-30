package com.viglet.shiohara.persistence.model.post.relator;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

/**
 * The persistent class for the ShRelatorItem database table.
 * 
 */
@Entity
@NamedQuery(name = "ShRelatorItem.findAll", query = "SELECT ri FROM ShRelatorItem ri")
@JsonIgnoreProperties({ "shPostAttr", "shParentPostAttr" })
public class ShRelatorItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String title;
	
	private String summary;
	
	private int ordinal;
	
	// bi-directional many-to-one association to ShPostAttr
	@OneToMany(mappedBy = "shParentRelatorItem", orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@Cascade({CascadeType.ALL})
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShPostAttr> shChildrenPostAttrs = new HashSet<ShPostAttr>();

	// bi-directional many-to-one association to ShPost
	@ManyToOne // (cascade = {CascadeType.ALL})
	@JoinColumn(name = "post_attr_id")
	private ShPostAttr shParentPostAttr;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Set<ShPostAttr> getShChildrenPostAttrs() {
		return shChildrenPostAttrs;
	}

	public void setShChildrenPostAttrs(Set<ShPostAttr> shChildrenPostAttrs) {
		this.shChildrenPostAttrs.clear();
		if (shChildrenPostAttrs != null) {
			this.shChildrenPostAttrs.addAll(shChildrenPostAttrs);
		}
	}

	public ShPostAttr getShParentPostAttr() {
		return shParentPostAttr;
	}

	public void setShParentPostAttr(ShPostAttr shParentPostAttr) {
		this.shParentPostAttr = shParentPostAttr;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public int getOrdinal() {
		return ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

}
