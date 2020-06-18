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
package com.viglet.shio.onstartup.user;

import java.util.Date;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.auth.ShGroup;
import com.viglet.shio.persistence.model.auth.ShUser;
import com.viglet.shio.persistence.model.post.type.ShPostType;
import com.viglet.shio.persistence.repository.auth.ShGroupRepository;
import com.viglet.shio.persistence.repository.auth.ShUserRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.post.type.ShSystemPostType;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShUserOnStartup {
	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShGroupRepository shGroupRepository;
	
	public void createDefaultRows() {

		if (shUserRepository.findAll().isEmpty()) {
			
			ShGroup shGroup = shGroupRepository.findByName("Administrator");
			
			Set<ShGroup> shGroups = new HashSet<>();
			shGroups.add(shGroup);
			
			ShPostType shPostType = shPostTypeRepository.findByName(ShSystemPostType.TEXT);

			ShUser shUser = new ShUser();

			shUser.setEmail("admin@localhost.local");
			shUser.setFirstName("Shio");
			shUser.setLastLogin(new Date());
			shUser.setLastName("Administrator");
			shUser.setLastPostType(String.valueOf(shPostType.getId()));
			shUser.setLoginTimes(0);
			shUser.setPassword(passwordEncoder.encode("admin"));
			shUser.setRealm("default");
			shUser.setUsername("admin");
			shUser.setEnabled(1);

			shUser.setShGroups(shGroups);
			
			shUserRepository.save(shUser);			
						
			
			shUser = new ShUser();

			shUser.setEmail("sample@localhost.local");
			shUser.setFirstName("Sample user");
			shUser.setLastLogin(new Date());
			shUser.setLastName("Sample");
			shUser.setLastPostType(String.valueOf(shPostType.getId()));
			shUser.setLoginTimes(0);
			shUser.setPassword(passwordEncoder.encode("sample123"));
			shUser.setRealm("default");
			shUser.setUsername("sample");
			shUser.setEnabled(1);
			
			shUserRepository.save(shUser);			
		}

	}
}
