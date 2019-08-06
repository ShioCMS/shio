package com.viglet.shiohara.sites.nashorn;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;

import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptException;
import javax.script.SimpleScriptContext;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.sites.cache.component.ShCacheJavascript;

@Component
public class ShNashornEngineProcess {
	private static final Log logger = LogFactory.getLog(ShNashornEngineProcess.class);
	private static final ThreadLocal<HashMap<String, ScriptContext>> scriptContexts = new ThreadLocal<>();
	@Autowired
	private ScriptEngine scriptEngine;
	@Autowired
	private ShCacheJavascript shCacheJavascript;

	public Object render(String javascript, String html, HttpServletRequest request, Map<String, Object> shContent) {
		try {
			ScriptContext sc = new SimpleScriptContext();
			SimpleScriptContext ssc = new SimpleScriptContext();
			ssc.setBindings(scriptEngine.createBindings(), ScriptContext.ENGINE_SCOPE);
			Bindings b = scriptEngine.getBindings(ScriptContext.GLOBAL_SCOPE);
			if (b != null) {
				for (Map.Entry e : b.entrySet()) {
					ssc.setAttribute((String) e.getKey(), e.getValue(), ScriptContext.ENGINE_SCOPE);
				}
			}
			sc = shObjectLib(ssc);
			sc.setAttribute("shContent", shContent, ScriptContext.ENGINE_SCOPE);
			sc.setAttribute("html", html, ScriptContext.ENGINE_SCOPE);
			sc.setAttribute("request", request, ScriptContext.ENGINE_SCOPE);

			return scriptEngine.eval(javascript, sc);

		} catch (IOException | ScriptException e) {
			logger.error("ShNashornEngineProcess Error: ", e);
		}
		return null;
	}

	private ScriptContext shObjectLib(SimpleScriptContext ssc) throws IOException, ScriptException {
		StringBuilder shObjectJS;
		ScriptContext sc;
		if (scriptContexts.get() == null) {
			logger.debug("Creating TL");
			scriptContexts.set(new HashMap<String, ScriptContext>());
		}
		if ((sc = scriptContexts.get().get("shScript")) != null) {
			logger.debug("Reusing shScript");
		} else {
			logger.debug("Creating shScript");

			shObjectJS = shCacheJavascript.shObjectJSFactory();
			scriptEngine.eval(shObjectJS.toString(), ssc);
			HashMap<String, ScriptContext> elementMap = new HashMap<>();
			sc = (ScriptContext) ssc;
			elementMap.put("shScript", sc);
			scriptContexts.set(elementMap);

		}
		return sc;
	}
}
