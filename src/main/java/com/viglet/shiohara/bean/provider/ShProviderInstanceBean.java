package com.viglet.shiohara.bean.provider;

import java.util.HashMap;
import java.util.Map;

import com.viglet.shiohara.persistence.model.provider.ShProviderInstance;

public class ShProviderInstanceBean extends ShProviderInstance  {

	private Map<String, String> properties = new HashMap<>();

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}


	
}
