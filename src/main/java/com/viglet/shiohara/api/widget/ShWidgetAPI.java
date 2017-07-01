package com.viglet.shiohara.api.widget;

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

import com.viglet.shiohara.persistence.model.ShWidget;
import com.viglet.shiohara.persistence.service.ShWidgetService;

@Path("/widget")
public class ShWidgetAPI {
	ShWidgetService shWidgetService = new ShWidgetService();

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShWidget> list() throws Exception {
		return shWidgetService.listAll();
	}

	@Path("/{widgetId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShWidget edit(@PathParam("widgetId") int id) throws Exception {
		return shWidgetService.get(id);
	}

	@Path("/{widgetId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShWidget update(@PathParam("widgetId") int id, ShWidget shWidget) throws Exception {
		ShWidget shWidgetEdit = shWidgetService.get(id);
		shWidgetEdit.setName(shWidget.getName());
		shWidgetEdit.setType(shWidget.getType());
		shWidgetEdit.setClassName(shWidget.getClassName());
		shWidgetEdit.setDescription(shWidget.getDescription());
		shWidgetEdit.setImplementationCode(shWidget.getImplementationCode());
		shWidgetService.save(shWidgetEdit);
		return shWidgetEdit;
	}

	@Path("/{widgetId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("widgetId") int id) throws Exception {
		return shWidgetService.delete(id);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public Response add(ShWidget shWidget) throws Exception {
		shWidgetService.save(shWidget);
		String result = "Widget saved: " + shWidget;
		return Response.status(200).entity(result).build();

	}

}