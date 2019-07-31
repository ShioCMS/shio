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

package com.viglet.shiohara.persistence.model.workflow;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;

import org.hibernate.annotations.GenericGenerator;

import com.viglet.shiohara.persistence.model.object.ShObject;

/**
 * The persistent class for the ShWidget database table.
 * 
 */
@Entity
@NamedQuery(name = "ShWorkflowTask.findAll", query = "SELECT wt FROM ShWorkflowTask wt")
public class ShWorkflowTask implements Serializable {

	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private Date date;

	private String title;

	@ManyToOne
	@JoinColumn(name = "object_id")
	private ShObject shObject;

	private String requester;
	
	private String requested;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getRequester() {
		return requester;
	}

	public void setRequester(String requester) {
		this.requester = requester;
	}

	public String getRequested() {
		return requested;
	}

	public void setRequested(String requested) {
		this.requested = requested;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ShObject getShObject() {
		return shObject;
	}

	public void setShObject(ShObject shObject) {
		this.shObject = shObject;
	}

}
