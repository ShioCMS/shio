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

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.SystemObjectView;
import com.viglet.shiohara.api.post.ShPostWIthBreadcrumb;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.search.HibernateSearchService;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
@Path("/search")
public class ShSearchAPI {

	@Autowired
	private HibernateSearchService searchservice;
	@Autowired
	private ShFolderUtils shFolderUtils;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShPostWIthBreadcrumb> search(@QueryParam("q") String q) throws Exception {
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
