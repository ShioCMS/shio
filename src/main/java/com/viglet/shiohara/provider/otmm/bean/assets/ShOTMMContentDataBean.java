package com.viglet.shiohara.provider.otmm.bean.assets;

import com.fasterxml.jackson.annotation.JsonProperty;

public class ShOTMMContentDataBean {

	@JsonProperty("data_source")
	private String dataSource;

	@JsonProperty("temp_file")
	private boolean tempFile;

	public String getDataSource() {
		return dataSource;
	}

	public void setDataSource(String dataSource) {
		this.dataSource = dataSource;
	}

	public boolean isTempFile() {
		return tempFile;
	}

	public void setTempFile(boolean tempFile) {
		this.tempFile = tempFile;
	}

	
}
