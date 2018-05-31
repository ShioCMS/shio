package com.viglet.shiohara.persistence.model.folder;

import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.search.annotations.Field;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;

import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the ShPost database table.
 * 
 */
@Entity
@NamedQuery(name = "ShFolder.findAll", query = "SELECT c FROM ShFolder c")
@JsonIgnoreProperties({ "shFolders", "shPosts" })
@PrimaryKeyJoinColumn(name = "object_id")
public class ShFolder extends ShObject {
	private static final long serialVersionUID = 1L;

	@Field
	private String name;

	private byte rootFolder;

	// bi-directional many-to-one association to ShFolder
	@ManyToOne
	@JoinColumn(name = "parent_folder_id")
	private ShFolder parentFolder;

	// bi-directional many-to-one association to ShSite
	@ManyToOne
	@JoinColumn(name = "site_id")
	private ShSite shSite;

	// bi-directional many-to-one association to ShFolder
	@OneToMany(mappedBy = "parentFolder", orphanRemoval = true)
	@Cascade({CascadeType.ALL})
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShFolder> shFolders = new HashSet<ShFolder>();

	// bi-directional many-to-one association to ShFolder
	@OneToMany(mappedBy = "shFolder", orphanRemoval = true)
	@Cascade({CascadeType.ALL})
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private Set<ShPost> shPosts = new HashSet<ShPost>();

	public ShFolder() {
		this.setObjectType(ShObjectType.FOLDER);
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ShFolder getParentFolder() {
		return parentFolder;
	}

	public void setParentFolder(ShFolder parentFolder) {
		this.parentFolder = parentFolder;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

	public Set<ShFolder> getShFolders() {
		return this.shFolders;
	}

	public void setShFolders(Set<ShFolder> shFolders) {
		this.shFolders.clear();
		if (shFolders != null) {
			this.shFolders.addAll(shFolders);
		}
	}

	public Set<ShPost> getShPosts() {
		return this.shPosts;
	}

	public void setShPosts(Set<ShPost> shPosts) {
		this.shPosts.clear();
		if (shPosts != null) {
			this.shPosts.addAll(shPosts);
		}
	}

	public byte getRootFolder() {
		return rootFolder;
	}

	public void setRootFolder(byte rootFolder) {
		this.rootFolder = rootFolder;
	}

	@Override
	public String getObjectType() {
		return ShObjectType.FOLDER;
	}

	@Override
	public void setObjectType(String objectType) {		
		super.setObjectType(ShObjectType.FOLDER);
	}
}
