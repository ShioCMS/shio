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
					+ ((String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE)).replaceAll("^/sites/", "/");
			ShCachedObject shCacheObject = (ShCachedObject) ShCacheManager.getCache(contextURL);
			if (shCacheObject != null) {
				// End Page Layout
				//System.out.println("Is cached " + contextURL);
				response.setContentType(MediaType.TEXT_HTML_VALUE);
				response.getWriter().write(((String) shCacheObject.object).toString());
			} else {
				//System.out.println("Is not cached " + contextURL);
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
				//System.out.println("Is cached " + contextURL);
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
				//System.out.println("Is not cached " + contextURL);
				String html = this.siteContext(shSiteName, shFormat, shLocale, 5, request, response);
				shCacheObject = new ShCachedObject(html, contextURL, cacheMinutes);
				/* Place the object into the cache! */
				ShCacheManager.putCache(shCacheObject);
			}
		}
	}

	private String siteContext(String shSiteName, String shFormat, String shLocale, int contextPathPosition,
			HttpServletRequest request, HttpServletResponse response) throws IOException, ScriptException {

		InputStreamReader isr = new InputStreamReader(
				resourceloader.getResource("classpath:/js/server-side/shObject.js").getInputStream());

		StringBuilder shObjectJS = new StringBuilder();
		try (Reader reader = new BufferedReader(isr)) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				shObjectJS.append((char) c);
			}
		}

		String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String shContext = null;
		String[] contexts = url.split("/");
		ArrayList<String> contentPath = new ArrayList<String>();

		if (contextPathPosition > 1) {
			shContext = contexts[1];
		}
		for (int i = contextPathPosition; i < contexts.length; i++) {
			contentPath.add(contexts[i]);
		}

		ShSite shSite = shSiteRepository.findByFurl(shSiteName);

		int lastPosition = contentPath.size() - 1;
		String postName = null;
		String folderPath = "/";
		if (contentPath.size() >= 1) {
			postName = contentPath.get(lastPosition);

			ArrayList<String> folderPathArray = contentPath;

			// Remove PostName
			folderPathArray.remove(folderPathArray.size() - 1);

			for (String path : folderPathArray) {
				folderPath = folderPath + path + "/";
			}
		}

		ShFolder shParentFolder = shFolderUtils.folderFromPath(shSite, folderPath);
		ShFolder shFolderItem = null;
		boolean isFolder = false;

		ShPost shPostItem = null;

		// If shPostItem is not null, so is a Post, otherwise is a Folder
		if (postName != null) {
			shPostItem = shPostRepository.findByShFolderAndFurl(shParentFolder, postName);

		}

		if (shPostItem != null) {
			// Alias
			if (shPostItem.getShPostType().getName().equals(ShSystemPostType.ALIAS)) {
				List<ShPostAttr> shPostAttrs = shPostItem.getShPostAttrs();
				for (ShPostAttr shPostAttr : shPostAttrs) {
					if (shPostAttr.getShPostTypeAttr().getName().equals(ShSystemPostTypeAttr.CONTENT)) {
						shPostItem = shPostRepository.findById(UUID.fromString(shPostAttr.getStrValue())).get();
					}
				}
			}
		} else {
			String folderPathCurrent = folderPath;
			if (postName != null) {
				folderPathCurrent = folderPathCurrent + postName + "/";
			}
			shFolderItem = shFolderUtils.folderFromPath(shSite, folderPathCurrent);
			if (shFolderItem != null) {
				// System.out.println("shFolderItem is not null");
				ShPost shFolderIndex = shPostRepository.findByShFolderAndFurl(shFolderItem, "index");

				if (shFolderIndex != null) {
					shPostItem = shFolderIndex;
					isFolder = true;
				}
			} else {
				// System.out.println("shFolderItem is null");

			}
			// System.out.println(shSite.getName());
			// System.out.println(folderPathCurrent);
		}

		// Nashorn Engine
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		Bindings bindings = engine.createBindings();

		String javascript = null;
		String javascriptVar = null;
		String pageLayoutHTML = null;
		String pageLayoutJS = null;
		// Folder
		if (isFolder || shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {

			if (shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
				shFolderItem = shPostItem.getShFolder();
			}

			// Folder Index

			Map<String, ShPostAttr> shFolderIndexMap = shPostUtils.postToMap(shPostItem);

			// Page Layout
			String pageLayoutName = shFolderIndexMap.get(ShSystemPostTypeAttr.PAGE_LAYOUT).getStrValue();

			ShPost shFolderPageLayout = shPostRepository.findByTitle(pageLayoutName);

			Map<String, ShPostAttr> shFolderPageLayoutMap = shPostUtils.postToMap(shFolderPageLayout);

			pageLayoutHTML = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
			pageLayoutJS = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

			// Theme

			String themeName = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();

			ShPost shTheme = shPostRepository.findByTitle(themeName);

			Map<String, ShPostAttr> shThemeMap = shPostUtils.postToMap(shTheme);

			JSONObject shThemeAttrs = new JSONObject();
			shThemeAttrs.put("javascript", shThemeMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());
			shThemeAttrs.put("css", shThemeMap.get(ShSystemPostTypeAttr.CSS).getStrValue());

			// Folder converted to JSON

			JSONArray shPostItems = new JSONArray();
			JSONArray shChildFolderItems = new JSONArray();

			JSONObject shFolderItemAttrs = shFolderUtils.toJSON(shFolderItem);

			shFolderItemAttrs.put("theme", shThemeAttrs);

			JSONObject shSiteItemAttrs = shSiteUtils.toJSON(shSite, shContext);

			List<ShFolder> shFolders = shFolderRepository.findByParentFolder(shFolderItem);

			for (ShFolder shChildFolder : shFolders) {
				shChildFolderItems.put(shFolderUtils.toJSON(shChildFolder));
			}
			List<ShPost> shPosts = shPostRepository.findByShFolder(shFolderItem);

			for (ShPost shPost : shPosts) {
				if (!shPost.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
					JSONObject shPostItemAttrs = shPostUtils.toJSON(shPost);
					shPostItems.put(shPostItemAttrs);
				}
			}

			// Post Item converted to JSON
			JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);
			shPostItemAttrs.put("theme", shThemeAttrs);

			shFolderItemAttrs.put("posts", shPostItems);
			shFolderItemAttrs.put("folders", shChildFolderItems);
			shFolderItemAttrs.put("post", shPostItemAttrs);
			shFolderItemAttrs.put("site", shSiteItemAttrs);
			// Folder Attribs
			javascriptVar = "var shContent = " + shFolderItemAttrs.toString() + ";";
			javascript = javascriptVar + javascript;

		} else {
			// Post
			// Page Layout
			JSONObject postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
			String pageLayoutName = (String) postTypeLayout.get(shPostItem.getShPostType().getName());
			ShPost shPostPageLayout = shPostRepository.findByTitle(pageLayoutName);

			Map<String, ShPostAttr> shPostPageLayoutMap = shPostUtils.postToMap(shPostPageLayout);

			pageLayoutHTML = shPostPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
			pageLayoutJS = shPostPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

			// Theme
			String themeName = shPostPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
			ShPost shTheme = shPostRepository.findByTitle(themeName);

			Map<String, ShPostAttr> shThemeMap = shPostUtils.postToMap(shTheme);

			JSONObject shThemeAttrs = new JSONObject();
			shThemeAttrs.put("javascript", shThemeMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());
			shThemeAttrs.put("css", shThemeMap.get(ShSystemPostTypeAttr.CSS).getStrValue());

			JSONObject shSiteItemAttrs = shSiteUtils.toJSON(shSite, shContext);

			JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);

			shPostItemAttrs.put("theme", shThemeAttrs);
			shPostItemAttrs.put("site", shSiteItemAttrs);

			// PostItem Attribs
			javascriptVar = "var shContent = " + shPostItemAttrs.toString() + ";";
			javascript = javascriptVar + javascript;
		}

		// Page Layout
		javascript = javascriptVar + pageLayoutJS;
		bindings.put("html", pageLayoutHTML);
		bindings.put("spring", context);
		Object pageLayoutResult = engine.eval(javascript, bindings);
		String pageLayoutHTMLMod = (pageLayoutResult.toString());

		Document doc = Jsoup.parse(pageLayoutHTMLMod);
		Elements elements = doc.getElementsByAttribute("sh-region");

		// Regions
		for (Element element : elements) {

			String regionAttr = element.attr("sh-region");

			ShPost shRegionPost = shPostRepository.findByTitle(regionAttr);

			Map<String, ShPostAttr> shRegionPostMap = shPostUtils.postToMap(shRegionPost);

			String shRegionJS = shRegionPostMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();
			String shRegionHTML = shRegionPostMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
			javascript = javascriptVar + shRegionJS;

			bindings.put("html", shRegionHTML);

			javascript = shObjectJS.toString() + javascript;

			Object regionResult = engine.eval(javascript, bindings);
			element.html(regionResult.toString());
		}

		// End Page Layout
		response.setContentType(MediaType.TEXT_HTML_VALUE);
		response.getWriter().write(doc.html());
		return doc.html();
	}

}
