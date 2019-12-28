package com.viglet.shiohara.bean.provider.exchange;

import java.util.HashMap;
import java.util.Map;

import com.viglet.shiohara.persistence.model.provider.exchange.ShExchangeProviderInstance;

public class ShExchangeProviderInstanceBean extends ShExchangeProviderInstance  {

	private Map<String, String> properties = new HashMap<>();

	public Map<String, String> getProperties() {
		return properties;
	}

	public void setProperties(Map<String, String> properties) {
		this.properties = properties;
	}


	
}
