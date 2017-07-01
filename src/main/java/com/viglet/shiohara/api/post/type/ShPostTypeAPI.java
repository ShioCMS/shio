package com.viglet.shiohara.api.post.type;

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

import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.service.ShPostTypeService;

@Path("/post/type")
public class ShPostTypeAPI {
	ShPostTypeService shPostTypeService = new ShPostTypeService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPostType> list() throws Exception {
		return shPostTypeService.listAll();
	}

	@Path("/{postTypeId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostType edit(@PathParam("postTypeId") int id) throws Exception {
		return shPostTypeService.get(id);
	}

	@Path("/{postTypeId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostType update(@PathParam("postTypeId") int id, ShPostType shPostType) throws Exception {
		ShPostType shPostTypeEdit = shPostTypeService.get(id);
		shPostTypeEdit.setDate(shPostType.getDate());
		shPostTypeEdit.setTitle(shPostType.getTitle());
		shPostTypeEdit.setDescription(shPostType.getDescription());
		shPostTypeEdit.setName(shPostType.getName());
		shPostTypeService.save(shPostTypeEdit);
		return shPostTypeEdit;
	}

	@Path("/{postTypeId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postTypeId") int id) throws Exception {
		return shPostTypeService.delete(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(ShPostType shPostType) throws Exception {
		shPostTypeService.save(shPostType);
		String result = "PostType saved : " + shPostType;
		return Response.status(201).entity(result).build();

	}

}