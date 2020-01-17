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
public class ShOTDSAuthenticationResponseBean {
    private String resourceID;
    private String continuationContext;
    private String token;
    private String ticket;
    private String userId;
    private String continuationData;
    private String failureReason;
    private long passwordExpirationTime;
    private boolean continuation;
	public String getResourceID() {
		return resourceID;
	}
	public void setResourceID(String resourceID) {
		this.resourceID = resourceID;
	}
	public String getContinuationContext() {
		return continuationContext;
	}
	public void setContinuationContext(String continuationContext) {
		this.continuationContext = continuationContext;
	}
	public String getToken() {
		return token;
	}
	public void setToken(String token) {
		this.token = token;
	}
	public String getTicket() {
		return ticket;
	}
	public void setTicket(String ticket) {
		this.ticket = ticket;
	}
	public String getUserId() {
		return userId;
	}
	public void setUserId(String userId) {
		this.userId = userId;
	}
	public String getContinuationData() {
		return continuationData;
	}
	public void setContinuationData(String continuationData) {
		this.continuationData = continuationData;
	}
	public String getFailureReason() {
		return failureReason;
	}
	public void setFailureReason(String failureReason) {
		this.failureReason = failureReason;
	}
	public long getPasswordExpirationTime() {
		return passwordExpirationTime;
	}
	public void setPasswordExpirationTime(long passwordExpirationTime) {
		this.passwordExpirationTime = passwordExpirationTime;
	}
	public boolean isContinuation() {
		return continuation;
	}
	public void setContinuation(boolean continuation) {
		this.continuation = continuation;
	}

    
}