/*
 * Copyright (C) 2016-2021 the original author or authors. 
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
package com.viglet.shio.exchange.folder;

import java.io.File;

import com.viglet.shio.exchange.ShExchangeObjectMap;

/**
 * Folder Exchange Context
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
public class ShFolderExchangeContext {
	private ShFolderExchange shFolderExchange;
	private File extractFolder;
	String username;
	private String shObject;
	private boolean importOnlyFolders;
	private ShExchangeObjectMap shExchangeObjectMap;

	private boolean isCloned;

	public ShFolderExchange getShFolderExchange() {
		return shFolderExchange;
	}

	public void setShFolderExchange(ShFolderExchange shFolderExchange) {
		this.shFolderExchange = shFolderExchange;
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

	public String getShObject() {
		return shObject;
	}

	public void setShObject(String shObject) {
		this.shObject = shObject;
	}

	public boolean isImportOnlyFolders() {
		return importOnlyFolders;
	}

	public void setImportOnlyFolders(boolean importOnlyFolders) {
		this.importOnlyFolders = importOnlyFolders;
	}

	public boolean isCloned() {
		return isCloned;
	}

	public void setCloned(boolean isCloned) {
		this.isCloned = isCloned;
	}

	public ShExchangeObjectMap getShExchangeObjectMap() {
		return shExchangeObjectMap;
	}

	public void setShExchangeObjectMap(ShExchangeObjectMap shExchangeObjectMap) {
		this.shExchangeObjectMap = shExchangeObjectMap;
	}

}
