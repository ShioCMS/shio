package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;


/**
 * The persistent class for the ShSite database table.
 * 
 */
@Entity
@NamedQuery(name="ShSite.findAll", query="SELECT s FROM ShSite s")
public class ShSite implements Serializable {
	private static final long serialVersionUID = 1L;

	@EmbeddedId
	private ShSitePK id;

	private String name;

	public ShSite() {
	}

	public ShSitePK getId() {
		return this.id;
	}

	public void setId(ShSitePK id) {
		this.id = id;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

}