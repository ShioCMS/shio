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
package com.viglet.shiohara.api;

import java.io.IOException;

import org.eclipse.jgit.api.errors.GitAPIException;
import org.eclipse.jgit.api.errors.JGitInternalException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.provider.storage.ShGitProvider;

import io.swagger.annotations.Api;

/**
 * @author Alexandre Oliveira
 * @since 0.3.0
 */
@RestController
@RequestMapping("/api/v2")
@Api(value = "/", tags = "Heartbeat", description = "Heartbeat")
public class ShAPI {

	@Autowired
	private ShAPIBean shAPIBean;

	@Autowired
	private ShGitProvider shGitProvider;

	@GetMapping
	public ShAPIBean shApiInfo() {

		shAPIBean.setProduct("Viglet Shiohara");

		return shAPIBean;
	}

	@GetMapping("test")
	public ShAPIBean testApi() {

		shAPIBean.setProduct("Test Api");

		shGitProvider.cloneRepository();
		try {
			shGitProvider.init();

			shGitProvider.newItem("174a27fc-337d-49fb-91b5-129baada3d72");
			shGitProvider.pushToRepo();
		} catch (JGitInternalException | IOException | GitAPIException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return shAPIBean;
	}

}
