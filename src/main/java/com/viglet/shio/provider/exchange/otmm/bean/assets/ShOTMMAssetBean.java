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

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.viglet.shio.provider.exchange.otmm.bean.object.ShOTMMObjectBean;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTMMAssetBean extends ShOTMMObjectBean {

	@JsonProperty("asset_lock_state_last_update_date")
	private Date assetLockStateLastUpdateDate;

	@JsonProperty("asset_lock_state_user_id")
	private String assetLockStateUserId;

	@JsonProperty("content_lock_state_last_update_date")
	private Date contentLockStateLastUpdateDate;

	@JsonProperty("content_lock_state_user_id")
	private String contentLockStateUserId;

	@JsonProperty("content_lock_state_user_name")
	private String contentLockStateUserName;

	@JsonProperty("content_size")
	private int contentSize;

	@JsonProperty("content_state_last_update_date")
	private Date contentStateLastUpdateDate;

	@JsonProperty("content_state_user_id")
	private String contentStateUserId;

	@JsonProperty("content_state_user_name")
	private String contentStateUserName;

	@JsonProperty("master_content_info")
	private ShOTMMMasterContentBean masterContentInfo;

	@JsonProperty("metadata_lock_state_user_name")
	private String metadataLockStateUserName;

	@JsonProperty("mime_type")
	private String mimeType;

	public Date getAssetLockStateLastUpdateDate() {
		return assetLockStateLastUpdateDate;
	}

	public void setAssetLockStateLastUpdateDate(Date assetLockStateLastUpdateDate) {
		this.assetLockStateLastUpdateDate = assetLockStateLastUpdateDate;
	}

	public String getAssetLockStateUserId() {
		return assetLockStateUserId;
	}

	public void setAssetLockStateUserId(String assetLockStateUserId) {
		this.assetLockStateUserId = assetLockStateUserId;
	}

	public Date getContentLockStateLastUpdateDate() {
		return contentLockStateLastUpdateDate;
	}

	public void setContentLockStateLastUpdateDate(Date contentLockStateLastUpdateDate) {
		this.contentLockStateLastUpdateDate = contentLockStateLastUpdateDate;
	}

	public String getContentLockStateUserId() {
		return contentLockStateUserId;
	}

	public void setContentLockStateUserId(String contentLockStateUserId) {
		this.contentLockStateUserId = contentLockStateUserId;
	}

	public String getContentLockStateUserName() {
		return contentLockStateUserName;
	}

	public void setContentLockStateUserName(String contentLockStateUserName) {
		this.contentLockStateUserName = contentLockStateUserName;
	}

	public int getContentSize() {
		return contentSize;
	}

	public void setContentSize(int contentSize) {
		this.contentSize = contentSize;
	}

	public Date getContentStateLastUpdateDate() {
		return contentStateLastUpdateDate;
	}

	public void setContentStateLastUpdateDate(Date contentStateLastUpdateDate) {
		this.contentStateLastUpdateDate = contentStateLastUpdateDate;
	}

	public String getContentStateUserId() {
		return contentStateUserId;
	}

	public void setContentStateUserId(String contentStateUserId) {
		this.contentStateUserId = contentStateUserId;
	}

	public String getContentStateUserName() {
		return contentStateUserName;
	}

	public void setContentStateUserName(String contentStateUserName) {
		this.contentStateUserName = contentStateUserName;
	}

	public ShOTMMMasterContentBean getMasterContentInfo() {
		return masterContentInfo;
	}

	public void setMasterContentInfo(ShOTMMMasterContentBean masterContentInfo) {
		this.masterContentInfo = masterContentInfo;
	}

	public String getMetadataLockStateUserName() {
		return metadataLockStateUserName;
	}

	public void setMetadataLockStateUserName(String metadataLockStateUserName) {
		this.metadataLockStateUserName = metadataLockStateUserName;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

}
