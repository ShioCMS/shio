package com.viglet.shiohara.provider.otmm.bean.assets;

import java.util.List;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMAssetsResourceBean {

	@JsonProperty("collection_summary")
	private ShOTMMCollectionSummaryBean collectionSummary;

	@JsonProperty("asset_list")
	private List<ShOTMMAssetBean> assetList;

	public ShOTMMCollectionSummaryBean getCollectionSummary() {
		return collectionSummary;
	}

	public void setCollectionSummary(ShOTMMCollectionSummaryBean collectionSummary) {
		this.collectionSummary = collectionSummary;
	}

	public List<ShOTMMAssetBean> getAssetList() {
		return assetList;
	}

	public void setAssetList(List<ShOTMMAssetBean> assetList) {
		this.assetList = assetList;
	}

}
