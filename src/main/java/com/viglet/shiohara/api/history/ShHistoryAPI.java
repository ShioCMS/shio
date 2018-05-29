package com.viglet.shiohara.api.history;

import java.util.List;
import java.util.Set;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.history.ShHistory;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.history.ShHistoryRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/history")
@Api(tags = "History", description = "History API")
public class ShHistoryAPI {

	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShHistoryRepository shHistoryRepository;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShHistory> shHistoryList() throws Exception {
		return shHistoryRepository.findAll();
	}

	@GetMapping("/object/{globalId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public Set<ShHistory> shHistoryByObject(@PathVariable String globalId) throws Exception {
		if (shGlobalIdRepository.findById(globalId).isPresent()) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(globalId).get();
			if (shGlobalId != null) {
				if (shGlobalId.getType().equals(ShObjectType.SITE)) {
					ShSite shSite = (ShSite) shGlobalId.getShObject();
					return shHistoryRepository.findByShSite(shSite.getId());
				} else {
					return shHistoryRepository.findByShObject(shGlobalId.getShObject().getId());
				}
			}
		}
		return null;
	}

}
