package com.viglet.shiohara.auth.otds.provider.bean;

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
