package com.viglet.shiohara.persistence.model.reference;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;

import com.viglet.shiohara.persistence.model.object.ShObject;

@Entity
@NamedQuery(name = "ShReference.findAll", query = "SELECT r FROM ShReference r")
public class ShReference implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;
	
	@ManyToOne
	@JoinColumn(name = "object_from" , nullable = false)
	private ShObject shObjectFrom;
	
	@ManyToOne
	@JoinColumn(name = "object_to", nullable = false)
	private ShObject shObjectTo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ShObject getShObjectFrom() {
		return shObjectFrom;
	}

	public void setShObjectFrom(ShObject shObjectFrom) {
		this.shObjectFrom = shObjectFrom;
	}

	public ShObject getShObjectTo() {
		return shObjectTo;
	}

	public void setShObjectTo(ShObject shObjectTo) {
		this.shObjectTo = shObjectTo;
	}

	
}
