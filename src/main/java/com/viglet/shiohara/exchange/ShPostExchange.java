package com.viglet.shiohara.exchange;

import java.util.Date;
import java.util.Map;
import java.util.UUID;

import com.fasterxml.jackson.annotation.JsonFormat;

public class ShPostExchange {
	
	private UUID globalId;
	
	private UUID id;

	@JsonFormat(pattern = "yyyy-MM-dd'T'HH:mm'Z'")
	private Date date;

	private UUID channel;

	private String postType;

	private Map<String, Object> fields;

	
	public UUID getGlobalId() {
		return globalId;
	}

	public void setGlobalId(UUID globalId) {
		this.globalId = globalId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public Date getDate() {
		return this.date;
	}
	
	public void setDate(Date date) {
		this.date = date;
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

	public String getPostType() {
		return postType;
	}

	public void setPostType(String postType) {
		this.postType = postType;
	}

}
