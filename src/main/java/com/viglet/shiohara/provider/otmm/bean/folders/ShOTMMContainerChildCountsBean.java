package com.viglet.shiohara.provider.otmm.bean.folders;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMContainerChildCountsBean {

	@JsonProperty("asset_count")
	private int assetCount;

	@JsonProperty("container_count")
	private int containerCount;

	@JsonProperty("total_child_count")
	private int totalChildCount;

	public int getAssetCount() {
		return assetCount;
	}

	public void setAssetCount(int assetCount) {
		this.assetCount = assetCount;
	}

	public int getContainerCount() {
		return containerCount;
	}

	public void setContainerCount(int containerCount) {
		this.containerCount = containerCount;
	}

	public int getTotalChildCount() {
		return totalChildCount;
	}

	public void setTotalChildCount(int totalChildCount) {
		this.totalChildCount = totalChildCount;
	}

}
