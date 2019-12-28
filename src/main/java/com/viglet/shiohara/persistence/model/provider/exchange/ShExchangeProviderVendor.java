/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.persistence.model.provider.exchange;

import java.util.List;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

/**
 * The persistent class for the ShExchangeProviderVendor database table.
 * 
 */
@Entity
@NamedQuery(name = "ShExchangeProviderVendor.findAll", query = "SELECT pv FROM ShExchangeProviderVendor pv")
@JsonIgnoreProperties({ "instances" })
public class ShExchangeProviderVendor {

	@Id
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String name;

	@Column(name = "class_name")
	private String className;

	private String description;

	@Column(name = "implementation_code")
	private String implementationCode;

	@OneToMany(mappedBy = "vendor")
	private List<ShExchangeProviderInstance> instances;

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

	public String getClassName() {
		return className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImplementationCode() {
		return implementationCode;
	}

	public void setImplementationCode(String implementationCode) {
		this.implementationCode = implementationCode;
	}

	public List<ShExchangeProviderInstance> getInstances() {
		return instances;
	}

	public void setInstances(List<ShExchangeProviderInstance> instances) {
		this.instances = instances;
	}

}
