package com.viglet.shiohara.provider.exchange.otcs.bean.result;

import java.util.List;
import java.util.Map;

public class ShOTCSResultsDataBean {

	private List<Map<String, Object>> categories;

	private List<Map<String, Boolean>> categories_inheritance;

	private List<ShOTCSResultsDataColumnsBean> columns;

	private List<String> followups;
	
	private List<ShOTCSNicknameBean> nicknames;

	private List<ShOTCSPermissionsBean> permissions;

	private ShOTCSPropertiesBean properties;

	private ShOTCSRMIConsDataBean rmiconsdata;

	private Map<String, String> systemattributes;

	private List<ShOTCSVersionsBean> versions;	


	public List<Map<String, Object>> getCategories() {
		return categories;
	}

	public void setCategories(List<Map<String, Object>> categories) {
		this.categories = categories;
	}

	public List<Map<String, Boolean>> getCategories_inheritance() {
		return categories_inheritance;
	}

	public void setCategories_inheritance(List<Map<String, Boolean>> categories_inheritance) {
		this.categories_inheritance = categories_inheritance;
	}

	public List<ShOTCSResultsDataColumnsBean> getColumns() {
		return columns;
	}

	public void setColumns(List<ShOTCSResultsDataColumnsBean> columns) {
		this.columns = columns;
	}

	public List<ShOTCSNicknameBean> getNicknames() {
		return nicknames;
	}

	public void setNicknames(List<ShOTCSNicknameBean> nicknames) {
		this.nicknames = nicknames;
	}

	public List<ShOTCSPermissionsBean> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<ShOTCSPermissionsBean> permissions) {
		this.permissions = permissions;
	}

	public ShOTCSPropertiesBean getProperties() {
		return properties;
	}

	public void setProperties(ShOTCSPropertiesBean properties) {
		this.properties = properties;
	}

	public ShOTCSRMIConsDataBean getRmiconsdata() {
		return rmiconsdata;
	}

	public void setRmiconsdata(ShOTCSRMIConsDataBean rmiconsdata) {
		this.rmiconsdata = rmiconsdata;
	}

	public Map<String, String> getSystemattributes() {
		return systemattributes;
	}

	public void setSystemattributes(Map<String, String> systemattributes) {
		this.systemattributes = systemattributes;
	}

	public List<ShOTCSVersionsBean> getVersions() {
		return versions;
	}

	public void setVersions(List<ShOTCSVersionsBean> versions) {
		this.versions = versions;
	}

	public List<String> getFollowups() {
		return followups;
	}

	public void setFollowups(List<String> followups) {
		this.followups = followups;
	}

}
