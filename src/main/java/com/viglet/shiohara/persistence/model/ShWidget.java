package com.viglet.shiohara.persistence.model;

import java.io.Serializable;
import javax.persistence.*;

import org.codehaus.jackson.annotate.JsonIgnoreProperties;

import java.util.List;


/**
 * The persistent class for the ShWidget database table.
 * 
 */
@Entity
@NamedQuery(name="ShWidget.findAll", query="SELECT s FROM ShWidget s")
@JsonIgnoreProperties({ "shPostTypeAttrs" })
public class ShWidget implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy=GenerationType.IDENTITY)
	private int id;

	@Column(name="class_name")
	private String className;

	private String description;

	@Column(name="implementation_code")
	private String implementationCode;

	private String name;

	private String type;

	//bi-directional many-to-one association to ShPostTypeAttr
	@OneToMany(mappedBy="shWidget")
	private List<ShPostTypeAttr> shPostTypeAttrs;

	public ShWidget() {
	}

	public int getId() {
		return this.id;
	}

	public void setId(int id) {
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

	public List<ShPostTypeAttr> getShPostTypeAttrs() {
		return this.shPostTypeAttrs;
	}

	public void setShPostTypeAttrs(List<ShPostTypeAttr> shPostTypeAttrs) {
		this.shPostTypeAttrs = shPostTypeAttrs;
	}

	public ShPostTypeAttr addShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		getShPostTypeAttrs().add(shPostTypeAttr);
		shPostTypeAttr.setShWidget(this);

		return shPostTypeAttr;
	}

	public ShPostTypeAttr removeShPostTypeAttr(ShPostTypeAttr shPostTypeAttr) {
		getShPostTypeAttrs().remove(shPostTypeAttr);
		shPostTypeAttr.setShWidget(null);

		return shPostTypeAttr;
	}

}