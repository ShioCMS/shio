package com.viglet.shiohara.provider.otmm.bean.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMAssetContentInfoBean {

	@JsonProperty("master_content")
	private ShOTMMMasterContentBean masterContent;

	public ShOTMMMasterContentBean getMasterContent() {
		return masterContent;
	}

	public void setMasterContent(ShOTMMMasterContentBean masterContent) {
		this.masterContent = masterContent;
	}

}
