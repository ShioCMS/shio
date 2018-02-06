package com.viglet.shiohara.exchange;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class ShPostExchange {
	private UUID id;

	private Date date;

	private String summary;

	private String title;

	private UUID channel;
	
	private UUID postType;

	private Map<String, Object> fields;
	
	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
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

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public UUID getChannel() {
		return channel;
	}

	public void setChannel(UUID channel) {
		this.channel = channel;
	}

	public Map<String, Object> getFields() {
		return fields;
	}

	public void setFields(Map<String, Object> fields) {
		this.fields = fields;
	}

	public UUID getPostType() {
		return postType;
	}

	public void setPostType(UUID postType) {
		this.postType = postType;
	}

}
