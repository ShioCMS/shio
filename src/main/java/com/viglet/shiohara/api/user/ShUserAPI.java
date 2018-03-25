package com.viglet.shiohara.api.user;

import java.util.List;

import javax.servlet.ServletContext;
import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;

@Component
@Path("/user")
public class ShUserAPI {
	@Context
	ServletContext context;
	
	@Autowired
	ShUserRepository shUserRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShUser> list() throws Exception {
		return shUserRepository.findAll();
	}

	@Path("/current")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShUser current(@QueryParam("access_token") int accessToken) throws Exception {
		return shUserRepository.findById(accessToken);
	}

	@Path("/{userId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShUser edit(@PathParam("userId") int id) throws Exception {
		JSONObject jsonResponse = (JSONObject) context.getAttribute("VecJSONResponse");
		if (jsonResponse != null) {
			System.out.println("O atributo: " + jsonResponse.toString());
		}
		return shUserRepository.findById(id);
	}

	@Path("/{userId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShUser update(@PathParam("postId") int id, ShUser shUser) throws Exception {
		ShUser shUserEdit = shUserRepository.findById(id);
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
		shUserRepository.save(shUserEdit);
		return shUserEdit;
	}

	@Path("/{userId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("userId") int id) throws Exception {
		shUserRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShUser add(ShUser shUser) throws Exception {
		shUserRepository.save(shUser);
		return shUser;

	}

}
