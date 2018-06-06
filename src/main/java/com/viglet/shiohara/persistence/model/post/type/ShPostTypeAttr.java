package com.viglet.shiohara.persistence.model.post.type;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Cascade;
import org.hibernate.annotations.CascadeType;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.GenericGenerator;
import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.widget.ShWidget;

import java.util.HashSet;
import java.util.Set;

/**
 * The persistent class for the ShPostTypeAttr database table.
 * 
 */
@Entity
@NamedQuery(name = "ShPostTypeAttr.findAll", query = "SELECT s FROM ShPostTypeAttr s")
@JsonIgnoreProperties({ "shPostAttrs" })
// Removed shPostType ignore, because it is used in JSON from new Post
public class ShPostTypeAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GenericGenerator(name = "UUID", strategy = "com.viglet.shiohara.jpa.ShUUIDGenerator")
	@GeneratedValue(generator = "UUID")

	@Column(name = "id", updatable = false, nullable = false)
	private String id;

	private String description;

	private byte isSummary;

	private byte isTitle;

	private String label;

	private String name;

	private int ordinal;

	private byte required;

	@OneToMany(mappedBy = "shParentPostTypeAttr", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
	@OnDelete(action = OnDeleteAction.CASCADE)
	private Set<ShPostTypeAttr> shPostTypeAttrs = new HashSet<ShPostTypeAttr>();

	@OneToMany(mappedBy = "shPostTypeAttr", orphanRemoval = true)
	@Cascade({ CascadeType.ALL })
	@Fetch(org.hibernate.annotations.FetchMode.JOIN)
 
	private Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();

	@ManyToOne
	@JoinColumn(name = "postType_id")
	@JsonView({ ShJsonView.ShJsonViewPostTypeAttr.class })
	private ShPostType shPostType;

	@ManyToOne
	@JoinColumn(name = "widget_id")
	private ShWidget shWidget;

	// bi-directional many-to-one association to ShPostTypeAttr
	@ManyToOne
	@JoinColumn(name = "parent_relator_id")
	@JsonView({ ShJsonView.ShJsonViewPostTypeAttr.class })
	private ShPostTypeAttr shParentPostTypeAttr;

	@Transient
	private String shPostTypeName;

	public String getShPostTypeName() {
		if (shPostType != null) {
			shPostTypeName = shPostType.getName();
		}
		return shPostTypeName;
	}

	public ShPostTypeAttr() {
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

	public byte getIsSummary() {
		return this.isSummary;
	}

	public void setIsSummary(byte isSummary) {
		this.isSummary = isSummary;
	}

	public byte getIsTitle() {
		return this.isTitle;
	}

	public void setIsTitle(byte isTitle) {
		this.isTitle = isTitle;
	}

	public String getLabel() {
		return this.label;
	}

	public void setLabel(String label) {
		this.label = label;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOrdinal() {
		return this.ordinal;
	}

	public void setOrdinal(int ordinal) {
		this.ordinal = ordinal;
	}

	public byte getRequired() {
		return this.required;
	}

	public void setRequired(byte required) {
		this.required = required;
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
		shPostAttr.setShPostTypeAttr(this);

		return shPostAttr;
	}

	public ShPostAttr removeShPostAttr(ShPostAttr shPostAttr) {
		getShPostAttrs().remove(shPostAttr);
		shPostAttr.setShPostTypeAttr(null);

		return shPostAttr;
	}

	public ShPostType getShPostType() {
		return this.shPostType;
	}

	public void setShPostType(ShPostType shPostType) {
		this.shPostType = shPostType;
	}

	public ShWidget getShWidget() {
		return this.shWidget;
	}

	public void setShWidget(ShWidget shWidget) {
		this.shWidget = shWidget;
	}

	public Set<ShPostTypeAttr> getShPostTypeAttrs() {
		return shPostTypeAttrs;
	}

	public void setShPostTypeAttrs(Set<ShPostTypeAttr> shPostTypeAttrs) {
		this.shPostTypeAttrs.clear();
		if (shPostTypeAttrs != null) {
			this.shPostTypeAttrs.addAll(shPostTypeAttrs);
		}
	}

	public ShPostTypeAttr getShParentPostTypeAttr() {
		return shParentPostTypeAttr;
	}

	public void setShParentPostTypeAttr(ShPostTypeAttr shParentPostTypeAttr) {
		this.shParentPostTypeAttr = shParentPostTypeAttr;
	}

}
