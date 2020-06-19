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
package com.viglet.shio.exchange;

import java.io.File;

/**
 * Post Exchange Context
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
public class ShExchangeContext {
	private File extractFolder;
	private String username;
	private boolean isCloned;

	public ShExchangeContext(File extractFolder, String username, boolean isCloned) {
		this.extractFolder = extractFolder;
		this.username = username;
		this.isCloned = isCloned;
	}

	public File getExtractFolder() {
		return extractFolder;
	}

	public void setExtractFolder(File extractFolder) {
		this.extractFolder = extractFolder;
	}

	public String getUsername() {
		return username;
	}

	public void setUsername(String username) {
		this.username = username;
	}

	public boolean isCloned() {
		return isCloned;
	}

	public void setCloned(boolean isCloned) {
		this.isCloned = isCloned;
	}

}
