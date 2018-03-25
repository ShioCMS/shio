package com.viglet.shiohara.api.reference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.SystemObjectView;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;

@Component
@Path("/reference")

public class ShReferenceAPI {

	@Autowired
	ShReferenceRepository shReferenceRepository;
	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShReference.class })
	public List<ShReference> list() throws Exception {
		return shReferenceRepository.findAll();
	}

	@Path("/from/{fromId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShReference.class })
	public List<ShReference> from(@PathParam("fromId") UUID fromId) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(fromId);
		return shReferenceRepository.findByShGlobalFromId(shGlobalId);
	}

	@Path("/to/{toId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShReference.class })
	public List<ShReference> to(@PathParam("toId") UUID toId) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(toId);
		return shReferenceRepository.findByShGlobalToId(shGlobalId);
	}

	@Path("/to/{toId}/replace/{otherId}")
	@POST
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShReference.class })
	public List<ShReference> toReplace(@PathParam("toId") UUID toId, @PathParam("otherId") UUID otherId)
			throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(toId);
		ShGlobalId shGlobaOtherlId = shGlobalIdRepository.findById(otherId);
		ShPost shPostOther = shPostRepository.findById(shGlobaOtherlId.getShObject().getId());
		List<ShReference> shReferences = shReferenceRepository.findByShGlobalToId(shGlobalId);

		for (ShReference shReference : shReferences) {
			if (shReference != null && shReference.getShGlobalFromId() != null
					&& shReference.getShGlobalFromId().getType().equals("POST")) {
				ShPost shPost = (ShPost) shReference.getShGlobalFromId().getShObject();
				for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
					Set<ShObject> newReferences = new HashSet<ShObject>();
					for (ShObject shObjectReference : shPostAttr.getReferenceObjects()) {
						if (shObjectReference.getShGlobalId().getId().toString()
								.equals(shGlobalId.getId().toString())) {
							newReferences.add(shPostOther);
							shPostAttr.setStrValue(shPostOther.getId().toString());
						} else {
							newReferences.add(shObjectReference);
							shPostAttr.setStrValue(shObjectReference.getId().toString());
						}
					}
					shPostAttr.setReferenceObjects(newReferences);
					shPostAttrRepository.saveAndFlush(shPostAttr);
				}
			}
			shReference.setShGlobalToId(shGlobaOtherlId);
			shReferenceRepository.saveAndFlush(shReference);
		}
		return shReferenceRepository.findByShGlobalToId(shGlobalId);
	}
}
