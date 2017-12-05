package com.viglet.shiohara.onstartup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.onstartup.post.type.ShPostTypeOnStartup;
import com.viglet.shiohara.onstartup.site.ShSiteOnStartup;
import com.viglet.shiohara.onstartup.system.ShConfigVarOnStartup;
import com.viglet.shiohara.onstartup.system.ShLocaleOnStartup;
import com.viglet.shiohara.onstartup.widget.ShWidgetOnStartup;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

@Component
public class ShOnStartup implements ApplicationRunner {

	@Autowired
	private ShConfigVarRepository shConfigVarRepository;

	@Autowired
	private ShLocaleOnStartup shLocaleOnStartup;
	@Autowired
	private ShWidgetOnStartup shWidgetOnStartup;	
	@Autowired
	private ShPostTypeOnStartup shPostTypeOnStartup;
	@Autowired
	private ShConfigVarOnStartup shConfigVarOnStartup;
	@Autowired
	private ShSiteOnStartup shSiteOnStartup;
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		final String FIRST_TIME = "FIRST_TIME";
		
		if (shConfigVarRepository.findById(FIRST_TIME) == null) {
			
			System.out.println("First Time Configuration ...");

			shLocaleOnStartup.createDefaultRows();
			shWidgetOnStartup.createDefaultRows();
			shPostTypeOnStartup.createDefaultRows();
			shConfigVarOnStartup.createDefaultRows();
			shSiteOnStartup.createDefaultRows();
			
			System.out.println("Configuration finished.");
		}

	}

}