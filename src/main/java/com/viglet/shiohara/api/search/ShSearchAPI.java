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

package com.viglet.shiohara.api.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.api.post.ShPostWithBreadcrumb;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/search")
@Api(tags = "Search", description = "Search for Shiohara Objects")
public class ShSearchAPI {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;

	@ApiOperation(value = "Search for Shiohara Objects")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShPostWithBreadcrumb> shSearch(@RequestParam(value = "q") String q) throws Exception {
		List<ShPostWithBreadcrumb> searchResults = new ArrayList<ShPostWithBreadcrumb>();
		for (ShPost shPost : shPostRepository.fuzzySearch(q)) {
			ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shPost.getShFolder());
			ShSite shSite = breadcrumb.get(0).getShSite();
			ShPostWithBreadcrumb shPostWithBreadcrumb = new ShPostWithBreadcrumb();
			shPostWithBreadcrumb.setShPost(shPost);
			shPostWithBreadcrumb.setBreadcrumb(breadcrumb);
			shPostWithBreadcrumb.setShSite(shSite);
			searchResults.add(shPostWithBreadcrumb);
		}

		return searchResults;
	}
}
