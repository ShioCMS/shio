package com.viglet.shio.stock.beans;

public class ShSPhotoPreviewBean {
	private String path;
	private int width;
	private int height;

	public ShSPhotoPreviewBean(String path, int width, int height) {
		super();
		this.setPath(path);
		this.setWidth(width);
		this.setHeight(height);
	}

	public String getPath() {
		return path;
	}

	public void setPath(String path) {
		this.path = path;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

}
