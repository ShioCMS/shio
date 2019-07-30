package com.viglet.shiohara.bean;

import java.util.Date;

public class ShFolderTinyBean {

	private String id;

	private int position;

	private String name;

	private Date date;

	public ShFolderTinyBean(String id, String name, int position, Date date) {
		this.id = id;
		this.position = position;
		this.name = name;
		this.date = date;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

}
