/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.onstartup;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.onstartup.ecommerce.ShEcomPaymentTypeDefinitionOnStartup;
import com.viglet.shiohara.onstartup.post.type.ShPostTypeOnStartup;
import com.viglet.shiohara.onstartup.site.ShSiteOnStartup;
import com.viglet.shiohara.onstartup.system.ShConfigVarOnStartup;
import com.viglet.shiohara.onstartup.system.ShLocaleOnStartup;
import com.viglet.shiohara.onstartup.user.ShGroupOnStartup;
import com.viglet.shiohara.onstartup.user.ShUserOnStartup;
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
	@Autowired
	private ShGroupOnStartup shGroupOnStartup;
	@Autowired
	private ShUserOnStartup shUserOnStartup;
	@Autowired
	private ShEcomPaymentTypeDefinitionOnStartup shEcomPaymentTypeDefinitionOnStartup;
	
	@Override
	public void run(ApplicationArguments arg0) throws Exception {
		final String FIRST_TIME = "FIRST_TIME";
	
		if (!shConfigVarRepository.findById(FIRST_TIME).isPresent()) {
			
			System.out.println("First Time Configuration ...");

			shLocaleOnStartup.createDefaultRows();			
			shWidgetOnStartup.createDefaultRows();
			shPostTypeOnStartup.createDefaultRows();
			shGroupOnStartup.createDefaultRows();
			shUserOnStartup.createDefaultRows();			
			shConfigVarOnStartup.createDefaultRows();
			shSiteOnStartup.createDefaultRows();
			shEcomPaymentTypeDefinitionOnStartup.createDefaultRows();
			
			System.out.println("Configuration finished.");
		}

	}

}
