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
		System.out.println(shContext + " " + shSite + " " + shFormat + " " + shLocale + " " + contentPath.toString());

		String postName = contentPath.get(contentPath.size() - 1).replaceAll("-", " ");
		
		ShPost shPostItem = shPostRepository.findByTitle(postName);

		ShPost shPost = shPostRepository.findById(4); // Page Template Post
		List<ShPostAttr> shPostAttrs = shPost.getShPostAttrs();

		Map<String, ShPostAttr> shPostAttrMap = new HashMap<String, ShPostAttr>();
		for (ShPostAttr shPostAttr : shPostAttrs)
			shPostAttrMap.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr);

		String javascript = shPostAttrMap.get("Javascript").getStrValue();
		String html = shPostAttrMap.get("HTML").getStrValue();

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = engine.createBindings();
		bindings.put("name", postName);
		bindings.put("html", html);
		bindings.put("post", shPostItem);

		Object result = engine.eval(javascript, bindings);

		response.setContentType("text/html");
		response.getWriter().write(result.toString());
	}

}
