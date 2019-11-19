package com.viglet.shiohara.provider.otcs.bean.result;

public class ShOTCSResultsDataColumnsBean {

	private int data_type;

	private String key;

	private String name;

	private String sort_key;

	private boolean include_time;

	public int getData_type() {
		return data_type;
	}

	public void setData_type(int data_type) {
		this.data_type = data_type;
	}

	public String getKey() {
		return key;
	}

	public void setKey(String key) {
		this.key = key;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getSort_key() {
		return sort_key;
	}

	public void setSort_key(String sort_key) {
		this.sort_key = sort_key;
	}

	public boolean isInclude_time() {
		return include_time;
	}

	public void setInclude_time(boolean include_time) {
		this.include_time = include_time;
	}

}
