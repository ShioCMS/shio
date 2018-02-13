package com.viglet.shiohara.persistence.model.channel;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;

import java.util.Date;
import java.util.List;
import java.util.UUID;

/**
 * The persistent class for the ShPost database table.
 * 
 */
@Entity
@NamedQuery(name = "ShChannel.findAll", query = "SELECT c FROM ShChannel c")
@JsonIgnoreProperties({ "shChannels", "shPosts" })
public class ShChannel implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")
	
	@Column(name = "id", updatable = false, nullable = false)
	private UUID id;

	@Temporal(TemporalType.TIMESTAMP)
	private Date date;

	private String name;

	private byte rootChannel;

	// bi-directional many-to-one association to ShChannel
	@ManyToOne
	@JoinColumn(name = "parent_channel_id")
	private ShChannel parentChannel;

	// bi-directional many-to-one association to ShSite
	@ManyToOne
	@JoinColumn(name = "site_id")
	private ShSite shSite;

	// bi-directional many-to-one association to ShChannel
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "parentChannel", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShChannel> shChannels;

	// bi-directional many-to-one association to ShChannel
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shChannel", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShPost> shPosts;

	public ShChannel() {
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
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

	public List<ShChannel> getShChannels() {
		return this.shChannels;
	}

	public void setShChannels(List<ShChannel> shChannels) {
		this.shChannels = shChannels;
	}

	public List<ShPost> getShPosts() {
		return this.shPosts;
	}

	public void setShPosts(List<ShPost> shPosts) {
		this.shPosts = shPosts;
	}

	public byte getRootChannel() {
		return rootChannel;
	}

	public void setRootChannel(byte rootChannel) {
		this.rootChannel = rootChannel;
	}

}