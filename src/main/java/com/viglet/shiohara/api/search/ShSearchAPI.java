package com.viglet.shiohara.api.search;

import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
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

@Component
@RestController
@RequestMapping("/api/v2/search")
public class ShSearchAPI {

	@Autowired
	private HibernateSearchService searchservice;
	@Autowired
	private ShFolderUtils shFolderUtils;

	@RequestMapping(method = RequestMethod.GET)
	@JsonView({  ShJsonView.ShJsonViewObject.class })
	public List<ShPostWIthBreadcrumb> search(@RequestParam(value = "q") String q) throws Exception {
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
