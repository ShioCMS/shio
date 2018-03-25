package com.viglet.shiohara.persistence.model.object;

import java.io.Serializable;
import java.util.Set;
import java.util.UUID;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.ManyToMany;
import javax.persistence.NamedQuery;
import javax.persistence.OneToOne;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

@Entity
@Inheritance(strategy = InheritanceType.TABLE_PER_CLASS)
@NamedQuery(name = "ShObject.findAll", query = "SELECT o FROM ShObject o")
public class ShObject implements Serializable {

	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@OneToOne(mappedBy = "shObject", cascade = CascadeType.ALL, fetch = FetchType.LAZY, optional = false)
	@JsonView({ShJsonView.ShJsonViewObject.class})
	private ShGlobalId shGlobalId;

	@ManyToMany(mappedBy = "referenceObjects")
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShPostAttr> shPostAttrRefs;

	/*
	 * public Set<ShPostAttr> getShPostAttrRefs() { return this.shPostAttrRefs; }
	 * 
	 * public void setShPostAttrRefs(Set<ShPostAttr> shPostAttrs) {
	 * this.shPostAttrRefs = shPostAttrs; }
	 */
	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public ShGlobalId getShGlobalId() {
		return shGlobalId;
	}

	public void setShGlobalId(ShGlobalId shGlobalId) {
		this.shGlobalId = shGlobalId;
	}

}
