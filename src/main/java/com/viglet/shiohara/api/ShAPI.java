package com.viglet.shiohara.api;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.json.JSONException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
@Path("")
public class ShAPI {

	@Autowired
	ShAPIBean shAPIBean;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShAPIBean info() throws JSONException {

		shAPIBean.setProduct("Viglet Shiohara");

		return shAPIBean;
	}
}
