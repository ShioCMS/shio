package com.viglet.shiohara.exchange;

import java.io.File;
import java.util.UUID;

public class ShFileExchange {
	
	private UUID globalId;
	
	private UUID id;

	private File file;

	public UUID getGlobalId() {
		return globalId;
	}

	public void setGlobalId(UUID globalId) {
		this.globalId = globalId;
	}

	public UUID getId() {
		return id;
	}

	public void setId(UUID id) {
		this.id = id;
	}

	public File getFile() {
		return file;
	}

	public void setFile(File file) {
		this.file = file;
	}

}
