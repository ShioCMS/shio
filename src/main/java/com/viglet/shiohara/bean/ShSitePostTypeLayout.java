package com.viglet.shiohara.bean;

public class ShSitePostTypeLayout {
	private String format;
	private String layout;
	private String mimeType;
	private Integer cacheTTL;

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLayout() {
		return layout;
	}

	public void setLayout(String layout) {
		this.layout = layout;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Integer getCacheTTL() {
		return cacheTTL;
	}

	public void setCacheTTL(Integer cacheTTL) {
		this.cacheTTL = cacheTTL;
	}

}