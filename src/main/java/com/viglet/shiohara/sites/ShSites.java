package com.viglet.shiohara.sites;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;
import javax.servlet.ServletConfig;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.context.support.SpringBeanAutowiringSupport;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@Component
public class ShSites extends HttpServlet {
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	private static final long serialVersionUID = 1L;

	public void init(ServletConfig config) throws ServletException {
		super.init(config);
		SpringBeanAutowiringSupport.processInjectionBasedOnServletContext(this, config.getServletContext());
	}

	@Override
	protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// System.out.println("MyServlet's doGet() method is invoked.");
		try {
			doAction(req, resp);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	@Override
	protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
		// System.out.println("MyServlet's doPost() method is invoked.");
		try {
			doAction(req, resp);
		} catch (ScriptException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

	private void doAction(HttpServletRequest req, HttpServletResponse resp)
			throws ServletException, IOException, ScriptException {
		ShPost shPost = shPostRepository.findById(4); // Page Template Post
		List<ShPostAttr> shPostAttrs = shPost.getShPostAttrs();

		Map<String, ShPostAttr> shPostAttrMap = new HashMap<String, ShPostAttr>();
		for (ShPostAttr shPostAttr : shPostAttrs)
			shPostAttrMap.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr);

		String javascript = shPostAttrMap.get("Javascript").getStrValue();
		String html = shPostAttrMap.get("HTML").getStrValue();
		String name = req.getParameter("name");

		ScriptEngine engine = new ScriptEngineManager().getEngineByName("nashorn");
		Bindings bindings = engine.createBindings();
		bindings.put("name", name);
		bindings.put("html", html);

		Object result = engine.eval(javascript, bindings);

		resp.setContentType("text/html");
		resp.getWriter().write(result.toString());
	}

}