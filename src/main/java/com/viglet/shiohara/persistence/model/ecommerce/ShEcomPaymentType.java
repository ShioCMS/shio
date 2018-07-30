package com.viglet.shiohara.persistence.model.ecommerce;

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
@NamedQuery(name = "ShEcomPaymentType.findAll", query = "SELECT ept FROM ShEcomPaymentType ept")
public class ShEcomPaymentType {

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

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

	public Set<ShEcomOrder> getShEcomOrders() {
		return shEcomOrders;
	}

	public void setShEcomOrders(Set<ShEcomOrder> shEcomOrders) {
		this.shEcomOrders = shEcomOrders;
	}

}
