package com.viglet.shiohara.provider.otmm.bean.permission;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMAccessControlDescriptorBean {

	@JsonProperty("permissions_map")
	private ShOTMMPermissionMapBean permissionsMap;

	public ShOTMMPermissionMapBean getPermissionsMap() {
		return permissionsMap;
	}

	public void setPermissionsMap(ShOTMMPermissionMapBean permissionsMap) {
		this.permissionsMap = permissionsMap;
	}

}
