package com.viglet.shiohara.object;

public enum ShObjectType {
	FOLDER("FOLDER"), POST("POST"), POST_TYPE("POST_TYPE"), SITE("SITE"), USER("USER") ;

	private String objectType;

	ShObjectType(String objectType) {
		this.objectType = objectType;
	}

	public String id() {
		return objectType;
	}

}
