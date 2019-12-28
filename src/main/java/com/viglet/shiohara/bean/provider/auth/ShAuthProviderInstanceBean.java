package com.viglet.shiohara.bean.provider.auth;

import java.util.HashMap;
import java.util.Map;

import com.viglet.shiohara.persistence.model.provider.auth.ShAuthProviderInstance;

public class ShAuthProviderInstanceBean extends ShAuthProviderInstance  {

	private Map<String, String> properties = new HashMap<>();

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}


	
}
