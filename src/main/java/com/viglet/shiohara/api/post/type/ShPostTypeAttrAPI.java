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

import com.viglet.shiohara.persistence.model.ShPostTypeAttr;
import com.viglet.shiohara.persistence.service.ShPostTypeAttrService;

@Path("/post/type/attr")
public class ShPostTypeAttrAPI {
	ShPostTypeAttrService shPostTypeAttrService = new ShPostTypeAttrService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPostTypeAttr> list() throws Exception {
		return shPostTypeAttrService.listAll();
	}

	@Path("/{postTypeAttrId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr edit(@PathParam("postTypeAttrId") int id) throws Exception {
		return shPostTypeAttrService.get(id);
	}

	@Path("/{postTypeAttrId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr update(@PathParam("postTypeAttrId") int id, ShPostTypeAttr shPostTypeAttr) throws Exception {
		ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrService.get(id);
		shPostTypeAttrEdit.setIsSummary(shPostTypeAttr.getIsSummary());
		shPostTypeAttrEdit.setIsTitle(shPostTypeAttr.getIsTitle());
		shPostTypeAttrEdit.setLabel(shPostTypeAttr.getLabel());
		shPostTypeAttrEdit.setMany(shPostTypeAttr.getMany());
		shPostTypeAttrEdit.setName(shPostTypeAttr.getName());
		shPostTypeAttrEdit.setOrdinal(shPostTypeAttr.getOrdinal());
		shPostTypeAttrEdit.setRequired(shPostTypeAttr.getRequired());
		shPostTypeAttrEdit.setDescription(shPostTypeAttr.getDescription());
		shPostTypeAttrEdit.setShWidget(shPostTypeAttr.getShWidget());;
		shPostTypeAttrService.save(shPostTypeAttrEdit);
		return shPostTypeAttrEdit;
	}

	@Path("/{postTypeAttrId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postTypeAttrId") int id) throws Exception {
		return shPostTypeAttrService.delete(id);
	}

	@Deprecated
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(ShPostTypeAttr shPostTypeAttr) throws Exception {
		shPostTypeAttrService.save(shPostTypeAttr);
		String result = "Post Attrib saved: " + shPostTypeAttr;
		return Response.status(200).entity(result).build();

	}

}