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

package com.viglet.shiohara.api.auth;

import java.util.ArrayList;
import java.util.List;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AnonymousAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.auth.ShUser;
import com.viglet.shiohara.persistence.repository.auth.ShGroupRepository;
import com.viglet.shiohara.persistence.repository.auth.ShUserRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/user")
@Api(tags = "User", description = "User API")
public class ShUserAPI {

	@Autowired
	private PasswordEncoder passwordEncoder;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShGroupRepository shGroupRepository;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShUser> shUserList() {
		return shUserRepository.findAll();
	}

	@GetMapping("/current")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserCurrent() {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (!(authentication instanceof AnonymousAuthenticationToken)) {
			String currentUserName = authentication.getName();
			ShUser shUser = shUserRepository.findByUsername(currentUserName);
			shUser.setPassword(null);
			return shUser;
		}

		return null;
	}

	@GetMapping("/{username}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserEdit(@PathVariable String username) {
		ShUser shUser = shUserRepository.findByUsername(username);
		if (shUser != null) {
			shUser.setPassword(null);
			List<ShUser> shUsers = new ArrayList<>();
			shUsers.add(shUser);
			shUser.setShGroups(shGroupRepository.findByShUsersIn(shUsers));
		}
		return shUser;
	}

	@PutMapping("/{username}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserUpdate(@PathVariable String username, @RequestBody ShUser shUser) {
		ShUser shUserEdit = shUserRepository.findByUsername(username);
		if (shUserEdit != null) {
			shUserEdit.setFirstName(shUser.getFirstName());
			shUserEdit.setLastName(shUser.getLastName());
			if (shUser.getPassword() != null && shUser.getPassword().trim().length() > 0) {
				shUserEdit.setPassword(passwordEncoder.encode(shUser.getPassword()));
			}
			shUserEdit.setShGroups(shUser.getShGroups());
			shUserRepository.save(shUserEdit);
		}
		return shUserEdit;
	}

	@Transactional
	@DeleteMapping("/{username}")
	public boolean shUserDelete(@PathVariable String username) {
		shUserRepository.delete(username);
		return true;
	}

	@PostMapping("/{username}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserAdd(@PathVariable String username, @RequestBody ShUser shUser) {
		if (shUser.getPassword() != null && shUser.getPassword().trim().length() > 0) {
			shUser.setPassword(passwordEncoder.encode(shUser.getPassword()));
		}

		shUserRepository.save(shUser);

		return shUser;
	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShUser shUserStructure() {
		return new ShUser();

	}

}
