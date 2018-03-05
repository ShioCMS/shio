package com.viglet.shiohara.api.reference;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
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
	public ShReferences from(@PathParam("fromId") UUID fromId) throws Exception {
		List<ShReference> shReferences = shReferenceRepository.findByIdFromId(fromId);
		Set<ShObject> shObjects = new HashSet<ShObject>();

		for (ShReference shReference : shReferences) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(shReference.getId().getToId());
			shObjects.add(shGlobalId.getShObject());
		}
		ShReferences shReferencesObject = new ShReferences();
		shReferencesObject.setShObjects(shObjects);
		return shReferencesObject;
	}

	@Path("/to/{toId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShObject> to(@PathParam("toId") UUID toId) throws Exception {
		List<ShReference> shReferences = shReferenceRepository.findByIdFromId(toId);
		List<ShObject> shObjects = new ArrayList<ShObject>();

		for (ShReference shReference : shReferences) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(shReference.getId().getFromId());
			shObjects.add(shGlobalId.getShObject());
		}

		return shObjects;
	}
}