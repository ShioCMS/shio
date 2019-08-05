package com.viglet.shiohara.api.post.xp;
/*
 * Copyright (C) 2016-2019 the original author or authors. 
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

import java.security.Principal;

import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.bean.xp.ShPostXP;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.utils.ShPostUtils;

import io.swagger.annotations.Api;

/**
 * Post API.
 *
 * @author Alexandre Oliveira
 * @since 0.3.5
 */
@RestController
@RequestMapping("/api/v2/post/xp")
@Api(tags = "Post XP", description = "Post XP API")
public class ShPostXPAPI {

	@SuppressWarnings("unused")
	private static final Log logger = LogFactory.getLog(ShPostXPAPI.class);

	@Autowired
	private ShPostUtils shPostUtils;

	/**
	 * Post XP Edit API
	 * 
	 * @param id Post Id
	 * @return ShPost
	 */
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShPostXP shPostEdit(@PathVariable String id, Principal principal) {
		ShPostXP shPostXP = new ShPostXP();

		ShPost shPost = shPostUtils.loadLazyPost(id, false);
		shPostUtils.syncWithPostType(shPost);

		shPostXP.setShPost(shPost);
		shPostXP.setAllowPublish(shPostUtils.allowPublish(shPost.getShPostType(), principal));
		return shPostXP;
	}
}
