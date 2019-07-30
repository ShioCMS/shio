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

package com.viglet.shiohara.spring.security;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Service;

import com.viglet.shiohara.persistence.model.auth.ShGroup;
import com.viglet.shiohara.persistence.model.auth.ShRole;
import com.viglet.shiohara.persistence.model.auth.ShUser;
import com.viglet.shiohara.persistence.repository.auth.ShGroupRepository;
import com.viglet.shiohara.persistence.repository.auth.ShRoleRepository;
import com.viglet.shiohara.persistence.repository.auth.ShUserRepository;

@Service("customUserDetailsService")
public class ShCustomUserDetailsService implements UserDetailsService {
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShRoleRepository shRoleRepository;
	@Autowired
	private ShGroupRepository shGroupRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		ShUser shUser = shUserRepository.findByUsername(username);
		if (null == shUser) {
			throw new UsernameNotFoundException("No user present with username: " + username);
		} else {
			List<ShUser> users = new ArrayList<>();
			users.add(shUser);
			Set<ShGroup> shGroups = shGroupRepository.findByShUsersIn(users);
			Set<ShRole> shRoles = shRoleRepository.findByShGroupsIn(shGroups);

			List<String> roles = new ArrayList<>();
			for (ShRole shRole : shRoles) {
				roles.add(shRole.getName());
			}
			return new ShCustomUserDetails(shUser, roles);
		}
	}

}
