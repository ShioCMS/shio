package com.viglet.shiohara.api.history;

import java.util.List;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.history.ShHistory;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.repository.history.ShHistoryRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/history")
@Api(tags = "History", description = "History API")
public class ShHistoryAPI {

	@Autowired
	private ShObjectRepository shObjectRepository;
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
		if (shObjectRepository.findById(globalId).isPresent()) {
			ShObject shObject = shObjectRepository.findById(globalId).get();
			if (shObject != null) {
				if (shObject instanceof ShSite) {
					ShSite shSite = (ShSite) shObject;
					return shHistoryRepository.findByShSite(shSite.getId());
				} else {
					return shHistoryRepository.findByShObject(shObject.getId());
				}
			}
		}
		return null;
	}

}
