package com.viglet.shiohara.sites.cache.component;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.annotation.Cacheable;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

@Component
public class ShCacheJavascript {
	static final Logger logger = LogManager.getLogger(ShCacheJavascript.class.getName());
	@Autowired
	private ResourceLoader resourceloader;

	@Cacheable(value = "javascript", sync = true)
	public StringBuilder shObjectJSFactory() throws IOException {
		logger.debug("Executando shObjectJSFactory");
		InputStreamReader isrObjectJS = null;
		InputStreamReader isrHandlebars = null;
		StringBuilder shObjectJS = new StringBuilder();
		try {
			isrObjectJS = new InputStreamReader(
					resourceloader.getResource("classpath:/js/server-side/shObject.js").getInputStream());
			isrHandlebars = new InputStreamReader(
					resourceloader.getResource("classpath:/js/server-side/handlebars.min.js").getInputStream());

			try (Reader reader = new BufferedReader(isrObjectJS)) {
				int c = 0;
				while ((c = reader.read()) != -1) {
					shObjectJS.append((char) c);
				}
			}

			try (Reader reader = new BufferedReader(isrHandlebars)) {
				int c = 0;
				while ((c = reader.read()) != -1) {
					shObjectJS.append((char) c);
				}
			}

		} catch (Exception e) {
			logger.error("shObjectJSFactory Error", e);
		} finally {
			if (isrObjectJS != null)
				isrObjectJS.close();
			if (isrHandlebars != null)
				isrHandlebars.close();

		}
		return shObjectJS;
	}

}
