package com.viglet.shiohara.api.reference;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
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
	private ShObjectRepository shObjectRepository;
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
	public List<ShReference> shReferenceFrom(@PathVariable String fromId) throws Exception {
		ShObject shObject = shObjectRepository.findById(fromId).get();
		return shReferenceRepository.findByShObjectFrom(shObject);
	}
	@GetMapping("/to/{toId}")
	@JsonView({  ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceTo(@PathVariable String toId) throws Exception {
		ShObject shObject = shObjectRepository.findById(toId).get();
		return shReferenceRepository.findByShObjectTo(shObject);
	}

	@PostMapping("/to/{toId}/replace/{otherId}")
	@JsonView({  ShJsonView.ShJsonViewReference.class })
	public List<ShReference> shReferenceToReplace(@PathVariable String toId, @PathVariable String otherId)
			throws Exception {
		ShObject shObject = shObjectRepository.findById(toId).get();
		ShObject shObjectOther = shObjectRepository.findById(otherId).get();
		ShPost shPostOther = shPostRepository.findById(shObjectOther.getId()).get();
		List<ShReference> shReferences = shReferenceRepository.findByShObjectTo(shObject);

		for (ShReference shReference : shReferences) {
			if (shReference != null && shReference.getShObjectFrom() != null
					&& shReference.getShObjectFrom() instanceof ShPost) {
				ShPost shPost = (ShPost) shReference.getShObjectFrom();
				for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
					Set<ShObject> newReferences = new HashSet<ShObject>();
					for (ShObject shObjectReference : shPostAttr.getReferenceObjects()) {
						if (shObjectReference.getId().toString()
								.equals(shObject.getId().toString())) {
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
			shReference.setShObjectTo(shObjectOther);
			shReferenceRepository.saveAndFlush(shReference);
		}
		return shReferenceRepository.findByShObjectTo(shObject);
	}
}
