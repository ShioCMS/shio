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
package com.viglet.shio.image;

import java.io.IOException;

import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shio.utils.ShStaticFileUtils;

import net.coobird.thumbnailator.Thumbnails;

/**
 * @author Alexandre Oliveira
 */
@Controller
public class ShImageResize {
	private static final Log logger = LogFactory.getLog(ShImageResize.class);
	@Autowired
	ShStaticFileUtils shStaticFileUtils;

	@GetMapping("/image/{type}/{value}/**")
	public void resize(HttpServletRequest request, HttpServletResponse response, @PathVariable String type,
			@PathVariable String value) {
		try {
			String url = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
					.replaceAll("^/image", StringUtils.EMPTY);

			String[] contexts = url.split("/");
			StringJoiner path = new StringJoiner("/");

			for (int i = 3; i < contexts.length; i++) {
				path.add(contexts[i]);
			}
		
			String filePath = String.format("/%s", path.toString());
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
			if (type.equals("scale")) {
				double valueDouble = Double.parseDouble(value);
				double percent = valueDouble / 100;
				Thumbnails.of(shStaticFileUtils.filePath(filePath)).scale(percent).outputFormat("png").outputQuality(1)
						.toOutputStream(response.getOutputStream());
			} else {
				Thumbnails.of(shStaticFileUtils.filePath(filePath)).scale(1).outputFormat("png").outputQuality(1)
						.toOutputStream(response.getOutputStream());
			}

		} catch (IOException e) {
			logger.error("Image Resize Error", e);
		}
	}
}
