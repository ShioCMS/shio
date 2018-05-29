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

import org.json.JSONArray;
import org.json.JSONObject;
import org.jsoup.Jsoup;
import org.jsoup.nodes.Document;
import org.jsoup.nodes.Element;
import org.jsoup.select.Elements;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
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

	public ArrayList<String> contentPathFactory(int contextPathPosition, HttpServletRequest request) {
		String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
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
					shPostItem = shPostRepository.findById(shPostAttr.getStrValue()).get();
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

	public ShPost shPostItemFactory(ShSite shSite, String folderPath, String objectName) {
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

	public JSONObject shThemeFactory(String postThemeId) {
		ShPost shTheme = shPostRepository.findById(postThemeId).get();

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
		if (shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
			shFolderItem = shPostItem.getShFolder();
		}

		return shFolderItem;
	}

	public Map<String, ShPostAttr> shFolderPageLayoutMapFactory(ShPost shPostItem) {
		Map<String, ShPostAttr> shFolderIndexMap = shPostUtils.postToMap(shPostItem);

		String shPostFolderPageLayoutId = shFolderIndexMap.get(ShSystemPostTypeAttr.PAGE_LAYOUT).getStrValue();
		ShPost shFolderPageLayout = shPostRepository
				.findById(shPostFolderPageLayoutId).get();

		return shPostUtils.postToMap(shFolderPageLayout);
	}

	public String shPageLayoutFactory(String javascriptVar, String pageLayoutJS, String pageLayoutHTML)
			throws ScriptException, IOException {

		StringBuilder shObjectJS = this.shObjectJSFactory();

		String javascript = shObjectJS.toString() + javascriptVar + pageLayoutJS;

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

}
