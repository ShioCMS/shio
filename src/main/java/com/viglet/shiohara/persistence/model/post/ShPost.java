package com.viglet.shiohara.persistence.model.post;

import javax.persistence.*;

import org.hibernate.search.annotations.Field;
import org.hibernate.search.annotations.Indexed;
import org.hibernate.search.annotations.IndexedEmbedded;
import org.hibernate.search.annotations.Store;
import org.springframework.beans.factory.annotation.Configurable;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;

import java.util.ArrayList;
import java.util.List;

/**
 * The persistent class for the ShPost database table.
 * 
 */
@Configurable(preConstruction = true)
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
	@OneToMany(mappedBy = "shPost", cascade = CascadeType.PERSIST)
	private List<ShPostAttr> shPostAttrs  = new ArrayList<ShPostAttr>();

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

	public List<ShPostAttr> getShPostAttrs() {
		return this.shPostAttrs;
	}

	public void setShPostAttrs(List<ShPostAttr> shPostAttrs) {
		this.shPostAttrs = shPostAttrs;
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
