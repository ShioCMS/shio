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

package com.viglet.shiohara.onstartup.provider.exchange;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.provider.exchange.ShExchangeProviderVendor;
import com.viglet.shiohara.persistence.repository.provider.exchange.ShExchangeProviderVendorRepository;
import com.viglet.shiohara.provider.exchange.ShExchangeSystemProviderVendor;

@Component
public class ShExchangeProviderVendorOnStartup {

	@Autowired
	private ShExchangeProviderVendorRepository shExchangeProviderVendorRepository;

	public void createDefaultRows() {

		if (shExchangeProviderVendorRepository.findAll().isEmpty()) {

			ShExchangeProviderVendor shExchangeProviderVendor = new ShExchangeProviderVendor();
			shExchangeProviderVendor.setId("OTCS");
			shExchangeProviderVendor.setName(ShExchangeSystemProviderVendor.OTCS);
			shExchangeProviderVendor.setDescription(ShExchangeSystemProviderVendor.OTCS);
			shExchangeProviderVendor.setClassName("com.viglet.shiohara.provider.exchange.otcs.ShOTCSProvider");
			shExchangeProviderVendor.setImplementationCode("template/config/provider/exchange/otcs/otcs-configuration.html");

			shExchangeProviderVendorRepository.save(shExchangeProviderVendor);

			shExchangeProviderVendor = new ShExchangeProviderVendor();
			shExchangeProviderVendor.setId("OTMM");
			shExchangeProviderVendor.setName(ShExchangeSystemProviderVendor.OTMM);
			shExchangeProviderVendor.setDescription(ShExchangeSystemProviderVendor.OTMM);
			shExchangeProviderVendor.setClassName("com.viglet.shiohara.provider.exchange.otmm.ShOTMMProvider");
			shExchangeProviderVendor.setImplementationCode("template/config/provider/exchange/otmm/otmm-configuration.html");

			shExchangeProviderVendorRepository.save(shExchangeProviderVendor);
		}

	}
}
