package com.viglet.shiohara.provider.otmm.bean.sessions;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMSessionsSessionBean {

	@JsonProperty("domain_name")
	private String domainName;

	private int id;

	@JsonProperty("local_session")
	private boolean localSession;

	@JsonProperty("login_name")
	private String loginName;

	@JsonProperty("message_digest")
	private String messageDigest;

	@JsonProperty("role_name")
	private String roleName;

	@JsonProperty("user_full_name")
	private String userFullName;

	@JsonProperty("user_id")
	private int userId;

	@JsonProperty("user_role_id")
	private int userRoleId;

	@JsonProperty("validation_key")
	private String validationKey;

	public String getDomainName() {
		return domainName;
	}

	public void setDomainName(String domainName) {
		this.domainName = domainName;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public boolean isLocalSession() {
		return localSession;
	}

	public void setLocalSession(boolean localSession) {
		this.localSession = localSession;
	}

	public String getLoginName() {
		return loginName;
	}

	public void setLoginName(String loginName) {
		this.loginName = loginName;
	}

	public String getMessageDigest() {
		return messageDigest;
	}

	public void setMessageDigest(String messageDigest) {
		this.messageDigest = messageDigest;
	}

	public String getRoleName() {
		return roleName;
	}

	public void setRoleName(String roleName) {
		this.roleName = roleName;
	}

	public String getUserFullName() {
		return userFullName;
	}

	public void setUserFullName(String userFullName) {
		this.userFullName = userFullName;
	}

	public int getUserId() {
		return userId;
	}

	public void setUserId(int userId) {
		this.userId = userId;
	}

	public int getUserRoleId() {
		return userRoleId;
	}

	public void setUserRoleId(int userRoleId) {
		this.userRoleId = userRoleId;
	}

	public String getValidationKey() {
		return validationKey;
	}

	public void setValidationKey(String validationKey) {
		this.validationKey = validationKey;
	}

}
