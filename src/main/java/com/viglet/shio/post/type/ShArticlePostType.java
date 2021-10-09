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
package com.viglet.shio.post.type;

import java.util.HashMap;
import java.util.Map;

import com.viglet.shio.exchange.post.ShPostExchange;

/**
 * Article Post Type 
 * 
 * @author Alexandre Oliveira
 * @since 0.3.4
 * 
 */
public class ShArticlePostType extends ShAbstractPostType {

	private String title;

	private String description;

	private String text;

	private String filePath;

	private String videoURL;

	public String getTitle() {
		return title;
	}

	public void setTitle(String title) {
		this.title = title;
	}

	public String getDescription() {
		return description;
	}

	public void setDescription(String description) {
		this.description = description;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getFilePath() {
		return filePath;
	}

	public void setFilePath(String filePath) {
		this.filePath = filePath;
	}

	public String getVideoURL() {
		return videoURL;
	}

	public void setVideoURL(String videoURL) {
		this.videoURL = videoURL;
	}

	@Override
	public ShPostExchange getShPostExchange() {
		ShPostExchange shPostExchange = super.getShPostExchange();
		shPostExchange.setPostType(ShSystemPostType.ARTICLE);
		Map<String, Object> fields = new HashMap<>();
		fields.put("TITLE", this.getTitle());
		fields.put("DESCRIPTION", this.getDescription());
		fields.put("TEXT", this.getText());
		fields.put("FILE", this.getFilePath());
		fields.put("VIDEO", this.getVideoURL());

		shPostExchange.setFields(fields);

		return shPostExchange;
	}
}
