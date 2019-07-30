package com.viglet.shiohara.image;

import java.io.IOException;

import java.util.StringJoiner;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.utils.ShStaticFileUtils;

import net.coobird.thumbnailator.Thumbnails;

@Controller
public class ShImageResize {
	private static final Log logger = LogFactory.getLog(ShImageResize.class);
	@Autowired
	ShStaticFileUtils shStaticFileUtils;

	@RequestMapping("/image/{type}/{value}/**")
	public void resize(HttpServletRequest request, HttpServletResponse response, @PathVariable String type,
			@PathVariable String value) {
		try {
			String url = ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
					.replaceAll("^/image", "");

			String[] contexts = url.split("/");
			StringJoiner path = new StringJoiner("/");

			for (int i = 3; i < contexts.length; i++) {
				path.add(contexts[i]);
			}
		
			String filePath = String.format("/%s", path.toString());
			response.setContentType(MediaType.IMAGE_PNG_VALUE);
			if (type.equals("scale")) {
				double valueDouble = Double.valueOf(value);
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
