package com.viglet.shiohara.persistence.model.reference;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;
import javax.persistence.NamedQuery;


@Entity
@NamedQuery(name = "ShReference.findAll", query = "SELECT r FROM ShReference r")
public class ShReference {
	
	 @EmbeddedId ShReferenceId id;

	public ShReferenceId getId() {
		return id;
	}

	public void setId(ShReferenceId id) {
		this.id = id;
	}
	 
	 
	
}
