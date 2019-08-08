/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.persistence.model.reference;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.object.ShObjectDraft;

@Entity
@NamedQuery(name = "ShReferenceDraft.findAll", query = "SELECT r FROM ShReferenceDraft r")
public class ShReferenceDraft implements Serializable{
	
	private static final long serialVersionUID = 1L;
	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")
	@Column(name = "id", updatable = false, nullable = false)
	private String id;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "object_from" , nullable = false)
	private ShObjectDraft shObjectFrom;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "object_to", nullable = false)
	private ShObject shObjectTo;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public ShObjectDraft getShObjectFrom() {
		return shObjectFrom;
	}

	public void setShObjectFrom(ShObjectDraft shObjectFrom) {
		this.shObjectFrom = shObjectFrom;
	}

	public ShObject getShObjectTo() {
		return shObjectTo;
	}

	public void setShObjectTo(ShObject shObjectTo) {
		this.shObjectTo = shObjectTo;
	}

	
}
