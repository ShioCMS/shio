package com.viglet.shiohara.persistence.model.ecommerce;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;

@Entity
@NamedQuery(name = "ShEcomOrder.findAll", query = "SELECT eo FROM ShEcomOrder eo")
public class ShEcomOrder {
	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String description;

	@ManyToOne
	@JoinColumn(name = "payment_type_id")
	private ShEcomPaymentType shEcomPaymentType;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public ShEcomPaymentType getShEcomPaymentType() {
		return shEcomPaymentType;
	}

	public void setShEcomPaymentType(ShEcomPaymentType shEcomPaymentType) {
		this.shEcomPaymentType = shEcomPaymentType;
	}
	
	

}
