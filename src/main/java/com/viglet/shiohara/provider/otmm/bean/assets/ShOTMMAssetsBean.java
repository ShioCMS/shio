package com.viglet.shiohara.provider.otmm.bean.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMAssetsBean {

	@JsonProperty("assets_resource")
	private ShOTMMAssetsResourceBean assetsResource;

	public ShOTMMAssetsResourceBean getAssetsResource() {
		return assetsResource;
	}

	public void setAssetsResource(ShOTMMAssetsResourceBean assetsResource) {
		this.assetsResource = assetsResource;
	}

}
