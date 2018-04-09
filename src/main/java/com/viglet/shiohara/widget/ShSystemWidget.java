package com.viglet.shiohara.widget;

public enum ShSystemWidget {
	TEXT("Text"), 
	ACE_JS("Ace Editor - Javascript"), 
	ACE_HTML("Ace Editor - HTML"), 
	HTML_EDITOR("HTML Editor"), 
	FILE("File"), 
	TEXT_AREA("Text Area");

	private String systemWidget;

	ShSystemWidget(String systemWidget) {
		this.systemWidget = systemWidget;
	}

	public String id() {
		return systemWidget;
	}

}
