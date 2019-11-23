/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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
