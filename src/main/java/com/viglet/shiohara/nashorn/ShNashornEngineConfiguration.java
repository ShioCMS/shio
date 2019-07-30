package com.viglet.shiohara.nashorn;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

import javax.annotation.Resource;
import javax.script.Bindings;
import javax.script.ScriptContext;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import com.viglet.shiohara.component.ShGetRelationComponent;
import com.viglet.shiohara.component.ShNavigationComponent;
import com.viglet.shiohara.component.ShQueryComponent;
import com.viglet.shiohara.component.ShSearchComponent;
import com.viglet.shiohara.component.form.ShFormComponent;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShObjectUtils;
import com.viglet.shiohara.utils.ShPostUtils;

@Configuration
public class ShNashornEngineConfiguration {
	private static final Log logger = LogFactory.getLog(ShNashornEngineConfiguration.class);
	private static final String[] NASHORN_CONFIGURATION = new String[] { "--persistent-code-cache",
			"--optimistic-types=true", "-pcc", "--class-cache-size=50000" };
	@Resource
	private ApplicationContext context;
	@Autowired
	private ShNavigationComponent shNavigationComponent;
	@Autowired
	private ShQueryComponent shQueryComponent;
	@Autowired
	private ShSearchComponent shSearchComponent;
	@Autowired
	private ShFormComponent shFormComponent;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShObjectUtils shObjectUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShGetRelationComponent shGetRelationComponent;

	@Bean
	public ScriptEngine scriptEngine() {

		Class<?> nashornScriptEngineFactory;
		try {

			nashornScriptEngineFactory = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");

			Method getScriptEngine = nashornScriptEngineFactory.getDeclaredMethod("getScriptEngine", String[].class);
			ScriptEngineFactory scriptEngineFactory = (ScriptEngineFactory) nashornScriptEngineFactory.newInstance();
			ScriptEngine engine = (ScriptEngine) getScriptEngine.invoke(scriptEngineFactory,
					new Object[] { NASHORN_CONFIGURATION });
			Bindings bindings = engine.createBindings();

			bindings.put("shNavigationComponent", shNavigationComponent);
			bindings.put("shQueryComponent", shQueryComponent);
			bindings.put("shSearchComponent", shSearchComponent);
			bindings.put("shFormComponent", shFormComponent);
			bindings.put("shFolderUtils", shFolderUtils);
			bindings.put("shObjectUtils", shObjectUtils);
			bindings.put("shPostUtils", shPostUtils);
			bindings.put("shGetRelationComponent", shGetRelationComponent);

			engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);

			return engine;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("ShNashornEngineConfiguration Error:", e);
		}
		return null;
	}

}
