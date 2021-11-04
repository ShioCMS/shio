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
package com.viglet.shio.provider.exchange.otcs.bean.result;

import java.util.Date;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTCSVersionsBean extends ShOTCSExternalBean {

	@JsonProperty("create_date")
	private Date createDate;

	private String description;

	@JsonProperty("file_create_date")
	private Date fileCreateDate;

	@JsonProperty("file_modify_date")
	private Date fileModifyDate;

	@JsonProperty("file_name")
	private String fileName;

	@JsonProperty("file_size")
	private int fileSize;

	@JsonProperty("file_type")
	private String fileType;

	private int id;

	private boolean locked;

	@JsonProperty("locked_date")
	private Date lockedDate;

	@JsonProperty("locked_user_id")
	private String lockedUserId;

	@JsonProperty("mime_type")
	private String mimeType;

	@JsonProperty("modify_date")
	private Date modifyDate;

	private String name;

	@JsonProperty("owner_id")
	private int ownerId;

	@JsonProperty("provider_id")
	private int providerId;

	@JsonProperty("version_id")
	private int versionId;

	@JsonProperty("version_number")
	private int versionNumber;

	@JsonProperty("version_number_major")
	private int versionNumberMajor;

	@JsonProperty("version_number_minor")
	private int versionNumberMinor;

	@JsonProperty("version_number_name")
	private int versionNumberName;

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Date getFileCreateDate() {
		return fileCreateDate;
	}

	public void setFileCreateDate(Date fileCreateDate) {
		this.fileCreateDate = fileCreateDate;
	}

	public Date getFileModifyDate() {
		return fileModifyDate;
	}

	public void setFileModifyDate(Date fileModifyDate) {
		this.fileModifyDate = fileModifyDate;
	}

	public String getFileName() {
		return fileName;
	}

	public void setFileName(String fileName) {
		this.fileName = fileName;
	}

	public int getFileSize() {
		return fileSize;
	}

	public void setFileSize(int fileSize) {
		this.fileSize = fileSize;
	}

	public String getFileType() {
		return fileType;
	}

	public void setFileType(String fileType) {
		this.fileType = fileType;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLocked() {
		return locked;
	}

	public void setLocked(boolean locked) {
		this.locked = locked;
	}

	public Date getLockedDate() {
		return lockedDate;
	}

	public void setLockedDate(Date lockedDate) {
		this.lockedDate = lockedDate;
	}

	public String getLockedUserId() {
		return lockedUserId;
	}

	public void setLockedUserId(String lockedUserId) {
		this.lockedUserId = lockedUserId;
	}

	public String getMimeType() {
		return mimeType;
	}

	public void setMimeType(String mimeType) {
		this.mimeType = mimeType;
	}

	public Date getModifyDate() {
		return modifyDate;
	}

	public void setModifyDate(Date modifyDate) {
		this.modifyDate = modifyDate;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public int getOwnerId() {
		return ownerId;
	}

	public void setOwnerId(int ownerId) {
		this.ownerId = ownerId;
	}

	public int getProviderId() {
		return providerId;
	}

	public void setProviderId(int providerId) {
		this.providerId = providerId;
	}

	public int getVersionId() {
		return versionId;
	}

	public void setVersionId(int versionId) {
		this.versionId = versionId;
	}

	public int getVersionNumber() {
		return versionNumber;
	}

	public void setVersionNumber(int versionNumber) {
		this.versionNumber = versionNumber;
	}

	public int getVersionNumberMajor() {
		return versionNumberMajor;
	}

	public void setVersionNumberMajor(int versionNumberMajor) {
		this.versionNumberMajor = versionNumberMajor;
	}

	public int getVersionNumberMinor() {
		return versionNumberMinor;
	}

	public void setVersionNumberMinor(int versionNumberMinor) {
		this.versionNumberMinor = versionNumberMinor;
	}

	public int getVersionNumberName() {
		return versionNumberName;
	}

	public void setVersionNumberName(int versionNumberName) {
		this.versionNumberName = versionNumberName;
	}

}
