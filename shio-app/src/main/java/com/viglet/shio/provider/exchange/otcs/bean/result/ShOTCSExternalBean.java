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
public abstract class ShOTCSExternalBean {

	@JsonProperty("external_create_date")
	private Date externalCreateDate;

	@JsonProperty("external_identity")
	private String externalIdentity;

	@JsonProperty("external_identity_type")
	private String externalIdentityType;

	@JsonProperty("external_modify_date")
	private Date externalModifyDate;

	@JsonProperty("external_source")
	private String externalSource;

	public Date getExternalCreateDate() {
		return externalCreateDate;
	}

	public void setExternalCreateDate(Date externalCreateDate) {
		this.externalCreateDate = externalCreateDate;
	}

	public String getExternalIdentity() {
		return externalIdentity;
	}

	public void setExternalIdentity(String externalIdentity) {
		this.externalIdentity = externalIdentity;
	}

	public String getExternalIdentityType() {
		return externalIdentityType;
	}

	public void setExternalIdentityType(String externalIdentityType) {
		this.externalIdentityType = externalIdentityType;
	}

	public Date getExternalModifyDate() {
		return externalModifyDate;
	}

	public void setExternalModifyDate(Date externalModifyDate) {
		this.externalModifyDate = externalModifyDate;
	}

	public String getExternalSource() {
		return externalSource;
	}

	public void setExternalSource(String externalSource) {
		this.externalSource = externalSource;
	}

}
