package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The primary key class for the ShSite database table.
 * 
 */
@Embeddable
public class ShSitePK implements Serializable {
	//default serial version id, required for serializable classes.
	private static final long serialVersionUID = 1L;

	private String id;

	private String description;

	private String url;

	public ShSitePK() {
	}
	public String getId() {
		return this.id;
	}
	public void setId(String id) {
		this.id = id;
	}
	public String getDescription() {
		return this.description;
	}
	public void setDescription(String description) {
		this.description = description;
	}
	public String getUrl() {
		return this.url;
	}
	public void setUrl(String url) {
		this.url = url;
	}

	public boolean equals(Object other) {
		if (this == other) {
			return true;
		}
		if (!(other instanceof ShSitePK)) {
			return false;
		}
		ShSitePK castOther = (ShSitePK)other;
		return 
			this.id.equals(castOther.id)
			&& this.description.equals(castOther.description)
			&& this.url.equals(castOther.url);
	}

	public int hashCode() {
		final int prime = 31;
		int hash = 17;
		hash = hash * prime + this.id.hashCode();
		hash = hash * prime + this.description.hashCode();
		hash = hash * prime + this.url.hashCode();
		
		return hash;
	}
}