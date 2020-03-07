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
package com.viglet.shio.bean;

import java.util.Date;

import com.viglet.shio.persistence.model.post.type.ShPostType;

/**
 * @author Alexandre Oliveira
 */
public class ShPostTinyBean {

	private String id;

	private String title;

	private String summary;

	private int position;

	private Date date;

	private ShPostType shPostType;

	private String objectType;

	private String publishStatus;

	private boolean published;

	public ShPostTinyBean(String id, String title, String summary, int position, Date date, String shPostTypeId,
			String shPosTypeName, String shPosTypeTitle, String objectType, String publishStatus, boolean published) {
		this.id = id;
		this.title = title;
		this.summary = summary;
		this.position = position;
		this.date = date;
		this.objectType = objectType;
		this.publishStatus = publishStatus;
		this.published = published;

		this.shPostType = new ShPostType();
		this.shPostType.setId(shPostTypeId);
		this.shPostType.setName(shPosTypeName);
		this.shPostType.setTitle(shPosTypeTitle);
	}

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public String getObjectType() {
		return objectType;
	}

	public void setObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public int getPosition() {
		return position;
	}

	public void setPosition(int position) {
		this.position = position;
	}

	public Date getDate() {
		return date;
	}

	public void setDate(Date date) {
		this.date = date;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public ShPostType getShPostType() {
		return shPostType;
	}

	public void setShPostType(ShPostType shPostType) {
		this.shPostType = shPostType;
	}

	public String getPublishStatus() {
		return publishStatus;
	}

	public void setPublishStatus(String publishStatus) {
		this.publishStatus = publishStatus;
	}

	public boolean isPublished() {
		return published;
	}

	public void setPublished(boolean published) {
		this.published = published;
	}

}
