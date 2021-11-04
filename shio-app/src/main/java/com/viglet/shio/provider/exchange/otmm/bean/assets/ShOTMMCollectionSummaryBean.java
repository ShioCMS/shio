/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.provider.exchange.otmm.bean.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
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
