package com.viglet.shiohara.provider.otmm.bean.folders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMFoldersBean {

	@JsonProperty("folders_resource")
	private ShOTMMFoldersResourceBean foldersResource;

	public ShOTMMFoldersResourceBean getFoldersResource() {
		return foldersResource;
	}

	public void setFoldersResource(ShOTMMFoldersResourceBean foldersResource) {
		this.foldersResource = foldersResource;
	}

}
