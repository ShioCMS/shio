package com.viglet.shiohara.auth.otds.provider.bean;

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