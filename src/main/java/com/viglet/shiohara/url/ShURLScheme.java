package com.viglet.shiohara.url;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.utils.ShFolderUtils;

@Controller
public class ShURLScheme {
	@Autowired
	private HttpServletRequest request;
	@Autowired
	private ShFolderUtils shFolderUtils;

	public String get(ShObject shObject) {
		String shContext = "sites";
		String shFormat = "default";
		String shLocale = "en_US";
		String shSiteName = null;
		if (shObject.getShGlobalId().getType().equals(ShObjectType.SITE)) {
			ShSite shSite = (ShSite) shObject;
			shSiteName = shSite.getName();
		} else if (shObject.getShGlobalId().getType().equals(ShObjectType.FOLDER)) {
			ShFolder shFolder = (ShFolder) shObject;
			shSiteName = shFolderUtils.getSite(shFolder).getName();
		} else if (shObject.getShGlobalId().getType().equals(ShObjectType.POST)) {
			ShPost shPost = (ShPost) shObject;
			ShFolder shFolder = shPost.getShFolder();
			shSiteName = shFolderUtils.getSite(shFolder).getName();
		}

		String url = "/" + shContext + "/" + shSiteName.replaceAll(" ", "-") + "/" + shFormat + "/" + shLocale;

		return url;
	}

	public String get() {
		String shSiteName = request.getHeader("x-sh-site");
		String url = null;
		if (shSiteName != null) {
			url = "";
		} else {
			String contextURL = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String shContext = null;
			String shFormat = null;
			String shLocale = null;
			String[] contexts = contextURL.split("/");

			for (int i = 1; i < contexts.length; i++) {
				switch (i) {
				case 1:
					shContext = contexts[i];
					break;
				case 2:
					shSiteName = contexts[i];
					break;
				case 3:
					shFormat = contexts[i];
					break;
				case 4:
					shLocale = contexts[i];
					break;
				}
			}

			url = "/" + shContext + "/" + shSiteName.replaceAll(" ", "-") + "/" + shFormat + "/" + shLocale;
		}
		return url;
	}
}
