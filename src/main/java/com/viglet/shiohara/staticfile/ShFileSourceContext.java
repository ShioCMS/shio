package com.viglet.shiohara.staticfile;

import java.io.File;
import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.utils.ShStaticFileUtils;

@Controller
public class ShFileSourceContext {

	@Autowired
	ShStaticFileUtils shStaticFileUtils; 
	@RequestMapping("/file_source2/**")
	private void fileSource(HttpServletRequest request, HttpServletResponse response) {
		String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String[] contexts = url.split("/");
		String shContext = null;
		ArrayList<String> contentPath = new ArrayList<String>();
		for (int i = 1; i < contexts.length; i++) {
			switch (i) {
			case 1:
				shContext = contexts[i];
				break;
			default:
				contentPath.add(contexts[i]);
				break;
			}
		}
		ArrayList<String> filePathArray = contentPath;

		String filePath = null;
		
		if (filePathArray.size() == 0) {
			filePath = File.separator;
		} else {
			for (String path : filePathArray) {
				filePath = filePath + File.separator + path;
			}
		}
		
		File file = shStaticFileUtils.filePath(filePath);
		
		
	}

}
