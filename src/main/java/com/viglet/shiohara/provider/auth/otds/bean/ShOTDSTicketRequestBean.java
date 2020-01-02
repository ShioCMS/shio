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
package com.viglet.shiohara.provider.auth.otds.bean;

/**
 * @author Alexandre Oliveira
 */
public class ShOTDSTicketRequestBean {
	private String targetResourceId;
	private String sourceResourceId;
	private String ticket;
	private String secureSecret;
	private String userName;
	private String authenticator;

	public String getTargetResourceId() {
		return targetResourceId;
	}

	public void setTargetResourceId(String targetResourceId) {
		this.targetResourceId = targetResourceId;
	}

	public String getSourceResourceId() {
		return sourceResourceId;
	}

	public void setSourceResourceId(String sourceResourceId) {
		this.sourceResourceId = sourceResourceId;
	}

	public String getTicket() {
		return ticket;
	}

	public void setTicket(String ticket) {
		this.ticket = ticket;
	}

	public String getSecureSecret() {
		return secureSecret;
	}

	public void setSecureSecret(String secureSecret) {
		this.secureSecret = secureSecret;
	}

	public String getUserName() {
		return userName;
	}

	public void setUserName(String userName) {
		this.userName = userName;
	}

	public String getAuthenticator() {
		return authenticator;
	}

	public void setAuthenticator(String authenticator) {
		this.authenticator = authenticator;
	}

}
