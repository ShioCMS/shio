package com.viglet.shiohara.provider.otmm.bean.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMSessionsBean {

	@JsonProperty("session_resource")
	private ShOTMMSessionsResourceBean sessionResource;

	public ShOTMMSessionsResourceBean getSessionResource() {
		return sessionResource;
	}

	public void setSessionResource(ShOTMMSessionsResourceBean sessionResource) {
		this.sessionResource = sessionResource;
	}
	
	
}
