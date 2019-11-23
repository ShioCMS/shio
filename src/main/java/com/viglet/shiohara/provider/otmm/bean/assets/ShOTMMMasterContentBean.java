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

package com.viglet.shiohara.provider.otmm.bean.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMMasterContentBean {

	@JsonProperty("content_checksum")
	private String contentChecksum;

	@JsonProperty("content_data")
	private ShOTMMContentDataBean contentData;

	@JsonProperty("content_kind")
	private String contentKind;

	@JsonProperty("content_manager_id")
	private String contentManagerId;

	@JsonProperty("content_size")
	private int contentSize;

	private int height;

	private String id;

	@JsonProperty("mime_type")
	private String mimeType;

	private String name;

	@JsonProperty("unit_of_size")
	private String unitOfSize;

	private String url;

	private int width;

	public String getContentChecksum() {
		return contentChecksum;
	}

	public void setContentChecksum(String contentChecksum) {
		this.contentChecksum = contentChecksum;
	}

	public ShOTMMContentDataBean getContentData() {
		return contentData;
	}

	public void setContentData(ShOTMMContentDataBean contentData) {
		this.contentData = contentData;
	}

	public String getContentKind() {
		return contentKind;
	}

	public void setContentKind(String contentKind) {
		this.contentKind = contentKind;
	}

	public String getContentManagerId() {
		return contentManagerId;
	}

	public void setContentManagerId(String contentManagerId) {
		this.contentManagerId = contentManagerId;
	}

	public int getContentSize() {
		return contentSize;
	}

	public void setContentSize(int contentSize) {
		this.contentSize = contentSize;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
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

	public String getUnitOfSize() {
		return unitOfSize;
	}

	public void setUnitOfSize(String unitOfSize) {
		this.unitOfSize = unitOfSize;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

}
