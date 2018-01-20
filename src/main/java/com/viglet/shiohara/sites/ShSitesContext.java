package com.viglet.shiohara.sites;

import java.io.IOException;
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

		ShSite shSite = shSiteRepository.findById(1);
		ShPost shTheme = shPostRepository.findById(5); // Theme
		ShPost shPostPageTemplate = shPostRepository.findById(6); // Page Template Post
		ShPost shChannelPageTemplate = shPostRepository.findById(7); // PageTemplate Channel

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
		// System.out.println(shContext + " " + shSite + " " + shFormat + " " + shLocale
		// + " " + contentPath.toString());

		String postName = contentPath.get(contentPath.size() - 1).replaceAll("-", " ");

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
			shChannelItem = shChannelRepository.findByShSiteAndParentChannelAndName(shSite, shChannel, postName);
			if (shChannelItem != null) {
				ShPost shChannelIndex = shPostRepository.findByShChannelAndTitle(shChannelItem, "index");
				if (shChannelIndex != null) {

					shPostItem = shChannelIndex;
					isChannel = true;
				}
			}
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
		String html = null;

		// Channel
		if (isChannel || shPostItem.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
			if (shPostItem.getShPostType().getName().equals("PT-CHANNEL-INDEX")) {
				shChannelItem = shPostItem.getShChannel();
			}
			List<ShPostAttr> shChannelPageTemplateAttrs = shChannelPageTemplate.getShPostAttrs();

			Map<String, ShPostAttr> shChannelPageTemplateMap = new HashMap<String, ShPostAttr>();
			for (ShPostAttr shChannelPageTemplateAttr : shChannelPageTemplateAttrs)
				shChannelPageTemplateMap.put(shChannelPageTemplateAttr.getShPostTypeAttr().getName(),
						shChannelPageTemplateAttr);

			javascript = shChannelPageTemplateMap.get("Javascript").getStrValue();
			html = shChannelPageTemplateMap.get("HTML").getStrValue();

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
					shPostItemSystemAttrs.put("link", shChannelUtils.channelPath(shPost.getShChannel()) + shPost.getTitle().replaceAll(" ", "-"));
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
			shPostItemSystemAttrs.put("link", shChannelUtils.channelPath(shPostItem.getShChannel()) + shPostItem.getTitle().replaceAll(" ", "-"));
			
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
			// Channel Attribs
			javascript = "var channel = " + shChannelItemAttrs.toString() + ";" + javascript;


		} else {
			// Post
			List<ShPostAttr> shPostPageTemplateAttrs = shPostPageTemplate.getShPostAttrs();

			Map<String, ShPostAttr> shPostPageTemplateMap = new HashMap<String, ShPostAttr>();
			for (ShPostAttr shPostPageTemplateAttr : shPostPageTemplateAttrs)
				shPostPageTemplateMap.put(shPostPageTemplateAttr.getShPostTypeAttr().getName(), shPostPageTemplateAttr);

			javascript = shPostPageTemplateMap.get("Javascript").getStrValue();
			html = shPostPageTemplateMap.get("HTML").getStrValue();

			// Post Item converted to JSON
			JSONObject shPostItemSystemAttrs = new JSONObject();
			shPostItemSystemAttrs.put("id", shPostItem.getId());
			shPostItemSystemAttrs.put("post-type-id", shPostItem.getShPostType().getId());
			shPostItemSystemAttrs.put("title", shPostItem.getTitle());
			shPostItemSystemAttrs.put("summary", shPostItem.getSummary());
			shPostItemSystemAttrs.put("link", shChannelUtils.channelPath(shPostItem.getShChannel()) + shPostItem.getTitle().replaceAll(" ", "-"));
			

			JSONObject shPostItemAttrs = new JSONObject();

			shPostItemAttrs.put("system", shPostItemSystemAttrs);
			shPostItemAttrs.put("theme", shThemeAttrs);

			for (ShPostAttr shPostAttr : shPostItem.getShPostAttrs()) {
				if (shPostAttr.getShPostTypeAttr().getName() != null) {
					shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
				}
			}
			// PostItem Attribs
			javascript = "var post = " + shPostItemAttrs.toString() + ";" + javascript;
		}
		bindings.put("html", html);
		Object result = engine.eval(javascript, bindings);
		response.setContentType("text/html");
		response.getWriter().write(result.toString());
	}

}
