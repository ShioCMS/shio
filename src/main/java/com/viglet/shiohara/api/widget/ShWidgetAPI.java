package com.viglet.shiohara.api.widget;

import java.util.List;
import java.util.UUID;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

@Component
@Path("/widget")
public class ShWidgetAPI {
	
	@Autowired
	ShWidgetRepository shWidgetRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShWidget> list() throws Exception {
		return shWidgetRepository.findAll();
	}

	@Path("/{widgetId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShWidget edit(@PathParam("widgetId") UUID id) throws Exception {
		return shWidgetRepository.findById(id);
	}

	@Path("/{widgetId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShWidget update(@PathParam("widgetId") UUID id, ShWidget shWidget) throws Exception {
		ShWidget shWidgetEdit = shWidgetRepository.findById(id);
		shWidgetEdit.setName(shWidget.getName());
		shWidgetEdit.setType(shWidget.getType());
		shWidgetEdit.setClassName(shWidget.getClassName());
		shWidgetEdit.setDescription(shWidget.getDescription());
		shWidgetEdit.setImplementationCode(shWidget.getImplementationCode());
		shWidgetRepository.save(shWidgetEdit);
		return shWidgetEdit;
	}

	@Path("/{widgetId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("widgetId") UUID id) throws Exception {
		shWidgetRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShWidget add(ShWidget shWidget) throws Exception {
		shWidgetRepository.save(shWidget);
		return shWidget;

	}

}