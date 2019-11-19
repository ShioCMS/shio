/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.provider.otcs.ShOTCSProvider;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2")
@Api(value="/", tags="Heartbeat", description="Heartbeat")
public class ShAPI {

	@Autowired
	private ShAPIBean shAPIBean;
	@Autowired
	private ShOTCSProvider shOTCSProvider;
	@GetMapping
	public ShAPIBean shApiInfo() throws JSONException {

		shAPIBean.setProduct("Viglet Shiohara");

		return shAPIBean;
	}
	
	@GetMapping("/test")
	public void shApiTest() {
		shOTCSProvider.init("http://localhost/OTCS/cs.exe", "admin", "admin");
		System.out.println(shOTCSProvider.rootFolder().getCollection().getPaging().getLimit());
	}
}
