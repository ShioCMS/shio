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

import com.viglet.shiohara.persistence.model.provider.ShProviderVendor;
import com.viglet.shiohara.persistence.repository.provider.ShProviderVendorRepository;
import com.viglet.shiohara.provider.ShSystemProviderVendor;

@Component
public class ShProviderVendorOnStartup {

	@Autowired
	private ShProviderVendorRepository shProviderVendorRepository;

	public void createDefaultRows() {

		if (shProviderVendorRepository.findAll().isEmpty()) {

			ShProviderVendor shProviderVendor = new ShProviderVendor();
			shProviderVendor.setId("OTCS");
			shProviderVendor.setName(ShSystemProviderVendor.OTCS);
			shProviderVendor.setDescription(ShSystemProviderVendor.OTCS);
			shProviderVendor.setClassName("com.viglet.shiohara.provider.otcs.ShOTCSProvider");
			shProviderVendor.setImplementationCode("template/provider/otcs/configuration.html");

			shProviderVendorRepository.save(shProviderVendor);

			shProviderVendor = new ShProviderVendor();
			shProviderVendor.setId("OTMM");
			shProviderVendor.setName(ShSystemProviderVendor.OTMM);
			shProviderVendor.setDescription(ShSystemProviderVendor.OTMM);
			shProviderVendor.setClassName("com.viglet.shiohara.provider.otmm.ShOTMMProvider");
			shProviderVendor.setImplementationCode("template/provider/otmm/configuration.html");

			shProviderVendorRepository.save(shProviderVendor);
		}

	}
}
