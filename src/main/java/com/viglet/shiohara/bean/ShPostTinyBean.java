package com.viglet.shiohara.bean;

import java.util.Date;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;

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
