package com.viglet.shiohara.sites;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.cache.ShCacheManager;
import com.viglet.shiohara.cache.ShCachedObject;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;

@Controller
public class ShSitesContext {
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;

	@RequestMapping("/sites/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ScriptException {
		String shXSiteName = request.getHeader("x-sh-site");
		boolean shXNoCache = request.getHeader("x-sh-nocache") != null && request.getHeader("x-sh-nocache").equals("1")
				? true
				: false;
		final int cacheMinutes = 1;
		if (shXSiteName != null) {
			String shFormat = "default";
			String shLocale = "en-us";
			String contextURL = "/sites/" + shXSiteName + "/" + shFormat + "/" + shLocale
					+ ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE))
							.replaceAll("^/sites/", "/");
			ShCachedObject shCacheObject = (ShCachedObject) ShCacheManager.getCache(contextURL);
			if (shCacheObject != null) {
				// End Page Layout
				// System.out.println("Is cached " + contextURL);
				response.setContentType(MediaType.TEXT_HTML_VALUE);
				response.getWriter().write(((String) shCacheObject.object).toString());
			} else {
				// System.out.println("Is not cached " + contextURL);
				String html = this.siteContext(shXSiteName, "default", "en-us", 2, request, response);
				shCacheObject = new ShCachedObject(html, contextURL, cacheMinutes);
				/* Place the object into the cache! */
				ShCacheManager.putCache(shCacheObject);
			}
		} else {

			@SuppressWarnings("unused")
			String shContext = null;
			String contextURL = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
			String shSiteName = null;
			String shFormat = null;
			String shLocale = null;
			String[] contexts = contextURL.split("/");

			ShCachedObject shCacheObject = (ShCachedObject) ShCacheManager.getCache(contextURL);
			if (shXNoCache) {
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
				this.siteContext(shSiteName, shFormat, shLocale, 5, request, response);
			} else {
				if (shCacheObject != null) {
					// End Page Layout
					// System.out.println("Is cached " + contextURL);
					response.setContentType(MediaType.TEXT_HTML_VALUE);
					response.getWriter().write(((String) shCacheObject.object).toString());
				} else {

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
					// System.out.println("Is not cached " + contextURL);
					String html = this.siteContext(shSiteName, shFormat, shLocale, 5, request, response);
					shCacheObject = new ShCachedObject(html, contextURL, cacheMinutes);
					/* Place the object into the cache! */
					ShCacheManager.putCache(shCacheObject);
				}
			}
		}
	}

	public String siteContext(String shSiteName, String shFormat, String shLocale, int contextPathPosition,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ScriptException {

		ArrayList<String> contentPath = shSitesContextComponent.contentPathFactory(contextPathPosition, request);

		ShSite shSite = shSiteRepository.findByFurl(shSiteName);

		String objectName = shSitesContextComponent.objectNameFactory(contentPath);

		String folderPath = shSitesContextComponent.folderPathFactory(contentPath);
		ShFolder shFolder = shFolderUtils.folderFromPath(shSite, folderPath);
		ShObject shObjectItem = shSitesContextComponent.shObjectItemFactory(shSite, shFolder, objectName);

		String javascriptVar = null;

		String pageLayoutHTML = null;

		String pageLayoutJS = null;

		// Folder
		if (shObjectItem instanceof ShFolder) {
			ShFolder shFolderItem = (ShFolder) shObjectItem;

			Map<String, ShPostAttr> shFolderPageLayoutMap = shSitesContextComponent
					.shFolderPageLayoutMapFactory(shFolderItem, shSite);

			pageLayoutHTML = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
			pageLayoutJS = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

			String shPostThemeId = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
			JSONObject shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

			JSONObject shPostItemAttrs = new JSONObject();

			shPostItemAttrs.put("theme", shThemeAttrs);

			JSONObject shFolderItemAttrs = shFolderUtils.toJSON(shFolderItem);

			shFolderItemAttrs.put("theme", shThemeAttrs);
			shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
			shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
			shFolderItemAttrs.put("post", shPostItemAttrs);
			shFolderItemAttrs.put("site", shSiteUtils.toJSON(shSite));

			javascriptVar = "var shContent = " + shFolderItemAttrs.toString() + ";";
		}
		// Post
		else if (shObjectItem instanceof ShPost) {
			ShPost shPostItem = (ShPost) shObjectItem;

			if (shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
				// Folder Index
				ShFolder shFolderItem = null;
				shFolderItem = shSitesContextComponent.shFolderItemFactory(shPostItem);

				Map<String, ShPostAttr> shFolderPageLayoutMap = shSitesContextComponent
						.shFolderPageLayoutMapFactory(shPostItem, shSite);

				pageLayoutHTML = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
				pageLayoutJS = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

				String shPostThemeId = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
				JSONObject shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

				JSONObject shPostItemAttrs = new JSONObject();

				shPostItemAttrs.put("theme", shThemeAttrs);

				JSONObject shFolderItemAttrs = shFolderUtils.toJSON(shFolderItem);

				shFolderItemAttrs.put("theme", shThemeAttrs);
				shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
				shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
				shFolderItemAttrs.put("post", shPostItemAttrs);
				shFolderItemAttrs.put("site", shSiteUtils.toJSON(shSite));

				javascriptVar = "var shContent = " + shFolderItemAttrs.toString() + ";";

			} else {
				// Other Post
				JSONObject postTypeLayout = new JSONObject();

				if (shSite.getPostTypeLayout() != null) {
					postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
				}

				String pageLayoutName = (String) postTypeLayout.get(shPostItem.getShPostType().getName());
				List<ShPost> shPostPageLayouts = shPostRepository.findByTitle(pageLayoutName);

				Map<String, ShPostAttr> shPostPageLayoutMap = null;

				if (shPostPageLayouts != null) {
					for (ShPost shPostPageLayout : shPostPageLayouts) {
						if (shPostUtils.getSite(shPostPageLayout).getId().equals(shSite.getId())) {
							shPostPageLayoutMap = shPostUtils.postToMap(shPostPageLayout);

						}
					}
				}

				if (shPostPageLayoutMap != null) {

					pageLayoutHTML = shPostPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
					pageLayoutJS = shPostPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

					String shPostThemeId = shPostPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
					JSONObject shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

					JSONObject shSiteItemAttrs = shSiteUtils.toJSON(shSite);

					JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);

					shPostItemAttrs.put("theme", shThemeAttrs);
					shPostItemAttrs.put("site", shSiteItemAttrs);

					javascriptVar = "var shContent = " + shPostItemAttrs.toString() + ";";

				}

			}
		}
		String shPageLayoutHTML = shSitesContextComponent.shPageLayoutFactory(javascriptVar, pageLayoutJS,
				pageLayoutHTML, request, shSite);

		response.setContentType(MediaType.TEXT_HTML_VALUE);

		response.getWriter().write(shPageLayoutHTML);

		return shPageLayoutHTML;

	}
}
