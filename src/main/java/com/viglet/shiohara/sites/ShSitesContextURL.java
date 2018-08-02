package com.viglet.shiohara.sites;

import java.util.ArrayList;

import javax.servlet.http.HttpServletRequest;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
public class ShSitesContextURL {
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShFolderUtils shFolderUtils;

	private String shContext = null;
	private String contextURL = null;
	private ShSite shSite = null;
	private ShFolder shParentFolder = null;
	private String shFormat = null;
	private String shLocale = null;
	private int contextPathPosition = 0;
	private ShObject shObject = null;
	private boolean cacheEnabled = true;

	public void init(HttpServletRequest request) {
		String shXSiteName = request.getHeader("x-sh-site");
		cacheEnabled = request.getHeader("x-sh-nocache") != null && request.getHeader("x-sh-nocache").equals("1") ? false
				: true;
		String shSiteName = null;
		if (shXSiteName != null) {
			shSiteName = shXSiteName;
			shContext = "sites";
			shFormat = "default";
			shLocale = "en-us";
			contextPathPosition = 2;
			contextURL = "/sites/" + shXSiteName + "/" + shFormat + "/" + shLocale
					+ ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
							.replaceAll("^/sites/", "/");
		} else {
			contextPathPosition = 5;
			contextURL = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);

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
		}
		shSite = shSiteRepository.findByFurl(shSiteName);

		ArrayList<String> contentPath = shSitesContextComponent.contentPathFactory(this.getContextPathPosition(),
				request);

		String objectName = shSitesContextComponent.objectNameFactory(contentPath);

		shParentFolder = shFolderUtils.folderFromPath(shSite, shSitesContextComponent.folderPathFactory(contentPath));
		shObject = shSitesContextComponent.shObjectItemFactory(shSite, shParentFolder, objectName);
		ShPost shPost1 = (ShPost) shObject;
	}

	public ShSiteRepository getShSiteRepository() {
		return shSiteRepository;
	}

	public void setShSiteRepository(ShSiteRepository shSiteRepository) {
		this.shSiteRepository = shSiteRepository;
	}

	public String getShContext() {
		return shContext;
	}

	public void setShContext(String shContext) {
		this.shContext = shContext;
	}

	public String getContextURL() {
		return contextURL;
	}

	public void setContextURL(String contextURL) {
		this.contextURL = contextURL;
	}

	public ShSite getShSite() {
		return shSite;
	}

	public void setShSite(ShSite shSite) {
		this.shSite = shSite;
	}

	public String getShFormat() {
		return shFormat;
	}

	public void setShFormat(String shFormat) {
		this.shFormat = shFormat;
	}

	public String getShLocale() {
		return shLocale;
	}

	public void setShLocale(String shLocale) {
		this.shLocale = shLocale;
	}

	public int getContextPathPosition() {
		return contextPathPosition;
	}

	public void setContextPathPosition(int contextPathPosition) {
		this.contextPathPosition = contextPathPosition;
	}

	public ShFolder getShParentFolder() {
		return shParentFolder;
	}

	public void setShParentFolder(ShFolder shParentFolder) {
		this.shParentFolder = shParentFolder;
	}

	public ShObject getShObject() {
		return shObject;
	}

	public void setShObject(ShObject shObject) {
		this.shObject = shObject;
	}

	public boolean isCacheEnabled() {
		return cacheEnabled;
	}

	public void setCacheEnabled(boolean cacheEnabled) {
		this.cacheEnabled = cacheEnabled;
	}

}
