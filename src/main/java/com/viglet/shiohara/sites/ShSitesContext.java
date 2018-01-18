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

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@Controller
public class ShSitesContext {
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;

	@RequestMapping("/sites/{shSite}/{shFormat}/{shLocale}/**")
	private void sitesFull(HttpServletRequest request, HttpServletResponse response,
			@PathVariable(value = "shSite") String shSite, @PathVariable(value = "shFormat") String shFormat,
			@PathVariable(value = "shLocale") String shLocale) throws IOException, ScriptException {

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
				shSite = contexts[i];
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

		ShPost shPostItem = shPostRepository.findByTitle(postName);

		ShPost shPostPageTemplate = shPostRepository.findById(5); // Page Template Post
	

		List<ShPostAttr> shPostPageTemplateAttrs = shPostPageTemplate.getShPostAttrs();

		Map<String, ShPostAttr> shPostPageTemplateMap = new HashMap<String, ShPostAttr>();
		for (ShPostAttr shPostPageTemplateAttr : shPostPageTemplateAttrs)
			shPostPageTemplateMap.put(shPostPageTemplateAttr.getShPostTypeAttr().getName(), shPostPageTemplateAttr);

		String javascript = shPostPageTemplateMap.get("Javascript").getStrValue();
		String html = shPostPageTemplateMap.get("HTML").getStrValue();

		
		ShPost shPostTheme = shPostRepository.findById(4); // Theme Post
		List<ShPostAttr> shPostThemeAttrs = shPostTheme.getShPostAttrs();

		Map<String, ShPostAttr> shPostThemeMap = new HashMap<String, ShPostAttr>();
		for (ShPostAttr shPostThemeAttr : shPostThemeAttrs)
			shPostThemeMap.put(shPostThemeAttr.getShPostTypeAttr().getName(), shPostThemeAttr);

		
		JSONObject shPostItemThemeAttrs = new JSONObject();
		shPostItemThemeAttrs.put("javascript", shPostThemeMap.get("Javascript").getStrValue());
		shPostItemThemeAttrs.put("css", shPostThemeMap.get("CSS").getStrValue());
		
		JSONObject shPostItemAttrs = new JSONObject();
		JSONObject shPostItemSystemAttrs = new JSONObject();
		shPostItemSystemAttrs.put("id", shPostItem.getId());
		shPostItemSystemAttrs.put("post-type-id", shPostItem.getShPostType().getId());
		shPostItemSystemAttrs.put("title", shPostItem.getTitle());
		shPostItemSystemAttrs.put("summary", shPostItem.getSummary());

		shPostItemAttrs.put("system", shPostItemSystemAttrs);
		shPostItemAttrs.put("theme", shPostItemThemeAttrs); 
		
		for (ShPostAttr shPostAttr : shPostItem.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getName() != null) {
				shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
			}
		}

		
		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = engine.createBindings();
		bindings.put("html", html);
		// PostItem Attribs
		javascript = "var post = " + shPostItemAttrs.toString() + ";" + javascript;
		Object result = engine.eval(javascript, bindings);

		response.setContentType("text/html");
		response.getWriter().write(result.toString());
	}

}
