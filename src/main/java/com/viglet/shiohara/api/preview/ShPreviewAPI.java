package com.viglet.shiohara.api.preview;

import java.net.URL;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.sites.ShSitesContextURL;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/preview")
@Api(tags = "Preview", description = "Preview API")
public class ShPreviewAPI {
	@Autowired
	ShSitesContextURL shSitesContextURL;

	@ApiOperation(value = "Detect URL")
	@PostMapping("/detect-url")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShObject shPreviewDetectURL(HttpServletRequest request) throws Exception {
		URL url = new URL(request.getParameter("url"));
		shSitesContextURL.byURL(url);

		return shSitesContextURL.getShObject();
	}
}