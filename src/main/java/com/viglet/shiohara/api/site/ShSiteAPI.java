package com.viglet.shiohara.api.site;

import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
@Path("/site")
public class ShSiteAPI {
	
	@Autowired
	ShSiteRepository shSiteRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShSite> list() throws Exception {
		return shSiteRepository.findAll();
	}

	@Path("/{siteId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite edit(@PathParam("siteId") int id) throws Exception {
		return shSiteRepository.findById(id);
	}

	@Path("/{siteId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite update(@PathParam("siteId") int id, ShSite shSite) throws Exception {
		ShSite shSiteEdit = shSiteRepository.findById(id);
		shSiteEdit.setName(shSite.getName());
		shSiteRepository.save(shSiteEdit);
		return shSiteEdit;
	}

	@Path("/{siteId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("siteId") int id) throws Exception {
		shSiteRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShSite add(ShSite shSite) throws Exception {
		shSiteRepository.save(shSite);
		return shSite;

	}

}