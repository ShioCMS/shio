package com.viglet.shiohara.bean;

import java.util.Set;

import com.viglet.shiohara.persistence.model.auth.ShGroup;

public class ShSecurityBean {

	private Set<String> shUsers;
	private Set<ShGroup> shGroups;

	public Set<String> getShUsers() {
		return shUsers;
	}

	public void setShUsers(Set<String> shUsers) {
		this.shUsers = shUsers;
	}

	public Set<ShGroup> getShGroups() {
		return shGroups;
	}

	public void setShGroups(Set<ShGroup> shGroups) {
		this.shGroups = shGroups;
	}

}
