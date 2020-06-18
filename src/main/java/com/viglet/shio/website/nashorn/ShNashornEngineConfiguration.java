/*
 * Copyright (C) 2016-2020 the original author or authors. 
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
package com.viglet.shio.website.nashorn;

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

import com.viglet.shio.website.component.ShGetRelationComponent;
import com.viglet.shio.website.component.ShNavigationComponent;
import com.viglet.shio.website.component.ShQueryComponent;
import com.viglet.shio.website.component.ShSearchComponent;
import com.viglet.shio.website.component.form.ShFormComponent;
import com.viglet.shio.website.utils.ShSitesFolderUtils;
import com.viglet.shio.website.utils.ShSitesObjectUtils;
import com.viglet.shio.website.utils.ShSitesPostUtils;

/**
 * @author Alexandre Oliveira
 */
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
	private ShSitesFolderUtils shSitesFolderUtils;
	@Autowired
	private ShSitesObjectUtils shSitesObjectUtils;
	@Autowired
	private ShSitesPostUtils shSitesPostUtils;
	@Autowired
	private ShGetRelationComponent shGetRelationComponent;

	@Bean
	public ScriptEngine scriptEngine() {

		Class<?> nashornScriptEngineFactory;
		try {

			nashornScriptEngineFactory = Class.forName("jdk.nashorn.api.scripting.NashornScriptEngineFactory");

			Method getScriptEngine = nashornScriptEngineFactory.getDeclaredMethod("getScriptEngine", String[].class);
			ScriptEngineFactory scriptEngineFactory = (ScriptEngineFactory) nashornScriptEngineFactory.getDeclaredConstructor().newInstance();
			ScriptEngine engine = (ScriptEngine) getScriptEngine.invoke(scriptEngineFactory, new Object[] {NASHORN_CONFIGURATION});
			Bindings bindings = engine.createBindings();

			bindings.put("shNavigationComponent", shNavigationComponent);
			bindings.put("shQueryComponent", shQueryComponent);
			bindings.put("shSearchComponent", shSearchComponent);
			bindings.put("shFormComponent", shFormComponent);
			bindings.put("shGetRelationComponent", shGetRelationComponent);
			bindings.put("shSitesFolderUtils", shSitesFolderUtils);
			bindings.put("shSitesObjectUtils", shSitesObjectUtils);
			bindings.put("shSitesPostUtils", shSitesPostUtils);

			engine.setBindings(bindings, ScriptContext.GLOBAL_SCOPE);

			return engine;
		} catch (ClassNotFoundException | NoSuchMethodException | SecurityException | InstantiationException
				| IllegalAccessException | IllegalArgumentException | InvocationTargetException e) {
			logger.error("ShNashornEngineConfiguration Error:", e);
		}
		return null;
	}

}
