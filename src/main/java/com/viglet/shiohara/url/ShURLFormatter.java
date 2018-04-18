package com.viglet.shiohara.url;

import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

@Component
public class ShURLFormatter {

	public String format(String URL) {

		String formattedURL = URL.toLowerCase();		
		formattedURL = formattedURL.replaceAll("&.+?;", " "); // kill entities
		formattedURL = StringUtils.stripAccents(formattedURL);
		formattedURL = formattedURL.replaceAll("\\.", "-");
		formattedURL = formattedURL.replaceAll("[^a-z0-9 _-]", "");
		formattedURL = formattedURL.replaceAll("\\s+", "-");
		formattedURL = StringUtils.strip(formattedURL, " -");
		return formattedURL;
	}
}
