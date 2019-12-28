package com.viglet.shiohara.provider.exchange.otcs.bean.result;

import java.util.List;

public class ShOTCSPermissionsBean {

	private List<String> permissions;

	private int right_id;
	
	private String type;
	
	public List<String> getPermissions() {
		return permissions;
	}

	public void setPermissions(List<String> permissions) {
		this.permissions = permissions;
	}

	public int getRight_id() {
		return right_id;
	}

	public void setRight_id(int right_id) {
		this.right_id = right_id;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}
	
}
