package com.viglet.shiohara.sites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.annotation.Resource;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.cache.ShCacheManager;
import com.viglet.shiohara.cache.ShCachedObject;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
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
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ResourceLoader resourceloader;

	@Resource
	private ApplicationContext context;

	@RequestMapping("/sites/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ScriptException {
		String shXSiteName = request.getHeader("x-sh-site");
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

	private StringBuilder shObjectJSFactory() throws IOException {
		InputStreamReader isr = new InputStreamReader(
				resourceloader.getResource("classpath:/js/server-side/shObject.js").getInputStream());

		StringBuilder shObjectJS = new StringBuilder();
		try (Reader reader = new BufferedReader(isr)) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				shObjectJS.append((char) c);
			}
		}
		return shObjectJS;
	}

	private String folderPathFactory(ArrayList<String> contentPath) {
		String folderPath = "/";
		if (contentPath.size() >= 1) {
			ArrayList<String> folderPathArray = contentPath;

			// Remove PostName
			folderPathArray.remove(folderPathArray.size() - 1);

			for (String path : folderPathArray) {
				folderPath = folderPath + path + "/";
			}

		}
		return folderPath;
	}

	private ArrayList<String> contentPathFactory(int contextPathPosition, HttpServletRequest request) {
		String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String[] contexts = url.split("/");
		ArrayList<String> contentPath = new ArrayList<String>();

		for (int i = contextPathPosition; i < contexts.length; i++) {
			contentPath.add(contexts[i]);
		}
		return contentPath;
	}

	private ShPost shPostAlias(ShPost shPostItem) {
		if (shPostItem.getShPostType().getName().equals(ShSystemPostType.ALIAS)) {
			List<ShPostAttr> shPostAttrs = shPostItem.getShPostAttrs();
			for (ShPostAttr shPostAttr : shPostAttrs) {
				if (shPostAttr.getShPostTypeAttr().getName().equals(ShSystemPostTypeAttr.CONTENT)) {
					shPostItem = shPostRepository.findById(UUID.fromString(shPostAttr.getStrValue())).get();
				}
			}
		}
		return shPostItem;
	}

	private String objectNameFactory(ArrayList<String> contentPath) {
		String objectName = null;
		int lastPosition = contentPath.size() - 1;
		if (contentPath.size() >= 1) {
			objectName = contentPath.get(lastPosition);
		}
		return objectName;
	}

	private ShPost shPostItemFactory(ShSite shSite, String folderPath, String objectName) {
		ShPost shPostItem = null;

		// If shPostItem is not null, so is a Post, otherwise is a Folder
		if (objectName != null) {
			ShFolder shParentFolder = shFolderUtils.folderFromPath(shSite, folderPath);
			shPostItem = shPostRepository.findByShFolderAndFurl(shParentFolder, objectName);
		}

		if (shPostItem != null) {
			shPostItem = this.shPostAlias(shPostItem);
		} else {
			String folderPathCurrent = folderPath;
			if (objectName != null) {
				folderPathCurrent = folderPathCurrent + objectName + "/";
			}
			ShFolder shFolderItem = shFolderUtils.folderFromPath(shSite, folderPathCurrent);
			if (shFolderItem != null) {
				// System.out.println("shFolderItem is not null");
				ShPost shFolderIndex = shPostRepository.findByShFolderAndFurl(shFolderItem, "index");

				if (shFolderIndex != null) {
					shPostItem = shFolderIndex;
				}
			}
		}
		return shPostItem;
	}

	private JSONObject shThemeFactory(String themeName) {
		ShPost shTheme = shPostRepository.findByTitle(themeName);

		Map<String, ShPostAttr> shThemeMap = shPostUtils.postToMap(shTheme);

		JSONObject shThemeAttrs = new JSONObject();
		shThemeAttrs.put("javascript", shThemeMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());
		shThemeAttrs.put("css", shThemeMap.get(ShSystemPostTypeAttr.CSS).getStrValue());

		return shThemeAttrs;
	}

	private JSONArray shPostItemsFactory(ShFolder shFolderItem) {
		JSONArray shPostItems = new JSONArray();
		List<ShPost> shPosts = shPostRepository.findByShFolder(shFolderItem);

		for (ShPost shPost : shPosts) {
			if (!shPost.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
				JSONObject shPostItemAttrs = shPostUtils.toJSON(shPost);
				shPostItems.put(shPostItemAttrs);
			}
		}

		return shPostItems;
	}

	private JSONArray shChildFolderItemsFactory(ShFolder shFolderItem) {
		JSONArray shChildFolderItems = new JSONArray();
		List<ShFolder> shFolders = shFolderRepository.findByParentFolder(shFolderItem);

		for (ShFolder shChildFolder : shFolders) {
			shChildFolderItems.put(shFolderUtils.toJSON(shChildFolder));
		}
		return shChildFolderItems;
	}

	private ShFolder shFolderItemFactory(ShPost shPostItem) {
		ShFolder shFolderItem = null;
		if (shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
			shFolderItem = shPostItem.getShFolder();
		}

		return shFolderItem;
	}

	private Map<String, ShPostAttr> shFolderPageLayoutMapFactory(ShPost shPostItem) {
		Map<String, ShPostAttr> shFolderIndexMap = shPostUtils.postToMap(shPostItem);

		ShPost shFolderPageLayout = shPostRepository
				.findByTitle(shFolderIndexMap.get(ShSystemPostTypeAttr.PAGE_LAYOUT).getStrValue());

		return shPostUtils.postToMap(shFolderPageLayout);
	}

	private String shPageLayoutFactory(String javascriptVar, String pageLayoutJS,
			String pageLayoutHTML) throws ScriptException, IOException {
	
		StringBuilder shObjectJS = this.shObjectJSFactory();

		String javascript = javascriptVar + pageLayoutJS;

		// Nashorn Engine
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = engine.createBindings();
		bindings.put("html", pageLayoutHTML);
		bindings.put("spring", context);
		Object pageLayoutResult = engine.eval(javascript, bindings);

		Document doc = Jsoup.parse(pageLayoutResult.toString());

		Elements elements = doc.getElementsByAttribute("sh-region");

		// Regions
		for (Element element : elements) {

			String regionAttr = element.attr("sh-region");

			ShPost shRegionPost = shPostRepository.findByTitle(regionAttr);

			Map<String, ShPostAttr> shRegionPostMap = shPostUtils.postToMap(shRegionPost);

			String shRegionJS = shRegionPostMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

			String shRegionHTML = shRegionPostMap.get(ShSystemPostTypeAttr.HTML).getStrValue();

			bindings.put("html", shRegionHTML);

			javascript = shObjectJS.toString() + javascriptVar + shRegionJS;

			Object regionResult = engine.eval(javascript, bindings);

			element.html(regionResult.toString());
		}
		return doc.html();
	}

	private String siteContext(String shSiteName, String shFormat, String shLocale, int contextPathPosition,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ScriptException {

		ArrayList<String> contentPath = this.contentPathFactory(contextPathPosition, request);

		ShSite shSite = shSiteRepository.findByFurl(shSiteName);

		String objectName = this.objectNameFactory(contentPath);

		String folderPath = this.folderPathFactory(contentPath);

		ShPost shPostItem = this.shPostItemFactory(shSite, folderPath, objectName);

		String javascriptVar = null;

		String pageLayoutHTML = null;

		String pageLayoutJS = null;

		// Folder
		if (shPostItem != null && shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {

			ShFolder shFolderItem = this.shFolderItemFactory(shPostItem);

			Map<String, ShPostAttr> shFolderPageLayoutMap = this.shFolderPageLayoutMapFactory(shPostItem);

			pageLayoutHTML = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
			pageLayoutJS = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

			JSONObject shThemeAttrs = this
					.shThemeFactory(shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue());

			JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);
			shPostItemAttrs.put("theme", shThemeAttrs);

			JSONObject shFolderItemAttrs = shFolderUtils.toJSON(shFolderItem);

			shFolderItemAttrs.put("theme", shThemeAttrs);
			shFolderItemAttrs.put("posts", this.shPostItemsFactory(shFolderItem));
			shFolderItemAttrs.put("folders", this.shChildFolderItemsFactory(shFolderItem));
			shFolderItemAttrs.put("post", shPostItemAttrs);
			shFolderItemAttrs.put("site", shSiteUtils.toJSON(shSite));

			javascriptVar = "var shContent = " + shFolderItemAttrs.toString() + ";";

		} else {
			// Post
			JSONObject postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
			String pageLayoutName = (String) postTypeLayout.get(shPostItem.getShPostType().getName());
			ShPost shPostPageLayout = shPostRepository.findByTitle(pageLayoutName);

			Map<String, ShPostAttr> shPostPageLayoutMap = shPostUtils.postToMap(shPostPageLayout);

			pageLayoutHTML = shPostPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
			pageLayoutJS = shPostPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

			JSONObject shThemeAttrs = this
					.shThemeFactory(shPostPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue());

			JSONObject shSiteItemAttrs = shSiteUtils.toJSON(shSite);

			JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);

			shPostItemAttrs.put("theme", shThemeAttrs);
			shPostItemAttrs.put("site", shSiteItemAttrs);

			javascriptVar = "var shContent = " + shPostItemAttrs.toString() + ";";
		
		}
			
		String shPageLayoutHTML = this.shPageLayoutFactory(javascriptVar, pageLayoutJS, pageLayoutHTML);

		response.setContentType(MediaType.TEXT_HTML_VALUE);

		response.getWriter().write(shPageLayoutHTML);

		return shPageLayoutHTML;
	}
}
