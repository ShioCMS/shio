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
	private String id;

	private String owner;

	private Date date;

	private String description;

	private String shObject;

	private String shSite;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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

	public String getShObject() {
		return shObject;
	}

	public void setShObject(String shObject) {
		this.shObject = shObject;
	}

	public String getShSite() {
		return shSite;
	}

	public void setShSite(String shSite) {
		this.shSite = shSite;
	}
	

}
