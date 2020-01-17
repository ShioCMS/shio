/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shiohara.bean.security;

import java.util.Set;

/**
 * @author Alexandre Oliveira
 */
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
