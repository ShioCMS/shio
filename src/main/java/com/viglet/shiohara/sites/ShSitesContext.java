package com.viglet.shiohara.sites;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShChannelUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;

@Controller
public class ShSitesContext {
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShSiteRepository shSiteRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShChannelUtils shChannelUtils;
	@Autowired
	ShPostUtils shPostUtils;
	@Autowired
	ShSiteUtils shSiteUtils;
	@Autowired
	ResourceLoader resourceloader;

	@Resource
	private ApplicationContext context;
	@RequestMapping("/sites/{shSiteName}/{shFormat}/{shLocale}/**")
	private void sitesFull(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "shSiteName") String shSiteName, @PathVariable(value = "shFormat") String shFormat,
			@PathVariable(value = "shLocale") String shLocale) throws IOException, ScriptException {

		InputStreamReader isr = new InputStreamReader(resourceloader.getResource("classpath:/js/shObject.js").getInputStream());

		StringBuilder shObjectJS = new StringBuilder();
		try (Reader reader = new BufferedReader(isr)) {
			int c = 0;
			while ((c = reader.read()) != -1) {
				shObjectJS.append((char) c);
			}
		}

	//	ShPost shPostPageLayout = shPostRepository.findByTitle("Post Page Layout"); // Page Layout Post

		String url = (String) request.getAttribute(HandlerMapping.PATH_WITHIN_HANDLER_MAPPING_ATTRIBUTE);
		String shContext = null;
		String[] contexts = url.split("/");
		ArrayList<String> contentPath = new ArrayList<String>();

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
			default:
				contentPath.add(contexts[i]);
				break;
			}
		}

		ShSite shSite = shSiteRepository.findByName(shSiteName.replaceAll("-", " "));

		// System.out.println(shContext + " " + shSite + " " + shFormat + " " + shLocale
		// + " " + contentPath.toString());

		int lastPosition = contentPath.size() - 1;
		String postName = contentPath.get(lastPosition).replaceAll("-", " ");

		ArrayList<String> channelPathArray = contentPath;

		channelPathArray.remove(channelPathArray.size() - 1);
		String channelPath = "/";
		for (String path : channelPathArray) {
			channelPath = channelPath + path + "/";
		}

		ShChannel shChannel = shChannelUtils.channelFromPath(shSite, channelPath);
		ShChannel shChannelItem = null;
		boolean isChannel = false;

		ShPost shPostItem = shPostRepository.findByShChannelAndTitle(shChannel, postName);
		if (shPostItem == null) {
			String channelPathCurrent = channelPath + postName.replaceAll(" ", "-") + "/";

			shChannelItem = shChannelUtils.channelFromPath(shSite, channelPathCurrent);
			if (shChannelItem != null) {
				// System.out.println("shChannelItem is not null");
				ShPost shChannelIndex = shPostRepository.findByShChannelAndTitle(shChannelItem, "index");

				if (shChannelIndex != null) {
					shPostItem = shChannelIndex;
					isChannel = true;
				}
			} else {
				// System.out.println("shChannelItem is null");

			}
			// System.out.println(shSite.getName());
			// System.out.println(channelPathCurrent);
		}

		// Nashorn Engine
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");

		Bindings bindings = engine.createBindings();

		String javascript = null;
		String javascriptVar = null;
		String pageLayoutHTML = null;
		String pageLayoutJS = null;
		// Channel
		if (isChannel || shPostItem.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {

			if (shPostItem.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
				shChannelItem = shPostItem.getShChannel();
			}

			// Channel Index

			Map<String, ShPostAttr> shChannelIndexMap = shPostUtils.postToMap(shPostItem);

			// Page Layout
			String pageLayoutName = shChannelIndexMap.get("PAGE-LAYOUT").getStrValue();

			ShPost shChannelPageLayout = shPostRepository.findByTitle(pageLayoutName);

			Map<String, ShPostAttr> shChannelPageLayoutMap = shPostUtils.postToMap(shChannelPageLayout);

			pageLayoutHTML = shChannelPageLayoutMap.get("HTML").getStrValue();
			pageLayoutJS = shChannelPageLayoutMap.get("JAVASCRIPT").getStrValue();

			// Theme

			String themeName = shChannelPageLayoutMap.get("THEME").getStrValue();

			ShPost shTheme = shPostRepository.findByTitle(themeName);

			Map<String, ShPostAttr> shThemeMap = shPostUtils.postToMap(shTheme);

			JSONObject shThemeAttrs = new JSONObject();
			shThemeAttrs.put("javascript", shThemeMap.get("JAVASCRIPT").getStrValue());
			shThemeAttrs.put("css", shThemeMap.get("CSS").getStrValue());

			// Channel converted to JSON
		
			JSONArray shPostItems = new JSONArray();
			JSONArray shChildChannelItems = new JSONArray();

			JSONObject shChannelItemAttrs = shChannelUtils.toJSON(shChannelItem);

			shChannelItemAttrs.put("theme", shThemeAttrs);

			JSONObject shSiteItemAttrs = shSiteUtils.toJSON(shSite, shContext);

			List<ShChannel> shChannels = shChannelRepository.findByParentChannel(shChannelItem);

			for (ShChannel shChildChannel : shChannels) {
				shChildChannelItems.put(shChannelUtils.toJSON(shChildChannel));
			}
			List<ShPost> shPosts = shPostRepository.findByShChannel(shChannelItem);

			for (ShPost shPost : shPosts) {
				if (!shPost.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
					JSONObject shPostItemAttrs = shPostUtils.toJSON(shPost);
					shPostItems.put(shPostItemAttrs);
				}
			}

			// Post Item converted to JSON
			JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);
			shPostItemAttrs.put("theme", shThemeAttrs);

			shChannelItemAttrs.put("posts", shPostItems);
			shChannelItemAttrs.put("channels", shChildChannelItems);
			shChannelItemAttrs.put("post", shPostItemAttrs);
			shChannelItemAttrs.put("site", shSiteItemAttrs);
			// Channel Attribs
			javascriptVar = "var shContent = " + shChannelItemAttrs.toString() + ";";
			javascript = javascriptVar + javascript;

		} else {
			// Post
			// Page Layout			
			JSONObject postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
			String pageLayoutName = (String) postTypeLayout.get(shPostItem.getShPostType().getName());
			ShPost shPostPageLayout = shPostRepository.findByTitle(pageLayoutName);
			
			Map<String, ShPostAttr> shPostPageLayoutMap = shPostUtils.postToMap(shPostPageLayout);

			pageLayoutHTML = shPostPageLayoutMap.get("HTML").getStrValue();
			pageLayoutJS = shPostPageLayoutMap.get("JAVASCRIPT").getStrValue();

			// Theme
			String themeName = shPostPageLayoutMap.get("THEME").getStrValue();
			ShPost shTheme = shPostRepository.findByTitle(themeName);

			Map<String, ShPostAttr> shThemeMap = shPostUtils.postToMap(shTheme);

			JSONObject shThemeAttrs = new JSONObject();
			shThemeAttrs.put("javascript", shThemeMap.get("JAVASCRIPT").getStrValue());
			shThemeAttrs.put("css", shThemeMap.get("CSS").getStrValue());
	
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

			String shRegionJS = shRegionPostMap.get("JAVASCRIPT").getStrValue();
			String shRegionHTML = shRegionPostMap.get("HTML").getStrValue();
			javascript = javascriptVar + shRegionJS;

			bindings.put("html", shRegionHTML);

			javascript = shObjectJS.toString() + javascript;

			Object regionResult = engine.eval(javascript, bindings);
			element.html(regionResult.toString());
		}

		// End Page Layout
		response.setContentType("text/html");
		response.getWriter().write(doc.html());
	}

	
}
