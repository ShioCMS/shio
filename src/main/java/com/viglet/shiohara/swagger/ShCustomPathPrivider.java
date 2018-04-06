package com.viglet.shiohara.swagger;

import springfox.documentation.spring.web.paths.AbstractPathProvider;

public class ShCustomPathPrivider extends AbstractPathProvider {

	@Override
	protected String applicationPath() {
		return "/api/v2";
	}

	@Override
	protected String getDocumentationPath() {
		return "/api/v2";
	}

}
