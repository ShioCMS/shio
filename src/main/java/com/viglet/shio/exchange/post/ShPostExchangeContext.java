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
package com.viglet.shio.exchange.post;

import java.io.File;
import java.util.Map;

import com.viglet.shio.exchange.ShPostExchange;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.post.relator.impl.ShRelatorItemImpl;

/**
 * Post Exchange Context
 * 
 * @author Alexandre Oliveira
 * @since 0.3.7
 * 
 */
public class ShPostExchangeContext {
	private ShPostExchange shPostExchange;
	private ShPost shPost;
	private Map<String, Object> shPostFields;
	private ShRelatorItemImpl shParentRelatorItem;
	private File extractFolder;
	private String username;
	private Map<String, Object> shObjects;
	private boolean isCloned;

	public ShPostExchange getShPostExchange() {
		return shPostExchange;
	}

	public void setShPostExchange(ShPostExchange shPostExchange) {
		this.shPostExchange = shPostExchange;
	}

	public ShPost getShPost() {
		return shPost;
	}

	public void setShPost(ShPost shPost) {
		this.shPost = shPost;
	}

	public Map<String, Object> getShPostFields() {
		return shPostFields;
	}

	public void setShPostFields(Map<String, Object> shPostFields) {
		this.shPostFields = shPostFields;
	}

	public ShRelatorItemImpl getShParentRelatorItem() {
		return shParentRelatorItem;
	}

	public void setShParentRelatorItem(ShRelatorItemImpl shParentRelatorItem) {
		this.shParentRelatorItem = shParentRelatorItem;
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

	public Map<String, Object> getShObjects() {
		return shObjects;
	}

	public void setShObjects(Map<String, Object> shObjects) {
		this.shObjects = shObjects;
	}

	public boolean isCloned() {
		return isCloned;
	}

	public void setCloned(boolean isCloned) {
		this.isCloned = isCloned;
	}

}
