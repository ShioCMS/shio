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
package com.viglet.shiohara.auth.otds.provider.bean;

import java.util.List;

/**
 * @author Alexandre Oliveira
 */
public class ShOTDSUserResponseBean {

	private String userPartitionID;

	private List<ShOTDSValuesBean> values;

	private String location;

	private String description;

	private Object customAttributes;

	private String objectClass;

	private String id;

	private String uuid;

	private String originUUID;

	private String urlId;

	private String name;

	public String getUserPartitionID() {
		return userPartitionID;
	}

	public void setUserPartitionID(String userPartitionID) {
		this.userPartitionID = userPartitionID;
	}

	public List<ShOTDSValuesBean> getValues() {
		return values;
	}

	public void setValues(List<ShOTDSValuesBean> values) {
		this.values = values;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public Object getCustomAttributes() {
		return customAttributes;
	}

	public void setCustomAttributes(Object customAttributes) {
		this.customAttributes = customAttributes;
	}

	public String getObjectClass() {
		return objectClass;
	}

	public void setObjectClass(String objectClass) {
		this.objectClass = objectClass;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getUuid() {
		return uuid;
	}

	public void setUuid(String uuid) {
		this.uuid = uuid;
	}

	public String getOriginUUID() {
		return originUUID;
	}

	public void setOriginUUID(String originUUID) {
		this.originUUID = originUUID;
	}

	public String getUrlId() {
		return urlId;
	}

	public void setUrlId(String urlId) {
		this.urlId = urlId;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
