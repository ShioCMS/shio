package com.viglet.shiohara.sites;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.Reader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

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
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.HandlerMapping;

import com.viglet.shiohara.channel.ShChannelUtils;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

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

	@RequestMapping("/sites/{shSiteName}/{shFormat}/{shLocale}/**")
	private void sitesFull(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "shSiteName") String shSiteName, @PathVariable(value = "shFormat") String shFormat,
			@PathVariable(value = "shLocale") String shLocale) throws IOException, ScriptException {

		
		InputStreamReader isr = new InputStreamReader(ShSitesContext.class.getClass().getResourceAsStream("/js/shObject.js"));
		
		
		
		StringBuilder shObjectJS = new StringBuilder();
	    try (Reader reader = new BufferedReader(isr)) {
	        int c = 0;
	        while ((c = reader.read()) != -1) {
	        	shObjectJS.append((char) c);
	        }
	    }
			    
		ShPost shTheme = shPostRepository.findByTitle("Sample Theme"); // Theme
		ShPost shPostPageLayout = shPostRepository.findByTitle("Post Page Layout"); // Page Layout Post
		ShPost shChannelPageLayout = shPostRepository.findByTitle("Channel Page Layout"); // Page Layout Channel

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
			 //System.out.println(shSite.getName());
			 //System.out.println(channelPathCurrent);
		}

		// Nashorn Engine
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = engine.createBindings();

		// Theme
		List<ShPostAttr> shThemeAttrList = shTheme.getShPostAttrs();

		Map<String, ShPostAttr> shThemeMap = new HashMap<String, ShPostAttr>();
		for (ShPostAttr shPostThemeAttr : shThemeAttrList)
			shThemeMap.put(shPostThemeAttr.getShPostTypeAttr().getName(), shPostThemeAttr);

		JSONObject shThemeAttrs = new JSONObject();
		shThemeAttrs.put("javascript", shThemeMap.get("Javascript").getStrValue());
		shThemeAttrs.put("css", shThemeMap.get("CSS").getStrValue());

		String javascript = null;
		String javascriptVar = null;
		String pageLayoutHTML = null;
		String pageLayoutJS = null;
		// Channel
		if (isChannel || shPostItem.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
			if (shPostItem.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
				shChannelItem = shPostItem.getShChannel();
			}
			// Page Layout
			List<ShPostAttr> shChannelPageLayoutAttrList = shChannelPageLayout.getShPostAttrs();

			Map<String, ShPostAttr> shChanneltPageLayoutMap = new HashMap<String, ShPostAttr>();
			for (ShPostAttr shChannelPageLayoutAttr : shChannelPageLayoutAttrList)
				shChanneltPageLayoutMap.put(shChannelPageLayoutAttr.getShPostTypeAttr().getName(),
						shChannelPageLayoutAttr);

			pageLayoutHTML = shChanneltPageLayoutMap.get("HTML").getStrValue();
			pageLayoutJS = shChanneltPageLayoutMap.get("Javascript").getStrValue();

			// Channel converted to JSON
			JSONObject shChannelItemSystemAttrs = new JSONObject();
			shChannelItemSystemAttrs.put("id", shChannelItem.getId());
			shChannelItemSystemAttrs.put("title", shChannelItem.getName());
			shChannelItemSystemAttrs.put("summary", shChannelItem.getSummary());
			shChannelItemSystemAttrs.put("link", shChannelUtils.channelPath(shChannelItem));

			JSONArray shPostItems = new JSONArray();
			JSONArray shChildChannelItems = new JSONArray();

			JSONObject shChannelItemAttrs = new JSONObject();

			shChannelItemAttrs.put("system", shChannelItemSystemAttrs);
			shChannelItemAttrs.put("theme", shThemeAttrs);

			JSONObject shSiteItemSystemAttrs = new JSONObject();
			shSiteItemSystemAttrs.put("id", shSite.getId());
			shSiteItemSystemAttrs.put("title", shSite.getName());
			shSiteItemSystemAttrs.put("summary", shSite.getDescription());
			shSiteItemSystemAttrs.put("link",
					"/" + shContext + "/" + shSite.getName().replaceAll(" ", "-") + "/default/pt-br");

			JSONObject shSiteItemAttrs = new JSONObject();

			shSiteItemAttrs.put("system", shSiteItemSystemAttrs);

			List<ShChannel> shChannels = shChannelRepository.findByParentChannel(shChannelItem);

			for (ShChannel shChildChannel : shChannels) {
				JSONObject shChildChannelAttrs = new JSONObject();
				JSONObject shChildChannelSystemAttrs = new JSONObject();
				shChildChannelSystemAttrs.put("id", shChildChannel.getId());
				shChildChannelSystemAttrs.put("title", shChildChannel.getName());
				shChildChannelSystemAttrs.put("summary", shChildChannel.getSummary());
				shChildChannelSystemAttrs.put("link", shChannelUtils.channelPath(shChildChannel));
				shChildChannelAttrs.put("system", shChildChannelSystemAttrs);
				shChildChannelItems.put(shChildChannelAttrs);
			}
			List<ShPost> shPosts = shPostRepository.findByShChannel(shChannelItem);

			for (ShPost shPost : shPosts) {
				if (!shPost.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
					JSONObject shPostItemAttrs = new JSONObject();

					JSONObject shPostItemSystemAttrs = new JSONObject();
					shPostItemSystemAttrs.put("id", shPost.getId());
					shPostItemSystemAttrs.put("post-type-id", shPost.getShPostType().getId());
					shPostItemSystemAttrs.put("title", shPost.getTitle());
					shPostItemSystemAttrs.put("summary", shPost.getSummary());
					shPostItemSystemAttrs.put("link",
							shChannelUtils.channelPath(shPost.getShChannel()) + shPost.getTitle().replaceAll(" ", "-"));
					for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
						if (shPostAttr.getShPostTypeAttr().getName() != null) {
							shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
						}
					}
					shPostItemAttrs.put("system", shPostItemSystemAttrs);
					shPostItems.put(shPostItemAttrs);
				}
			}

			// Post Item converted to JSON
			JSONObject shPostItemSystemAttrs = new JSONObject();
			shPostItemSystemAttrs.put("id", shPostItem.getId());
			shPostItemSystemAttrs.put("post-type-id", shPostItem.getShPostType().getId());
			shPostItemSystemAttrs.put("title", shPostItem.getTitle());
			shPostItemSystemAttrs.put("summary", shPostItem.getSummary());
			shPostItemSystemAttrs.put("link",
					shChannelUtils.channelPath(shPostItem.getShChannel()) + shPostItem.getTitle().replaceAll(" ", "-"));

			JSONObject shPostItemAttrs = new JSONObject();

			shPostItemAttrs.put("system", shPostItemSystemAttrs);
			shPostItemAttrs.put("theme", shThemeAttrs);

			for (ShPostAttr shPostAttr : shPostItem.getShPostAttrs()) {
				if (shPostAttr.getShPostTypeAttr().getName() != null) {
					shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
				}
			}

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
			List<ShPostAttr> shPostPageLayoutAttrList = shPostPageLayout.getShPostAttrs();

			Map<String, ShPostAttr> shPostPageLayoutMap = new HashMap<String, ShPostAttr>();
			for (ShPostAttr shPostPageLayoutAttr : shPostPageLayoutAttrList)
				shPostPageLayoutMap.put(shPostPageLayoutAttr.getShPostTypeAttr().getName(), shPostPageLayoutAttr);

			pageLayoutHTML = shPostPageLayoutMap.get("HTML").getStrValue();
			pageLayoutJS = shPostPageLayoutMap.get("Javascript").getStrValue();

			// Post Item converted to JSON
			JSONObject shPostItemSystemAttrs = new JSONObject();
			shPostItemSystemAttrs.put("id", shPostItem.getId());
			shPostItemSystemAttrs.put("post-type-id", shPostItem.getShPostType().getId());
			shPostItemSystemAttrs.put("title", shPostItem.getTitle());
			shPostItemSystemAttrs.put("summary", shPostItem.getSummary());
			shPostItemSystemAttrs.put("link",
					shChannelUtils.channelPath(shPostItem.getShChannel()) + shPostItem.getTitle().replaceAll(" ", "-"));

			JSONObject shSiteItemSystemAttrs = new JSONObject();
			shSiteItemSystemAttrs.put("id", shSite.getId());
			shSiteItemSystemAttrs.put("title", shSite.getName());
			shSiteItemSystemAttrs.put("summary", shSite.getDescription());
			shSiteItemSystemAttrs.put("link",
					"/" + shContext + "/" + shSite.getName().replaceAll(" ", "-") + "/default/pt-br");

			JSONObject shSiteItemAttrs = new JSONObject();

			shSiteItemAttrs.put("system", shSiteItemSystemAttrs);

			JSONObject shPostItemAttrs = new JSONObject();

			shPostItemAttrs.put("system", shPostItemSystemAttrs);
			shPostItemAttrs.put("theme", shThemeAttrs);
			shPostItemAttrs.put("site", shSiteItemAttrs);

			for (ShPostAttr shPostAttr : shPostItem.getShPostAttrs()) {
				if (shPostAttr.getShPostTypeAttr().getName() != null) {
					shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
				}
			}
			// PostItem Attribs
			javascriptVar = "var shContent = " + shPostItemAttrs.toString() + ";";
			javascript = javascriptVar + javascript;
		}

		// Page Layout

		javascript = javascriptVar + pageLayoutJS;
		bindings.put("html", pageLayoutHTML);
		Object pageLayoutResult = engine.eval(javascript, bindings);
		String pageLayoutHTMLMod = (pageLayoutResult.toString());

		Document doc = Jsoup.parse(pageLayoutHTMLMod);
		Elements elements = doc.getElementsByAttribute("sh-region");

		for (Element element : elements) {

			String regionAttr = element.attr("sh-region");
			//System.out.println("regionAttr: " + regionAttr);
			ShPost shRegionPageTemplate = shPostRepository.findByTitle(regionAttr);

			List<ShPostAttr> shRegionPageTemplateAttrs = shRegionPageTemplate.getShPostAttrs();

			Map<String, ShPostAttr> shRegionPageTemplateMap = new HashMap<String, ShPostAttr>();
			for (ShPostAttr shRegionPageTemplateAttr : shRegionPageTemplateAttrs)
				shRegionPageTemplateMap.put(shRegionPageTemplateAttr.getShPostTypeAttr().getName(),
						shRegionPageTemplateAttr);

			String shRegionJS = shRegionPageTemplateMap.get("Javascript").getStrValue();
			String shRegionHTML = shRegionPageTemplateMap.get("HTML").getStrValue();
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
