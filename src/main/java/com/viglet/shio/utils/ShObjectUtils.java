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
package com.viglet.shio.utils;

import java.security.Principal;
import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.persistence.model.auth.ShGroup;
import com.viglet.shio.persistence.model.auth.ShUser;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.object.ShObject;
import com.viglet.shio.persistence.model.post.ShPost;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.auth.ShUserRepository;
import com.viglet.shio.persistence.repository.object.ShObjectRepository;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShObjectUtils {
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	public ShSite getSite(ShObject shObject) {
		if (shObject instanceof ShPost) {
			return shPostUtils.getSite((ShPost) shObject);
		} else if (shObject instanceof ShFolder) {
			return shFolderUtils.getSite((ShFolder) shObject);
		} else {
			return null;
		}
	}

	public boolean canAccess(Principal principal, String shObjectId) {
		ShUser shUser = null;
		ShObject shObject = shObjectRepository.findById(shObjectId).orElse(null);
		if (shObject != null) {
			if (principal != null)
				shUser = shUserRepository.findByUsername(principal.getName());
			Set<String> shGroups = new HashSet<>();
			Set<String> shUsers = new HashSet<>();
			if (shUser != null && shUser.getShGroups() != null) {
				boolean fullAccess = false;
				for (ShGroup shGroup : shUser.getShGroups()) {
					if (shGroup.getName().equals("Administrator")) {
						fullAccess = true;
					}
				}
				if (fullAccess) {
					return true;
				} else {
					for (ShGroup shGroup: shUser.getShGroups()) {
						shGroups.add(shGroup.getName());
					}
					shUsers.add(shUser.getUsername());
					if (shObjectRepository.countByIdAndShGroupsInOrIdAndShUsersInOrIdAndShGroupsIsNullAndShUsersIsNull(
							shObject.getId(), shGroups, shObject.getId(), shUsers, shObject.getId()) > 0) {
						return true;
					}
				}
			} else {
				return true;
			}
		}
		
		return false;
	}
}
