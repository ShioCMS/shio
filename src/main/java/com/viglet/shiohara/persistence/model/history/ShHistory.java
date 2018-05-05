package com.viglet.shiohara.persistence.model.history;

import java.io.Serializable;
import java.util.Date;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.site.ShSite;

/**
 * The persistent class for the ShWidget database table.
 * 
 */
@Entity
@NamedQuery(name = "ShHistory.findAll", query = "SELECT h FROM ShHistory h")
public class ShHistory implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	private String owner;

	private Date date;

	private String description;

	// bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name = "object_id")
	private ShObject shObject;

	@ManyToOne
	@JoinColumn(name = "site_id")
	private ShSite shSite;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ShObject getShObject() {
		return shObject;
	}

	public void setShObject(ShObject shObject) {
		this.shObject = shObject;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

}
