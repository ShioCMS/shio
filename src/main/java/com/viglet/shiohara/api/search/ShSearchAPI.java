package com.viglet.shiohara.api.search;

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
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.search.HibernateSearchService;

@Component
@Path("/search")
public class ShSearchAPI {

	@Autowired
	private HibernateSearchService searchservice;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShPost> search(@QueryParam("q") String q) throws Exception {
		System.out.println("search");
		List<ShPost> searchResults = null;
		searchResults = searchservice.fuzzySearch(q);

		return searchResults;
	}
}
