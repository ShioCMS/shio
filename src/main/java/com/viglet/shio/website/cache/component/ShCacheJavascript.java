/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.website.cache.component;

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

/**
 * @author Alexandre Oliveira
 */
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
