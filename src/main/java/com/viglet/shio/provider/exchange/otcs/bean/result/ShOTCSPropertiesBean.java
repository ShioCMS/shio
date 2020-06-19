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
import java.util.List;
import java.util.Map;

import com.fasterxml.jackson.annotation.JsonProperty;

/**
 * @author Alexandre Oliveira
 * 
 * @since 0.3.6
 */
public class ShOTCSPropertiesBean extends ShOTCSExternalBean {

	@JsonProperty("advanced_versioning")
	private String advancedVersioning;

	private boolean container;

	@JsonProperty("container_size")
	private int containerSize;

	@JsonProperty("create_date")
	private Date createDate;

	@JsonProperty("create_user_id")
	private int createUserId;

	private List<String> customsidebars;

	private String description;

	@JsonProperty("description_multilingual")
	private Map<String, Object> descriptionMultilingual;
	
	private boolean favorite;

	private int id;

	@JsonProperty("image_folder_id")
	private int imageFolderId;

	private boolean imagebrowseenabled;

	@JsonProperty("main_page_id")
	private int mainPageId;

	@JsonProperty("righmime_typet_id")
	private String mimeType;

	@JsonProperty("modify_date")
	private Date modifyDate;

	@JsonProperty("modify_user_id")
	private int modifyUserId;

	private String name;

	@JsonProperty("name_multilingual")
	private Map<String, Object> nameMultilingual;

	private String owner;

	@JsonProperty("owner_group_id")
	private int ownerGroupId;

	@JsonProperty("owner_user_id")
	private int ownerUserId;

	@JsonProperty("parent_id")
	private int parentId;

	@JsonProperty("permissions_model")
	private String permissionsModel;

	private boolean reserved;

	@JsonProperty("reserved_date")
	private Date reservedDate;

	@JsonProperty("reserved_shared_collaboration")
	private boolean reservedSharedCollaboration;

	@JsonProperty("reserved_user_id")
	private int reservedUserId;

	private int size;

	@JsonProperty("size_formatted")
	private String sizeFormatted;

	private int type;

	@JsonProperty("type_name")
	private String typeName;

	private String versionable;

	@JsonProperty("versions_control_advanced")
	private boolean versionsControlAdvanced;

	@JsonProperty("volume_id")
	private int volumeId;

	public String getAdvancedVersioning() {
		return advancedVersioning;
	}

	public void setAdvancedVersioning(String advancedVersioning) {
		this.advancedVersioning = advancedVersioning;
	}

	public boolean isContainer() {
		return container;
	}

	public void setContainer(boolean container) {
		this.container = container;
	}

	public int getContainerSize() {
		return containerSize;
	}

	public void setContainerSize(int containerSize) {
		this.containerSize = containerSize;
	}

	public Date getCreateDate() {
		return createDate;
	}

	public void setCreateDate(Date createDate) {
		this.createDate = createDate;
	}

	public int getCreateUserId() {
		return createUserId;
	}

	public void setCreateUserId(int createUserId) {
		this.createUserId = createUserId;
	}

	public List<String> getCustomsidebars() {
		return customsidebars;
	}

	public void setCustomsidebars(List<String> customsidebars) {
		this.customsidebars = customsidebars;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Map<String, Object> getDescriptionMultilingual() {
		return descriptionMultilingual;
	}

	public void setDescriptionMultilingual(Map<String, Object> descriptionMultilingual) {
		this.descriptionMultilingual = descriptionMultilingual;
	}

	public boolean isFavorite() {
		return favorite;
	}

	public void setFavorite(boolean favorite) {
		this.favorite = favorite;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public int getImageFolderId() {
		return imageFolderId;
	}

	public void setImageFolderId(int imageFolderId) {
		this.imageFolderId = imageFolderId;
	}

	public boolean isImagebrowseenabled() {
		return imagebrowseenabled;
	}

	public void setImagebrowseenabled(boolean imagebrowseenabled) {
		this.imagebrowseenabled = imagebrowseenabled;
	}

	public int getMainPageId() {
		return mainPageId;
	}

	public void setMainPageId(int mainPageId) {
		this.mainPageId = mainPageId;
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

	public int getModifyUserId() {
		return modifyUserId;
	}

	public void setModifyUserId(int modifyUserId) {
		this.modifyUserId = modifyUserId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Map<String, Object> getNameMultilingual() {
		return nameMultilingual;
	}

	public void setNameMultilingual(Map<String, Object> nameMultilingual) {
		this.nameMultilingual = nameMultilingual;
	}

	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}

	public int getOwnerGroupId() {
		return ownerGroupId;
	}

	public void setOwnerGroupId(int ownerGroupId) {
		this.ownerGroupId = ownerGroupId;
	}

	public int getOwnerUserId() {
		return ownerUserId;
	}

	public void setOwnerUserId(int ownerUserId) {
		this.ownerUserId = ownerUserId;
	}

	public int getParentId() {
		return parentId;
	}

	public void setParentId(int parentId) {
		this.parentId = parentId;
	}

	public String getPermissionsModel() {
		return permissionsModel;
	}

	public void setPermissionsModel(String permissionsModel) {
		this.permissionsModel = permissionsModel;
	}

	public boolean isReserved() {
		return reserved;
	}

	public void setReserved(boolean reserved) {
		this.reserved = reserved;
	}

	public Date getReservedDate() {
		return reservedDate;
	}

	public void setReservedDate(Date reservedDate) {
		this.reservedDate = reservedDate;
	}

	public boolean isReservedSharedCollaboration() {
		return reservedSharedCollaboration;
	}

	public void setReservedSharedCollaboration(boolean reservedSharedCollaboration) {
		this.reservedSharedCollaboration = reservedSharedCollaboration;
	}

	public int getReservedUserId() {
		return reservedUserId;
	}

	public void setReservedUserId(int reservedUserId) {
		this.reservedUserId = reservedUserId;
	}

	public int getSize() {
		return size;
	}

	public void setSize(int size) {
		this.size = size;
	}

	public String getSizeFormatted() {
		return sizeFormatted;
	}

	public void setSizeFormatted(String sizeFormatted) {
		this.sizeFormatted = sizeFormatted;
	}

	public int getType() {
		return type;
	}

	public void setType(int type) {
		this.type = type;
	}

	public String getTypeName() {
		return typeName;
	}

	public void setTypeName(String typeName) {
		this.typeName = typeName;
	}

	public String getVersionable() {
		return versionable;
	}

	public void setVersionable(String versionable) {
		this.versionable = versionable;
	}

	public boolean isVersionsControlAdvanced() {
		return versionsControlAdvanced;
	}

	public void setVersionsControlAdvanced(boolean versionsControlAdvanced) {
		this.versionsControlAdvanced = versionsControlAdvanced;
	}

	public int getVolumeId() {
		return volumeId;
	}

	public void setVolumeId(int volumeId) {
		this.volumeId = volumeId;
	}

}
