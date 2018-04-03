package com.viglet.shiohara.api.search;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.api.post.ShPostWIthBreadcrumb;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.search.HibernateSearchService;
import com.viglet.shiohara.utils.ShFolderUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/search")
@Api(value="onlinestore", description="Operations pertaining to products in Online Store")
public class ShSearchAPI {

	@Autowired
	private HibernateSearchService searchservice;
	@Autowired
	private ShFolderUtils shFolderUtils;

	@ApiOperation(value = "View a list of available products",response = Iterable.class)    
	@RequestMapping(method = RequestMethod.GET)
	@JsonView({  ShJsonView.ShJsonViewObject.class })
	public List<ShPostWIthBreadcrumb> shSearch(@RequestParam(value = "q") String q) throws Exception {
		List<ShPostWIthBreadcrumb> searchResults = new ArrayList<ShPostWIthBreadcrumb>();
		for (ShPost shPost : searchservice.fuzzySearch(q)) {
			ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shPost.getShFolder());
			ShSite shSite = breadcrumb.get(0).getShSite();
			ShPostWIthBreadcrumb shPostWIthBreadcrumb = new ShPostWIthBreadcrumb();
			shPostWIthBreadcrumb.setShPost(shPost);
			shPostWIthBreadcrumb.setBreadcrumb(breadcrumb);
			shPostWIthBreadcrumb.setShSite(shSite);
			searchResults.add(shPostWIthBreadcrumb);
		}

		return searchResults;
	}
}
