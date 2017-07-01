package com.viglet.shiohara.api.region;

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

import com.viglet.shiohara.persistence.model.ShRegion;
import com.viglet.shiohara.persistence.service.ShRegionService;

@Path("/region")
public class ShRegionAPI {
	ShRegionService shRegionService = new ShRegionService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShRegion> list() throws Exception {
		return shRegionService.listAll();
	}

	@Path("/{regionId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShRegion edit(@PathParam("regionId") int id) throws Exception {
		return shRegionService.get(id);
	}

	@Path("/{regionId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShRegion update(@PathParam("regionId") int id, ShRegion shRegion) throws Exception {
		ShRegion shRegionEdit = shRegionService.get(id);
		shRegionEdit.setName(shRegion.getName());
		shRegionEdit.setShPost(shRegion.getShPost());
		shRegionEdit.setShpostType(shRegion.getShPostType());		
		shRegionService.save(shRegionEdit);
		return shRegionEdit;
	}

	@Path("/{regionId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("regionId") int id) throws Exception {
		return shRegionService.delete(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(ShRegion shRegion) throws Exception {
		shRegionService.save(shRegion);
		String result = "Region saved: " + shRegion;
		return Response.status(201).entity(result).build();

	}

}