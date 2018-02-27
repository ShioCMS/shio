package com.viglet.shiohara.persistence.model.object;
import java.io.Serializable;
import java.util.UUID;

import javax.persistence.Column;
import javax.persistence.DiscriminatorColumn;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;

import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;

@Entity
@Inheritance(strategy = InheritanceType.JOINED)
@NamedQuery(name = "ShObject.findAll", query = "SELECT o FROM ShObject o")
public class ShObject implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;
	
	// bi-directional many-to-one association to ShPost
		@ManyToOne
		@JoinColumn(name = "global_id")
		private ShGlobalId shGlobalId;
		
	public UUID getId() {
		return this.id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

}