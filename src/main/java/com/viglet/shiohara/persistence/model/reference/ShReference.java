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

import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;


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
	@JoinColumn(name = "global_from_id" , nullable = false)
	private ShGlobalId shGlobalFromId;
	
	@ManyToOne
	@JoinColumn(name = "global_to_id", nullable = false)
	private ShGlobalId shGlobalToId;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ShGlobalId getShGlobalFromId() {
		return shGlobalFromId;
	}

	public void setShGlobalFromId(ShGlobalId shGlobalFromId) {
		this.shGlobalFromId = shGlobalFromId;
	}

	public ShGlobalId getShGlobalToId() {
		return shGlobalToId;
	}

	public void setShGlobalToId(ShGlobalId shGlobalToId) {
		this.shGlobalToId = shGlobalToId;
	}


	 
	
}
