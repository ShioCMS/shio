package com.viglet.shiohara.persistence.model.channel;

import java.io.Serializable;
import javax.persistence.*;

import com.viglet.shiohara.persistence.model.site.ShSite;

import java.util.Date;

/**
 * The persistent class for the ShPost database table.
 * 
 */
@Entity
@NamedQuery(name = "ShChannel.findAll", query = "SELECT c FROM ShChannel c")
public class ShChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String name;

	private String summary;
	
	private ShChannel parentChannel;

	private ShSite shSite;

	public ShChannel() {
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public ShChannel getParentChannel() {
		return parentChannel;
	}

	public void setParentChannel(ShChannel parentChannel) {
		this.parentChannel = parentChannel;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

	
}