/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.sites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.Resource;
import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;

import jdk.nashorn.api.scripting.NashornException;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Comment;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.api.folder.ShFolderAPI;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
public class ShSitesContextComponent {
	static final Logger logger = LogManager.getLogger(ShSitesContextComponent.class.getName());
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ResourceLoader resourceloader;

	@Resource
	private ApplicationContext context;

	public StringBuilder shObjectJSFactory() throws IOException {
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

	public String folderPathFactory(ArrayList<String> contentPath) {
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

	public ArrayList<String> contentPathFactory(String url) {
		int contextPathPosition = 5;
		String[] contexts = url.split("/");
		ArrayList<String> contentPath = new ArrayList<String>();

		for (int i = contextPathPosition; i < contexts.length; i++) {
			contentPath.add(contexts[i]);
		}
		return contentPath;
	}

	public ShPost shPostAlias(ShPost shPostItem) {
		if (shPostItem.getShPostType().getName().equals(ShSystemPostType.ALIAS)) {
			Set<ShPostAttr> shPostAttrs = shPostItem.getShPostAttrs();
			for (ShPostAttr shPostAttr : shPostAttrs) {
				if (shPostAttr.getShPostTypeAttr().getName().equals(ShSystemPostTypeAttr.CONTENT)) {
					shPostItem = shPostRepository.findById(shPostAttr.getStrValue()).orElse(null);
				}
			}
		}
		return shPostItem;
	}

	public String objectNameFactory(ArrayList<String> contentPath) {
		String objectName = null;
		int lastPosition = contentPath.size() - 1;
		if (contentPath.size() >= 1) {
			objectName = contentPath.get(lastPosition);
		}
		return objectName;
	}

	public ShObject shObjectItemFactory(ShSite shSite, ShFolder shFolder, String objectName) {
		ShObject shObjectItem = null;

		// If shPostItem is not null, so is a Post, otherwise is a Folder
		if (objectName != null) {
			ShFolder shParentFolder = shFolder;
			shObjectItem = shPostRepository.findByShFolderAndFurl(shParentFolder, objectName);
			if (shObjectItem == null) {
				shObjectItem = shPostRepository.findByShFolderAndTitle(shParentFolder, objectName);
			}
		}

		if (shObjectItem != null) {
			shObjectItem = this.shPostAlias((ShPost) shObjectItem);
		} else {
			String folderPathCurrent = shFolderUtils.folderPath(shFolder, true);
			if (objectName != null) {
				folderPathCurrent = folderPathCurrent + objectName + "/";
			}
			ShFolder shFolderItem = shFolderUtils.folderFromPath(shSite, folderPathCurrent);
			if (shFolderItem != null) {
				ShPost shFolderIndex = shPostRepository.findByShFolderAndFurl(shFolderItem, "index");

				if (shFolderIndex != null) {
					shObjectItem = shFolderIndex;
				} else {
					shObjectItem = shFolderItem;
				}
			}
		}
		return shObjectItem;
	}

	public JSONObject shThemeFactory(String postThemeId) {
		ShPost shTheme = shPostRepository.findById(postThemeId).orElse(null);

		Map<String, ShPostAttr> shThemeMap = shPostUtils.postToMap(shTheme);

		JSONObject shThemeAttrs = new JSONObject();
		shThemeAttrs.put("javascript", shThemeMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue());
		shThemeAttrs.put("css", shThemeMap.get(ShSystemPostTypeAttr.CSS).getStrValue());

		return shThemeAttrs;
	}

	public JSONArray shPostItemsFactory(ShFolder shFolderItem) {
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

	public JSONArray shChildFolderItemsFactory(ShFolder shFolderItem) {
		JSONArray shChildFolderItems = new JSONArray();
		List<ShFolder> shFolders = shFolderRepository.findByParentFolder(shFolderItem);

		for (ShFolder shChildFolder : shFolders) {
			shChildFolderItems.put(shFolderUtils.toJSON(shChildFolder));
		}
		return shChildFolderItems;
	}

	public ShFolder shFolderItemFactory(ShPost shPostItem) {
		ShFolder shFolderItem = null;
		if (shPostItem != null && shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
			shFolderItem = shPostItem.getShFolder();
		}

		return shFolderItem;
	}

	public Map<String, ShPostAttr> shFolderPageLayoutMapFactory(ShObject shObjectItem, ShSite shSite) {
		String shPostFolderPageLayoutId = null;
		ShPost shFolderPageLayout = null;

		if (shObjectItem instanceof ShPost) {
			Map<String, ShPostAttr> shFolderIndexMap = shPostUtils.postToMap((ShPost) shObjectItem);
			shPostFolderPageLayoutId = shFolderIndexMap.get(ShSystemPostTypeAttr.PAGE_LAYOUT).getStrValue();

			if (shPostFolderPageLayoutId != null) {
				shFolderPageLayout = shPostRepository.findById(shPostFolderPageLayoutId).orElse(null);
			}
		} else if (shObjectItem instanceof ShFolder) {
			// If Folder doesn't have PageLayout, it will try use default Folder Page Layout
			if (shSite.getPostTypeLayout() != null) {
				JSONObject postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
				String pageLayoutName = (String) postTypeLayout.get("FOLDER");
				List<ShPost> shPostPageLayouts = shPostRepository.findByTitle(pageLayoutName);

				if (shPostPageLayouts != null) {
					for (ShPost shPostPageLayout : shPostPageLayouts) {
						if (shPostUtils.getSite(shPostPageLayout).getId().equals(shSite.getId())) {
							shFolderPageLayout = shPostPageLayout;
						}
					}
				}
			}
		}

		return shPostUtils.postToMap(shFolderPageLayout);
	}

	public String shPageLayoutFactory(String javascriptVar, String pageLayoutJS, String pageLayoutHTML,
			HttpServletRequest request, ShSite shSite) throws ScriptException, IOException {

		StringBuilder shObjectJS = this.shObjectJSFactory();

		String javascript = shObjectJS.toString() + javascriptVar + pageLayoutJS;

		// Nashorn Engine
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = engine.createBindings();
		bindings.put("html", pageLayoutHTML);
		bindings.put("spring", context);
		bindings.put("request", request);
		Object pageLayoutResult = engine.eval(javascript, bindings);

		return this.shRegionFactory(engine, bindings, javascriptVar, pageLayoutResult.toString(), shSite).html();
	}

	private Document shRegionFactory(ScriptEngine engine, Bindings bindings, String javascriptVar, String regionResult,
			ShSite shSite) throws IOException, ScriptException {
		StringBuilder shObjectJS = this.shObjectJSFactory();

		Document doc = Jsoup.parse(regionResult);

		Elements elements = doc.getElementsByAttribute("sh-region");

		for (Element element : elements) {
			// element.addClass("sh-region");

			String regionAttr = element.attr("sh-region");

			List<ShPost> shRegionPosts = shPostRepository.findByTitle(regionAttr);

			Map<String, ShPostAttr> shRegionPostMap = null;
			String elementId = null;
			if (shRegionPosts != null) {

				for (ShPost shRegionPost : shRegionPosts) {
					elementId = shRegionPost.getId();
					// element.attr("id", shRegionPost.getId());
					if (shPostUtils.getSite(shRegionPost).getId().equals(shSite.getId())) {
						shRegionPostMap = shPostUtils.postToMap(shRegionPost);

					}
				}

			}

			if (shRegionPostMap != null) {
				String shRegionJS = shRegionPostMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

				String shRegionHTML = shRegionPostMap.get(ShSystemPostTypeAttr.HTML).getStrValue();

				bindings.put("html", shRegionHTML);

				String javascript = shObjectJS.toString() + javascriptVar + shRegionJS;

				try {
					Object regionResultChild = engine.eval(javascript, bindings);

					Comment comment = new Comment(String.format(" sh-region: %s, id: %s ", regionAttr, elementId));
					element.html(comment.toString() + this
							.shRegionFactory(engine, bindings, javascriptVar, regionResultChild.toString(), shSite)
							.html()).unwrap();
				} catch (ScriptException e) {
					if (e.getCause() instanceof NashornException) {
						//logger.info(NashornException.getScriptStackString(e));
						logger.info(e.getCause());
					}
				}
			}
		}

		return doc;
	}
}
