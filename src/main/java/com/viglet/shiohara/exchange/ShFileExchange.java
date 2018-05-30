package com.viglet.shiohara.exchange;

import java.io.File;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

@JsonIgnoreProperties(ignoreUnknown = true)
public class ShFileExchange {
	
	private String id;

	private File file;

	public String getId() {
		return id;
	}

	public void setId(String id) {
		this.id = id;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
