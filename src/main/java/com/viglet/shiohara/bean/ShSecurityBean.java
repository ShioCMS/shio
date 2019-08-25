package com.viglet.shiohara.bean;

import java.util.Set;

public class ShSecurityBean {

	private Set<String> shUsers;
	private Set<String> shGroups;

	public Set<String> getShUsers() {
		return shUsers;
	}

	public void setShUsers(Set<String> shUsers) {
		this.shUsers = shUsers;
	}

	public Set<String> getShGroups() {
		return shGroups;
	}

	public void setShGroups(Set<String> shGroups) {
		this.shGroups = shGroups;
	}

}
