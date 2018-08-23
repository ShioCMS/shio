package com.viglet.shiohara.persistence.model.ecommerce;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQuery(name = "ShEcomPaymentTypeDefinition.findAll", query = "SELECT eptd FROM ShEcomPaymentTypeDefinition eptd")
public class ShEcomPaymentTypeDefinition {

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String name;

	private String description;

	private Date date;

	@Column(name = "class_name")
	private String className;

	@Column(name = "setting_path")
	private String settingPath;

	// bi-directional many-to-one association to ShPost
	@OneToMany(mappedBy = "shEcomPaymentTypeDefinition", orphanRemoval = true)
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	private Set<ShEcomPaymentType> shEcomPaymentTypes = new HashSet<ShEcomPaymentType>();

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getSettingPath() {
		return settingPath;
	}

	public void setSettingPath(String settingPath) {
		this.settingPath = settingPath;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
