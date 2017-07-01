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
import javax.ws.rs.core.Response;

import com.viglet.shiohara.persistence.model.ShSite;
import com.viglet.shiohara.persistence.service.ShSiteService;

@Path("/site")
public class ShSiteAPI {
	ShSiteService shSiteService = new ShSiteService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShSite> list() throws Exception {
		return shSiteService.listAll();
	}

	@Path("/{siteId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite edit(@PathParam("siteId") int id) throws Exception {
		return shSiteService.get(id);
	}

	@Path("/{siteId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite update(@PathParam("siteId") int id, ShSite shSite) throws Exception {
		ShSite shSiteEdit = shSiteService.get(id);
		shSiteEdit.setName(shSite.getName());
		shSiteService.save(shSiteEdit);
		return shSiteEdit;
	}

	@Path("/{siteId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("siteId") int id) throws Exception {
		return shSiteService.delete(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(ShSite shSite) throws Exception {
		shSiteService.save(shSite);
		String result = "Site saved: " + shSite;
		return Response.status(200).entity(result).build();

	}

}