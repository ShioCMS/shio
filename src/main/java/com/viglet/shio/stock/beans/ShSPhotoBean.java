package com.viglet.shio.stock.beans;

import java.util.Date;

public class ShSPhotoBean {
	private String name;
	
	private Date date;
	
	private ShSPhotoPreviewBean preview_xxs;

	private ShSPhotoPreviewBean preview_xs;
	
	private ShSPhotoPreviewBean preview_s;
	
	private ShSPhotoPreviewBean preview_m;
	
	private ShSPhotoPreviewBean preview_l;
	
	private ShSPhotoPreviewBean preview_xl;
	
	private ShSPhotoPreviewBean raw;

	private String dominantColor;

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

	public ShSPhotoPreviewBean getPreview_xxs() {
		return preview_xxs;
	}

	public void setPreview_xxs(ShSPhotoPreviewBean preview_xxs) {
		this.preview_xxs = preview_xxs;
	}

	public ShSPhotoPreviewBean getPreview_xs() {
		return preview_xs;
	}

	public void setPreview_xs(ShSPhotoPreviewBean preview_xs) {
		this.preview_xs = preview_xs;
	}

	public ShSPhotoPreviewBean getPreview_s() {
		return preview_s;
	}

	public void setPreview_s(ShSPhotoPreviewBean preview_s) {
		this.preview_s = preview_s;
	}

	public ShSPhotoPreviewBean getPreview_m() {
		return preview_m;
	}

	public void setPreview_m(ShSPhotoPreviewBean preview_m) {
		this.preview_m = preview_m;
	}

	public ShSPhotoPreviewBean getPreview_l() {
		return preview_l;
	}

	public void setPreview_l(ShSPhotoPreviewBean preview_l) {
		this.preview_l = preview_l;
	}

	public ShSPhotoPreviewBean getPreview_xl() {
		return preview_xl;
	}

	public void setPreview_xl(ShSPhotoPreviewBean preview_xl) {
		this.preview_xl = preview_xl;
	}

	public ShSPhotoPreviewBean getRaw() {
		return raw;
	}

	public void setRaw(ShSPhotoPreviewBean raw) {
		this.raw = raw;
	}

	public String getDominantColor() {
		return dominantColor;
	}

	public void setDominantColor(String dominantColor) {
		this.dominantColor = dominantColor;
	}

}
