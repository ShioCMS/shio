package com.viglet.shiohara.persistence.model.system;

import java.io.Serializable;
import javax.persistence.*;

/**
 * The persistent class for the vigNLPSolutions database table.
 * 
 */
@Entity
@Table(name = "shLocale")
@NamedQuery(name = "ShLocale.findAll", query = "SELECT l FROM ShLocale l")
public class ShLocale implements Serializable {
	private static final long serialVersionUID = 1L;

	@Id
	@Column(unique = true, nullable = false, length = 5)
	private String initials;

	@Column(nullable = true, length = 255)
	private String en;

	@Column(nullable = true, length = 255)
	private String pt;

	public ShLocale() {

	}

	public ShLocale(String initials, String en, String pt) {
		setInitials(initials);
		setEn(en);
		setPt(pt);
	}

	public String getInitials() {
		return initials;
	}

	public void setInitials(String initials) {
		this.initials = initials;
	}

	public String getEn() {
		return en;
	}

	public void setEn(String en) {
		this.en = en;
	}

	public String getPt() {
		return pt;
	}

	public void setPt(String pt) {
		this.pt = pt;
	}

}