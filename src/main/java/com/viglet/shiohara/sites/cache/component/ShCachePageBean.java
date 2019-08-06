package com.viglet.shiohara.sites.cache.component;

import java.io.Serializable;
import java.util.Date;

public class ShCachePageBean implements Serializable {

	private static final long serialVersionUID = 1L;
	private String body;
	private String contentType;
	private Date expirationDate;

	public String getBody() {
		return body;
	}

	public void setBody(String body) {
		this.body = body;
	}

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public Date getExpirationDate() {
		return expirationDate;
	}

	public void setExpirationDate(Date expirationDate) {
		this.expirationDate = expirationDate;
	}
}
