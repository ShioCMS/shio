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
package com.viglet.shiohara.persistence.model.provider.exchange;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the ShExchangeProviderInstance database table.
 * 
 * @author Alexandre Oliveira
 */
@Entity
@NamedQuery(name = "ShExchangeProviderInstance.findAll", query = "SELECT pi FROM ShExchangeProviderInstance pi")
public class ShExchangeProviderInstance {

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String name;

	private String description;

	private int position;

	@ManyToOne
	@JoinColumn(name = "vendor_id")
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	private ShExchangeProviderVendor vendor;

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

	public ShExchangeProviderVendor getVendor() {
		return vendor;
	}

	public void setVendor(ShExchangeProviderVendor vendor) {
		this.vendor = vendor;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
