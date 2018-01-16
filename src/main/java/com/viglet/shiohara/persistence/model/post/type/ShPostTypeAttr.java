package com.viglet.shiohara.persistence.model.post.type;

import java.io.Serializable;
import javax.persistence.*;

import org.hibernate.annotations.Fetch;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.widget.ShWidget;

import java.util.List;

/**
 * The persistent class for the ShPostTypeAttr database table.
 * 
 */
@Entity
@NamedQuery(name = "ShPostTypeAttr.findAll", query = "SELECT s FROM ShPostTypeAttr s")
@JsonIgnoreProperties({ "shPostType", "shPostAttrs" })
public class ShPostTypeAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int id;

	private String description;

	private byte isSummary;

	private byte isTitle;

	private String label;

	private byte many;

	private String name;

	private int ordinal;

	private byte required;

	// bi-directional many-to-one association to ShPostAttr
	@OneToMany(fetch = FetchType.EAGER, mappedBy = "shPostTypeAttr", cascade = CascadeType.ALL)
	@Fetch(org.hibernate.annotations.FetchMode.SUBSELECT)
	private List<ShPostAttr> shPostAttrs;

	// bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name = "postType_id")
	private ShPostType shPostType;

	// bi-directional many-to-one association to ShWidget
	@ManyToOne
	@JoinColumn(name = "widget_id")
	private ShWidget shWidget;

	public ShPostTypeAttr() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public byte getMany() {
		return this.many;
	}

	public void setMany(byte many) {
		this.many = many;
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

	public List<ShPostAttr> getShPostAttrs() {
		return this.shPostAttrs;
	}

	public void setShPostAttrs(List<ShPostAttr> shPostAttrs) {
		this.shPostAttrs = shPostAttrs;
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

}