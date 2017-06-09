package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ShPostTypeAttr database table.
 * 
 */
@Entity
@NamedQuery(name="ShPostTypeAttr.findAll", query="SELECT s FROM ShPostTypeAttr s")
public class ShPostTypeAttr implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	private String description;

	private byte isSummary;

	private byte isTitle;

	private String label;

	private byte many;

	private String name;

	private int position;

	private byte required;

	//bi-directional many-to-one association to ShPostAttr
	@OneToMany(mappedBy="shPostTypeAttr")
	private List<ShPostAttr> shPostAttrs;

	//bi-directional many-to-one association to ShPostType
	@ManyToOne
	@JoinColumn(name="postType_id")
	private ShPostType shPostType;

	//bi-directional many-to-one association to ShWidget
	@ManyToOne
	@JoinColumn(name="widget_id")
	private ShWidget shWidget;

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

	public int getPosition() {
		return this.position;
	}

	public void setPosition(int position) {
		this.position = position;
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

	public void setShwidget(ShWidget shWidget) {
		this.shWidget = shWidget;
	}

}