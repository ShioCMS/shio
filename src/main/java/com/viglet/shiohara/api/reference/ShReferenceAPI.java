package com.viglet.shiohara.api.reference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/reference")
@Api(tags="Reference", description="Reference API")
public class ShReferenceAPI {

	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceList() throws Exception {
		return shReferenceRepository.findAll();
	}

	@GetMapping("/from/{fromId}")
	@JsonView({  ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceFrom(@PathVariable UUID fromId) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(fromId).get();
		return shReferenceRepository.findByShGlobalFromId(shGlobalId);
	}
	@GetMapping("/to/{toId}")
	@JsonView({  ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceTo(@PathVariable UUID toId) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(toId).get();
		return shReferenceRepository.findByShGlobalToId(shGlobalId);
	}

	@PostMapping("/to/{toId}/replace/{otherId}")
	@JsonView({  ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceToReplace(@PathVariable UUID toId, @PathVariable UUID otherId)
			throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(toId).get();
		ShGlobalId shGlobaOtherlId = shGlobalIdRepository.findById(otherId).get();
		ShPost shPostOther = shPostRepository.findById(shGlobaOtherlId.getShObject().getId()).get();
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
