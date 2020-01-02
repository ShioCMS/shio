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
package com.viglet.shiohara.onstartup.provider.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.provider.exchange.ShExchangeProviderInstance;
import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.provider.exchange.ShExchangeProviderInstanceRepository;
import com.viglet.shiohara.persistence.repository.provider.exchange.ShExchangeProviderVendorRepository;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;
import com.viglet.shiohara.provider.exchange.ShExchangeSystemProviderVendor;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShExchangeProviderInstanceOnStartup {
	private static final String PROVIDER_PATH = "/provider/%s";
	private static final String URL = "URL";
	private static final String USERNAME = "USERNAME";
	private static final String PASSWORD = "PASSWORD";

	@Autowired
	private ShConfigVarRepository shConfigVarRepository;
	@Autowired
	private ShExchangeProviderInstanceRepository shExchangeProviderInstanceRepository;
	@Autowired
	private ShExchangeProviderVendorRepository shExchangeProviderVendorRepository;

	public void createDefaultRows() {

		if (shExchangeProviderInstanceRepository.findAll().isEmpty()) {

			this.createOTCSInstance();
			this.createOTMMInstance();
		}
	}

	private void createOTCSInstance() {
		ShExchangeProviderInstance shExchangeProviderInstance = new ShExchangeProviderInstance();
		shExchangeProviderInstance.setName("OpenText Content Services");
		shExchangeProviderInstance.setDescription("Sample of OTCS");
		shExchangeProviderInstance.setVendor(shExchangeProviderVendorRepository.findById(ShExchangeSystemProviderVendor.OTCS).orElse(null));
		shExchangeProviderInstance.setEnabled(false);
		
		shExchangeProviderInstanceRepository.save(shExchangeProviderInstance);

		String providerInstance = String.format(PROVIDER_PATH, shExchangeProviderInstance.getId());

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

	private void createOTMMInstance() {
		ShExchangeProviderInstance shExchangeProviderInstance = new ShExchangeProviderInstance();
		shExchangeProviderInstance.setName("OpenText Media Management");
		shExchangeProviderInstance.setDescription("Sample of OTMM");
		shExchangeProviderInstance.setVendor(shExchangeProviderVendorRepository.findById(ShExchangeSystemProviderVendor.OTMM).orElse(null));
		shExchangeProviderInstance.setEnabled(false);
		
		shExchangeProviderInstanceRepository.save(shExchangeProviderInstance);

		String providerInstance = String.format(PROVIDER_PATH, shExchangeProviderInstance.getId());

		ShConfigVar shConfigVar = new ShConfigVar();
		shConfigVar.setPath(providerInstance);
		shConfigVar.setName(URL);
		shConfigVar.setValue("http://localhost:11090");
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
