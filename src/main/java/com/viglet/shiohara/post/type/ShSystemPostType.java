package com.viglet.shiohara.post.type;

public enum ShSystemPostType {
	TEXT("PT-TEXT"), 
	PHOTO("PT-PHOTO"), 
	VIDEO("PT-VIDEO"), 
	QUOTE("PT-QUOTE"), 
	LINK("PT-LINK"), 
	FILE("PT-FILE"), 
	TEXT_AREA("PT-TEXT-AREA"), 
	ARTICLE("PT-ARTICLE"), 
	REGION("PT-REGION"), 
	THEME("PT-THEME"), 
	PAGE_LAYOUT("PT-PAGE-LAYOUT"), 
	FOLDER_INDEX("PT-FOLDER-INDEX");

	private String objectType;

	ShSystemPostType(String objectType) {
		this.objectType = objectType;
	}

	public String id() {
		return objectType;
	}

}
