package com.viglet.shiohara.persistence.model.globalid;

import java.util.List;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

@Entity
@NamedQuery(name = "ShGlobalId.findAll", query = "SELECT g FROM ShGlobalId g")
public class ShGlobalId {

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	private UUID objectId;

	@Column(name = "type", length = 20)
	private String type;

	// bi-directional many-to-one association to ShPostAttr
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shGlobalId", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShObject> shObjects;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public UUID getObjectId() {
		return objectId;
	}

	public void setObjectId(UUID objectId) {
		this.objectId = objectId;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ShObject> getShObjects() {
		return shObjects;
	}

	public void setShObjects(List<ShObject> shObjects) {
		this.shObjects = shObjects;
	}

}
