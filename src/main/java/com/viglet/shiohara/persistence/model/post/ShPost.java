package com.viglet.shiohara.persistence.model.post;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;
import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;

import java.util.HashSet;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;

/**
 * The persistent class for the ShPost database table.
 * 
 */
@Indexed
@Entity
@NamedQuery(name = "ShPost.findAll", query = "SELECT s FROM ShPost s")
public class ShPost extends ShObject {
	private static final long serialVersionUID = 1L;

	@Field(store = Store.YES)
	private String summary;

	@Field(store = Store.YES)
	private String title;

	// bi-directional many-to-one association to ShPostType
	@IndexedEmbedded
	@ManyToOne
	@JoinColumn(name = "post_type_id")
	private ShPostType shPostType;

	// bi-directional many-to-one association to ShFolder
	@IndexedEmbedded
	@ManyToOne
	@JoinColumn(name = "folder_id")
	private ShFolder shFolder;

	// bi-directional many-to-one association to ShPostAttr
	@IndexedEmbedded
	@OneToMany(mappedBy = "shPost", orphanRemoval = true)
	@Cascade({CascadeType.ALL})
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();

	public ShPost() {
	}

	public String getSummary() {
		return this.summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public String getTitle() {
		return this.title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public ShPostType getShPostType() {
		return this.shPostType;
	}

	public void setShPostType(ShPostType shPostType) {
		this.shPostType = shPostType;
	}

	public Set<ShPostAttr> getShPostAttrs() {
		return this.shPostAttrs;
	}

	public void setShPostAttrs(Set<ShPostAttr> shPostAttrs) {
		this.shPostAttrs.clear();
		if (shPostAttrs != null) {
			this.shPostAttrs.addAll(shPostAttrs);
		}
	}

	public ShPostAttr addShPostAttr(ShPostAttr shPostAttr) {
		getShPostAttrs().add(shPostAttr);
		shPostAttr.setShPost(this);

		return shPostAttr;
	}

	public ShPostAttr removeShPostAttr(ShPostAttr shPostAttr) {
		getShPostAttrs().remove(shPostAttr);
		shPostAttr.setShPost(null);

		return shPostAttr;
	}

	public ShFolder getShFolder() {
		return shFolder;
	}

	public void setShFolder(ShFolder shFolder) {
		this.shFolder = shFolder;
	}

}
