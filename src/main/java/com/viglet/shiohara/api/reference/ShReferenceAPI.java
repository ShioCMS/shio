package com.viglet.shiohara.api.reference;

import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;

@Component
@Path("/reference")
public class ShReferenceAPI {

	@Autowired
	ShReferenceRepository shReferenceRepository;

	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShReference> list() throws Exception {
		return shReferenceRepository.findAll();
	}

	@Path("/from/{fromId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShReference> from(@PathParam("fromId") UUID fromId) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(fromId);
		return shReferenceRepository.findByShGlobalFromId(shGlobalId);
	}

	@Path("/to/{toId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShReference> to(@PathParam("toId") UUID toId) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(toId);
		return shReferenceRepository.findByShGlobalToId(shGlobalId);
	}
}