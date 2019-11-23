package com.viglet.shiohara.provider.otmm.bean.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMCollectionSummaryBean {

	@JsonProperty("after_index")
	private int afterIndex;

	private int limit;

	@JsonProperty("actual_count_of_items")
	private int actualCountOfItems;

	@JsonProperty("total_number_of_items")
	private int totalNumberOfItems;

	@JsonProperty("group_to_count_map")
	private ShOTMMGroupToCountMapBean groupToCountMap;

	public int getAfterIndex() {
		return afterIndex;
	}

	public void setAfterIndex(int afterIndex) {
		this.afterIndex = afterIndex;
	}

	public int getLimit() {
		return limit;
	}

	public void setLimit(int limit) {
		this.limit = limit;
	}

	public int getActualCountOfItems() {
		return actualCountOfItems;
	}

	public void setActualCountOfItems(int actualCountOfItems) {
		this.actualCountOfItems = actualCountOfItems;
	}

	public int getTotalNumberOfItems() {
		return totalNumberOfItems;
	}

	public void setTotalNumberOfItems(int totalNumberOfItems) {
		this.totalNumberOfItems = totalNumberOfItems;
	}

	public ShOTMMGroupToCountMapBean getGroupToCountMap() {
		return groupToCountMap;
	}

	public void setGroupToCountMap(ShOTMMGroupToCountMapBean groupToCountMap) {
		this.groupToCountMap = groupToCountMap;
	}

}
