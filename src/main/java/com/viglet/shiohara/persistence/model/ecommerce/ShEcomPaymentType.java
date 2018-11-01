/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQuery(name = "ShEcomPaymentType.findAll", query = "SELECT ept FROM ShEcomPaymentType ept")
public class ShEcomPaymentType {

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String name;

	private String description;

	private Date date;

	private String settings;

	@ManyToOne
	@JoinColumn(name = "payment_type_definition_id")
	private ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition;

	// bi-directional many-to-one association to ShFolder
	@OneToMany(mappedBy = "shEcomPaymentType", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShEcomOrder> shEcomOrders = new HashSet<ShEcomOrder>();
	
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

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSettings() {
		return settings;
	}

	public void setSettings(String settings) {
		this.settings = settings;
	}

	public ShEcomPaymentTypeDefinition getShEcomPaymentTypeDefinition() {
		return shEcomPaymentTypeDefinition;
	}

	public void setShEcomPaymentTypeDefinition(ShEcomPaymentTypeDefinition shEcomPaymentTypeDefinition) {
		this.shEcomPaymentTypeDefinition = shEcomPaymentTypeDefinition;
	}

	public Set<ShEcomOrder> getShEcomOrders() {
		return shEcomOrders;
	}

	public void setShEcomOrders(Set<ShEcomOrder> shEcomOrders) {
		this.shEcomOrders = shEcomOrders;
	}

}
