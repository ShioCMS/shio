package com.viglet.shio.bean;

import java.io.Serializable;

public class ShPostTypeReport implements Serializable {

	private static final long serialVersionUID = 1L;

	private String name;
	
	private int total;
	
	private float percentage;
	
	private String color;

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getTotal() {
		return total;
	}

	public void setTotal(int total) {
		this.total = total;
	}

	public float getPercentage() {
		return percentage;
	}

	public void setPercentage(float percentage) {
		this.percentage = percentage;
	}

	public String getColor() {
		return color;
	}

	public void setColor(String color) {
		this.color = color;
	}
	
	
}
