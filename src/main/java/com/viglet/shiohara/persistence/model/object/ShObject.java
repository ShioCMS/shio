package com.viglet.shiohara.persistence.model.object;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
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
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Store;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.history.ShHistory;
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
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	private ShGlobalId shGlobalId;

	@ManyToMany(mappedBy = "referenceObjects")
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShPostAttr> shPostAttrRefs;

	// bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(mappedBy = "shObject")
	private List<ShHistory> shHistories;

	@Temporal(TemporalType.TIMESTAMP)
	@Field(store = Store.NO)
	private Date date;

	private String owner;

	private String furl;

	private String modifier;

	private String publisher;

	private Date modifiedDate;

	private Date publicationDate;

	private int position;

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

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public String getFurl() {
		return furl;
	}

	public void setFurl(String furl) {
		this.furl = furl;
	}

	public Date getDate() {
		return this.date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getModifier() {
		return modifier;
	}

	public void setModifier(String modifier) {
		this.modifier = modifier;
	}

	public String getPublisher() {
		return publisher;
	}

	public void setPublisher(String publisher) {
		this.publisher = publisher;
	}

	public Date getModifiedDate() {
		return modifiedDate;
	}

	public void setModifiedDate(Date modifiedDate) {
		this.modifiedDate = modifiedDate;
	}

	public Date getPublicationDate() {
		return publicationDate;
	}

	public void setPublicationDate(Date publicationDate) {
		this.publicationDate = publicationDate;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

}
