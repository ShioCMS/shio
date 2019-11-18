package com.viglet.shiohara.bean.security;

import java.util.Set;

public class ShPageSecurityBean {

	private boolean allowRegisterUser;

	private boolean allowGuestUser;

	private Set<String> shGroups;

	public boolean isAllowRegisterUser() {
		return allowRegisterUser;
	}

	public void setAllowRegisterUser(boolean allowRegisterUser) {
		this.allowRegisterUser = allowRegisterUser;
	}

	public boolean isAllowGuestUser() {
		return allowGuestUser;
	}

	public void setAllowGuestUser(boolean allowGuestUser) {
		this.allowGuestUser = allowGuestUser;
	}

	public Set<String> getShGroups() {
		return shGroups;
	}

	public void setShGroups(Set<String> shGroups) {
		this.shGroups = shGroups;
	}

}
