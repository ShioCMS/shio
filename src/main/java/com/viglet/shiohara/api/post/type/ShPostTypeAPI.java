package com.viglet.shiohara.api.post.type;

import java.util.List;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;

import org.json.JSONException;

import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.service.ShPostTypeService;

@Path("/post/type")
public class ShPostTypeAPI {
	ShPostTypeService shPostTypeService = new ShPostTypeService();
	
	@GET
	@Produces("application/json")
	public List<ShPostType> list() throws JSONException {
		return shPostTypeService.listAll();
	}

}
