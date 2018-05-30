package com.viglet.shiohara.exchange;

import java.util.Set;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShRelatorExchange {

	private String id;

	private String name;

	private Set<Object> shSubPosts;

	public String getId() {
		return id;
	}

	public void setId(String id) {
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
