package com.viglet.shiohara.provider.otcs.bean.result;

import java.util.List;
import java.util.Map;

public class ShOTCSResultsDataBean {

	private String[] categories;

	private String[] categories_inheritance;

	private List<ShOTCSResultsDataColumnsBean> columns;

	private List<ShOTCSNicknameBean> nicknames;

	private List<ShOTCSPermissionsBean> permissions;

	private ShOTCSPropertiesBean properties;

	private ShOTCSRMIConsDataBean rmiconsdata;

	private Map<String, String> systemattributes;

	private List<ShOTCSVersionsBean> versions;

	public String[] getCategories() {
		return categories;
	}

	public void setCategories(String[] categories) {
		this.categories = categories;
	}

	public String[] getCategories_inheritance() {
		return categories_inheritance;
	}

	public void setCategories_inheritance(String[] categories_inheritance) {
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

}
