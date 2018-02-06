package com.viglet.shiohara.exchange;

import java.util.Date;
import java.util.UUID;

public class ShChannelExchange {
	private UUID id;

	private Date date;

	private String name;

	private String summary;

	private byte rootChannel;

	private UUID parentChannel;
	
	private UUID site;
	
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

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSummary() {
		return summary;
	}

	public void setSummary(String summary) {
		this.summary = summary;
	}

	public byte getRootChannel() {
		return rootChannel;
	}

	public void setRootChannel(byte rootChannel) {
		this.rootChannel = rootChannel;
	}

	public UUID getParentChannel() {
		return parentChannel;
	}

	public void setParentChannel(UUID parentChannel) {
		this.parentChannel = parentChannel;
	}

	public UUID getSite() {
		return site;
	}

	public void setSite(UUID shSite) {
		this.site = shSite;
	}

	
}
