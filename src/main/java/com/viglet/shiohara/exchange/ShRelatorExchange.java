package com.viglet.shiohara.exchange;

import java.util.Set;
import java.util.UUID;

public class ShRelatorExchange {

	private UUID id;

	private String name;

	private Set<Object> shSubPosts;

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public Set<Object> getShSubPosts() {
		return shSubPosts;
	}

	public void setShSubPosts(Set<Object> shSubPosts) {
		this.shSubPosts = shSubPosts;
	}

	
	

}
