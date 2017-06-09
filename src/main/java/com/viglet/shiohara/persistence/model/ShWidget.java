package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;
import java.util.List;


/**
 * The persistent class for the ShWidget database table.
 * 
 */
@Entity
@NamedQuery(name="ShWidget.findAll", query="SELECT s FROM ShWidget s")
public class ShWidget implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private String id;

	@Column(name="class_name")
	private String className;

	private String description;

	@Column(name="implementation_code")
	private String implementationCode;

	private String name;

	private String type;

	//bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(mappedBy="shwidget")
	private List<ShPostTypeAttr> shpostTypeAttrs;

	public ShWidget() {
	}

	public String getId() {
		return this.id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getClassName() {
		return this.className;
	}

	public void setClassName(String className) {
		this.className = className;
	}

	public String getDescription() {
		return this.description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getImplementationCode() {
		return this.implementationCode;
	}

	public void setImplementationCode(String implementationCode) {
		this.implementationCode = implementationCode;
	}

	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getType() {
		return this.type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<ShPostTypeAttr> getShpostTypeAttrs() {
		return this.shpostTypeAttrs;
	}

	public void setShpostTypeAttrs(List<ShPostTypeAttr> shpostTypeAttrs) {
		this.shpostTypeAttrs = shpostTypeAttrs;
	}

	public ShPostTypeAttr addShpostTypeAttr(ShPostTypeAttr shpostTypeAttr) {
		getShpostTypeAttrs().add(shpostTypeAttr);
		shpostTypeAttr.setShwidget(this);

		return shpostTypeAttr;
	}

	public ShPostTypeAttr removeShpostTypeAttr(ShPostTypeAttr shpostTypeAttr) {
		getShpostTypeAttrs().remove(shpostTypeAttr);
		shpostTypeAttr.setShwidget(null);

		return shpostTypeAttr;
	}

}