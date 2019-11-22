/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

package com.viglet.shiohara.onstartup.provider;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.provider.ShProviderInstance;
import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.provider.ShProviderInstanceRepository;
import com.viglet.shiohara.persistence.repository.provider.ShProviderVendorRepository;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

@Component
public class ShProviderInstanceOnStartup {
	private static final String PROVIDER_PATH = "/provider/%s";
	private static final String URL = "URL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";

	@Autowired
	private ShConfigVarRepository shConfigVarRepository;
	@Autowired
	private ShProviderInstanceRepository shProviderInstanceRepository;
	@Autowired
	private ShProviderVendorRepository shProviderVendorRepository;

	public void createDefaultRows() {

		if (shProviderInstanceRepository.findAll().isEmpty()) {

			ShProviderInstance shProviderInstance = new ShProviderInstance();
			shProviderInstance.setName("Content Server");
			shProviderInstance.setDescription("Content Server");
			shProviderInstance.setVendor(shProviderVendorRepository.findById("OTCS").orElse(null));

			shProviderInstanceRepository.save(shProviderInstance);

			String providerInstance = String.format(PROVIDER_PATH, shProviderInstance.getId());

			ShConfigVar shConfigVar = new ShConfigVar();
			shConfigVar.setPath(providerInstance);
			shConfigVar.setName(URL);
			shConfigVar.setValue("http://localhost/OTCS/cs.exe");
			shConfigVarRepository.save(shConfigVar);

			shConfigVar = new ShConfigVar();
			shConfigVar.setPath(providerInstance);
			shConfigVar.setName(USERNAME);
			shConfigVar.setValue("admin");
			shConfigVarRepository.save(shConfigVar);

			shConfigVar = new ShConfigVar();
			shConfigVar.setPath(providerInstance);
			shConfigVar.setName(PASSWORD);
			shConfigVar.setValue("password");
			shConfigVarRepository.save(shConfigVar);

		}
	}

}
