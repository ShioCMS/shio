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
import com.viglet.shio.provider.exchange.otmm.bean.permission.ShOTMMAccessControlDescriptorBean;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTMMAssetBean {

	@JsonProperty("access_control_descriptor")
	private ShOTMMAccessControlDescriptorBean accessControlDescriptor;

	@JsonProperty("asset_content_info")
	private ShOTMMAssetContentInfoBean assetContentInfo;

	@JsonProperty("asset_id")
	private String assetId;

	@JsonProperty("asset_lock_state_last_update_date")
	private Date assetLockStateLastUpdateDate;

	@JsonProperty("asset_lock_state_user_id")
	private String assetLockStateUserId;

	@JsonProperty("asset_state")
	private String assetState;

	@JsonProperty("asset_state_last_update_date")
	private Date assetStateLastUpdateDate;

	@JsonProperty("asset_state_user_id")
	private String assetStateUserId;

	@JsonProperty("checked_out")
	private boolean checkedOut;

	@JsonProperty("content_editable")
	private boolean contentEditable;

	@JsonProperty("content_lock_state_last_update_date")
	private Date contentLockStateLastUpdateDate;

	@JsonProperty("content_lock_state_user_id")
	private String contentLockStateUserId;

	@JsonProperty("content_lock_state_user_name")
	private String contentLockStateUserName;

	@JsonProperty("content_size")
	private int contentSize;

	@JsonProperty("content_state")
	private String contentState;

	@JsonProperty("content_state_last_update_date")
	private Date contentStateLastUpdateDate;

	@JsonProperty("content_state_user_id")
	private String contentStateUserId;

	@JsonProperty("content_state_user_name")
	private String contentStateUserName;

	@JsonProperty("content_type")
	private String contentType;

	@JsonProperty("creator_id")
	private String creatorId;

	@JsonProperty("date_imported")
	private Date dateImported;

	@JsonProperty("date_last_updated")
	private Date dateLastUpdated;

	private boolean deleted;

	private boolean expired;

	@JsonProperty("import_job_id")
	private int importJobId;

	@JsonProperty("import_user_name")
	private String importUserName;

	@JsonProperty("latest_version")
	private boolean latestVersion;

	@JsonProperty("legacy_model_id")
	private int legacyModelId;

	private boolean locked;

	@JsonProperty("master_content_info")
	private ShOTMMMasterContentBean masterContentInfo;

	@JsonProperty("metadata_lock_state_user_name")
	private String metadataLockStateUserName;

	@JsonProperty("metadata_model_id")
	private String metadata_model_id;

	@JsonProperty("metadata_state_user_name")
	private String metadata_state_user_name;

	@JsonProperty("mime_type")
	private String mimeType;

	private String name;

	@JsonProperty("original_asset_id")
	private String originalAssetId;

	@JsonProperty("subscribed_to")
	private boolean subscribedTo;

	private int version;

	public ShOTMMAccessControlDescriptorBean getAccessControlDescriptor() {
		return accessControlDescriptor;
	}

	public void setAccessControlDescriptor(ShOTMMAccessControlDescriptorBean accessControlDescriptor) {
		this.accessControlDescriptor = accessControlDescriptor;
	}

	public ShOTMMAssetContentInfoBean getAssetContentInfo() {
		return assetContentInfo;
	}

	public void setAssetContentInfo(ShOTMMAssetContentInfoBean assetContentInfo) {
		this.assetContentInfo = assetContentInfo;
	}

	public String getAssetId() {
		return assetId;
	}

	public void setAssetId(String assetId) {
		this.assetId = assetId;
	}

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

	public String getAssetState() {
		return assetState;
	}

	public void setAssetState(String assetState) {
		this.assetState = assetState;
	}

	public Date getAssetStateLastUpdateDate() {
		return assetStateLastUpdateDate;
	}

	public void setAssetStateLastUpdateDate(Date assetStateLastUpdateDate) {
		this.assetStateLastUpdateDate = assetStateLastUpdateDate;
	}

	public String getAssetStateUserId() {
		return assetStateUserId;
	}

	public void setAssetStateUserId(String assetStateUserId) {
		this.assetStateUserId = assetStateUserId;
	}

	public boolean isCheckedOut() {
		return checkedOut;
	}

	public void setCheckedOut(boolean checkedOut) {
		this.checkedOut = checkedOut;
	}

	public boolean isContentEditable() {
		return contentEditable;
	}

	public void setContentEditable(boolean contentEditable) {
		this.contentEditable = contentEditable;
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

	public String getContentState() {
		return contentState;
	}

	public void setContentState(String contentState) {
		this.contentState = contentState;
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

	public String getContentType() {
		return contentType;
	}

	public void setContentType(String contentType) {
		this.contentType = contentType;
	}

	public String getCreatorId() {
		return creatorId;
	}

	public void setCreatorId(String creatorId) {
		this.creatorId = creatorId;
	}

	public Date getDateImported() {
		return dateImported;
	}

	public void setDateImported(Date dateImported) {
		this.dateImported = dateImported;
	}

	public Date getDateLastUpdated() {
		return dateLastUpdated;
	}

	public void setDateLastUpdated(Date dateLastUpdated) {
		this.dateLastUpdated = dateLastUpdated;
	}

	public boolean isDeleted() {
		return deleted;
	}

	public void setDeleted(boolean deleted) {
		this.deleted = deleted;
	}

	public boolean isExpired() {
		return expired;
	}

	public void setExpired(boolean expired) {
		this.expired = expired;
	}

	public int getImportJobId() {
		return importJobId;
	}

	public void setImportJobId(int importJobId) {
		this.importJobId = importJobId;
	}

	public String getImportUserName() {
		return importUserName;
	}

	public void setImportUserName(String importUserName) {
		this.importUserName = importUserName;
	}

	public boolean isLatestVersion() {
		return latestVersion;
	}

	public void setLatestVersion(boolean latestVersion) {
		this.latestVersion = latestVersion;
	}

	public int getLegacyModelId() {
		return legacyModelId;
	}

	public void setLegacyModelId(int legacyModelId) {
		this.legacyModelId = legacyModelId;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getOriginalAssetId() {
		return originalAssetId;
	}

	public void setOriginalAssetId(String originalAssetId) {
		this.originalAssetId = originalAssetId;
	}

	public boolean isSubscribedTo() {
		return subscribedTo;
	}

	public void setSubscribedTo(boolean subscribedTo) {
		this.subscribedTo = subscribedTo;
	}

	public int getVersion() {
		return version;
	}

	public void setVersion(int version) {
		this.version = version;
	}

	public String getMetadata_model_id() {
		return metadata_model_id;
	}

	public void setMetadata_model_id(String metadata_model_id) {
		this.metadata_model_id = metadata_model_id;
	}

	public String getMetadata_state_user_name() {
		return metadata_state_user_name;
	}

	public void setMetadata_state_user_name(String metadata_state_user_name) {
		this.metadata_state_user_name = metadata_state_user_name;
	}

}
