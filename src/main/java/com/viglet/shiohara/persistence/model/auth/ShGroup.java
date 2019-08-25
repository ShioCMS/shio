package com.viglet.shiohara.persistence.model.auth;

import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.*;

import org.hibernate.annotations.GenericGenerator;

/**
 * The persistent class for the ShGroup database table.
 * 
 */
@Entity
@NamedQuery(name = "ShGroup.findAll", query = "SELECT g FROM ShGroup g")
public class ShGroup implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String name;

	private String description;

	@ManyToMany(mappedBy = "shGroups")
	private Set<ShRole> shRoles = new HashSet<>();

	@ManyToMany(mappedBy = "shGroups")
	private Set<ShUser> shUsers = new HashSet<>();

	public ShGroup() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Set<ShUser> getShUsers() {
		return this.shUsers;
	}

	public void setShUsers(Set<ShUser> shUsers) {
		this.shUsers.clear();
		if (shUsers != null) {
			this.shUsers.addAll(shUsers);
		}
	}
}