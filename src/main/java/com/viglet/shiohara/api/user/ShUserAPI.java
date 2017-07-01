package com.viglet.shiohara.api.user;

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

import com.viglet.shiohara.persistence.model.ShUser;
import com.viglet.shiohara.persistence.service.ShUserService;

@Path("/user")
public class ShUserAPI {
	ShUserService shUserService = new ShUserService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShUser> list() throws Exception {
		return shUserService.listAll();
	}

	@Path("/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShUser edit(@PathParam("userId") int id) throws Exception {
		return shUserService.get(id);
	}

	@Path("/{userId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShUser update(@PathParam("postId") int id, ShUser shUser) throws Exception {
		ShUser shUserEdit = shUserService.get(id);
		shUserEdit.setConfirmEmail(shUser.getConfirmEmail());
		shUserEdit.setEmail(shUser.getEmail());
		shUserEdit.setFirstName(shUser.getFirstName());
		shUserEdit.setLastLogin(shUser.getLastLogin());
		shUserEdit.setLastName(shUser.getLastName());
		shUserEdit.setLastPostType(shUser.getLastPostType());
		shUserEdit.setLoginTimes(shUser.getLoginTimes());
		shUserEdit.setPassword(shUser.getPassword());
		shUserEdit.setRealm(shUser.getRealm());
		shUserEdit.setRecoverPassword(shUser.getRecoverPassword());
		shUserEdit.setUsername(shUser.getUsername());
		shUserService.save(shUserEdit);
		return shUserEdit;
	}

	@Path("/{userId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("userId") int id) throws Exception {
		return shUserService.delete(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(ShUser shUser) throws Exception {
		shUserService.save(shUser);
		String result = "Post saved: " + shUser;
		return Response.status(200).entity(result).build();

	}

}