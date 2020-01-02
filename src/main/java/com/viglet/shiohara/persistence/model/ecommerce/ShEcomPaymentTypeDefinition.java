/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
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

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the ShEcomPaymentTypeDefinition database table.
 * 
 * @author Alexandre Oliveira
 */
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
	
	@Column(name = "form_path")
	private String formPath;

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

	public String getFormPath() {
		return formPath;
	}

	public void setFormPath(String formPath) {
		this.formPath = formPath;
	}

}
