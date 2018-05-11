package com.viglet.shiohara.persistence.model.post.relator;

import java.io.Serializable;
import java.util.List;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

/**
 * The persistent class for the ShPostAttr database table.
 * 
 */
@Entity
@NamedQuery(name = "ShRelatorItem.findAll", query = "SELECT ri FROM ShRelatorItem ri")
@JsonIgnoreProperties({ "shPostAttr"})
public class ShRelatorItem implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	// bi-directional many-to-one association to ShPostAttr
	@OneToMany(mappedBy = "shRelatorItem")
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	private List<ShPostAttr> shPostAttrs;

	// bi-directional many-to-one association to ShPost
	@ManyToOne
	@JoinColumn(name = "post_attr_id")
	private ShPostAttr shPostAttr;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public List<ShPostAttr> getShPostAttrs() {
		return shPostAttrs;
	}

	public void setShPostAttrs(List<ShPostAttr> shPostAttrs) {
		this.shPostAttrs = shPostAttrs;
	}

	public ShPostAttr getShPostAttr() {
		return shPostAttr;
	}

	public void setShPostAttr(ShPostAttr shPostAttr) {
		this.shPostAttr = shPostAttr;
	}

}
