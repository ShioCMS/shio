package com.viglet.shiohara.persistence.model.globalid;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.object.ShObject;

@Entity
@NamedQuery(name = "ShGlobalId.findAll", query = "SELECT g FROM ShGlobalId g")
public class ShGlobalId implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	@Column(name = "type", length = 20)
	private ShObjectType type;
	
	@OneToOne(mappedBy = "shGlobalId", fetch = FetchType.LAZY, optional = false)
	@JsonView({ShJsonView.ShJsonViewReference.class})
	private ShObject shObject;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ShObjectType getType() {
		return type;
	}

	public void setType(ShObjectType type) {
		this.type = type;
	}

	public ShObject getShObject() {
		return shObject;
	}

	public void setShObject(ShObject shObject) {
		this.shObject = shObject;
	}

}
