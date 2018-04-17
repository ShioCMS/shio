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

	private UUID folder;

	private String postType;

	private Map<String, Object> fields;

	private String owner;
	
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

	public UUID getFolder() {
		return folder;
	}

	public void setFolder(UUID folder) {
		this.folder = folder;
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
	public String getOwner() {
		return owner;
	}

	public void setOwner(String owner) {
		this.owner = owner;
	}
}
