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
package com.viglet.shiohara.onstartup.provider.auth;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.provider.auth.ShAuthProviderInstance;
import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.repository.provider.auth.ShAuthProviderInstanceRepository;
import com.viglet.shiohara.persistence.repository.provider.auth.ShAuthProviderVendorRepository;
import com.viglet.shiohara.persistence.repository.system.ShConfigVarRepository;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShAuthProviderInstanceOnStartup {
	private static final String PROVIDER_PATH = "/provider/auth/%s";
	private static final String HOST = "HOST";
	private static final String PORT = "PORT";
	private static final String RESOURCE_ID = "RESOURCE_ID";
	private static final String SECRET_KEY = "SECRET_KEY";
	private static final String PARTITION = "PARTITION";
	private static final String DOMAIN = "DOMAIN";
	private static final String MEMBERSHIP_FILTER = "MEMBERSHIP_FILTER";
	private static final String USERNAME = "USERNAME";
	private static final String USER_FILTER = "USER_FILTER";
	private static final String USER_SCOPE = "USER_SCOPE";
	private static final String USER_DN = "USER_DN";
	private static final String GROUP_FILTER = "GROUP_FILTER";
	private static final String GROUP_SCOPE = "GROUP_SCOPE";
	private static final String GROUP_DN = "GROUP_DN";

	@Autowired
	private ShConfigVarRepository shConfigVarRepository;
	@Autowired
	private ShAuthProviderInstanceRepository shAuthProviderInstanceRepository;
	@Autowired
	private ShAuthProviderVendorRepository shAuthProviderVendorRepository;

	public void createDefaultRows() {

		if (shAuthProviderInstanceRepository.findAll().isEmpty()) {

			this.createOTDSInstance();
		}
	}

	private void createOTDSInstance() {
		ShAuthProviderInstance shAuthProviderInstance = new ShAuthProviderInstance();
		shAuthProviderInstance.setName("OpenText Directory Services");
		shAuthProviderInstance.setDescription("Sample of OTDS");
		shAuthProviderInstance.setVendor(shAuthProviderVendorRepository.findById("OTDS").orElse(null));

		shAuthProviderInstanceRepository.save(shAuthProviderInstance);

		this.createAttribute(shAuthProviderInstance.getId(), HOST, "otds-hostname");
		this.createAttribute(shAuthProviderInstance.getId(), PORT, "8080");
		this.createAttribute(shAuthProviderInstance.getId(), RESOURCE_ID, "enter-otds-resource-id");
		this.createAttribute(shAuthProviderInstance.getId(), SECRET_KEY, "enter-otds-secret-key");
		this.createAttribute(shAuthProviderInstance.getId(), PARTITION, "otds.admin");
		this.createAttribute(shAuthProviderInstance.getId(), DOMAIN, "otds.admin");
		this.createAttribute(shAuthProviderInstance.getId(), MEMBERSHIP_FILTER,
				"(&(oTMember=%M)(objectclass=oTGroup))");
		this.createAttribute(shAuthProviderInstance.getId(), USERNAME, "oTExternalID3");
		this.createAttribute(shAuthProviderInstance.getId(), USER_FILTER, "(&(oTUserID1=%u)(objectclass=oTPerson))");
		this.createAttribute(shAuthProviderInstance.getId(), USER_SCOPE, "subtree");
		this.createAttribute(shAuthProviderInstance.getId(), USER_DN,
				"ou=Root,ou=otds.admin,ou=IdentityProviders,dc=identity,dc=opentext,dc=net");
		this.createAttribute(shAuthProviderInstance.getId(), GROUP_FILTER, "(&(cn=%g)(objectclass=oTGroup))");
		this.createAttribute(shAuthProviderInstance.getId(), GROUP_SCOPE, "subtree");
		this.createAttribute(shAuthProviderInstance.getId(), GROUP_DN,
				"ou=Root,ou=otds.admin,ou=IdentityProviders,dc=identity,dc=opentext,dc=net");
	}

	private void createAttribute(String instanceId, String key, String value) {
		String providerInstance = String.format(PROVIDER_PATH, instanceId);
		ShConfigVar shConfigVar = new ShConfigVar();
		shConfigVar.setPath(providerInstance);
		shConfigVar.setName(key);
		shConfigVar.setValue(value);
		shConfigVarRepository.save(shConfigVar);
	}
}
