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
package com.viglet.shio.website.nodejs.config;

import java.util.List;

/**
 * NodeJS App Config
 *
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
public class ShNodeJSConfigApp {

	private int port;

	private String templatePath;

	private List<String> sites;

	private String context;

	private String format;

	private String locale;

	private boolean hasContext;

	private boolean hasFormat;

	private boolean hasLocale;

	private boolean hasSiteName;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getTemplatePath() {
		return templatePath;
	}

	public void setTemplatePath(String templatePath) {
		this.templatePath = templatePath;
	}

	public List<String> getSites() {
		return sites;
	}

	public void setSites(List<String> sites) {
		this.sites = sites;
	}

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public String getFormat() {
		return format;
	}

	public void setFormat(String format) {
		this.format = format;
	}

	public String getLocale() {
		return locale;
	}

	public void setLocale(String locale) {
		this.locale = locale;
	}

	public boolean isHasContext() {
		return hasContext;
	}

	public void setHasContext(boolean hasContext) {
		this.hasContext = hasContext;
	}

	public boolean isHasFormat() {
		return hasFormat;
	}

	public void setHasFormat(boolean hasFormat) {
		this.hasFormat = hasFormat;
	}

	public boolean isHasLocale() {
		return hasLocale;
	}

	public void setHasLocale(boolean hasLocale) {
		this.hasLocale = hasLocale;
	}

	public boolean isHasSiteName() {
		return hasSiteName;
	}

	public void setHasSiteName(boolean hasSiteName) {
		this.hasSiteName = hasSiteName;
	}

}
