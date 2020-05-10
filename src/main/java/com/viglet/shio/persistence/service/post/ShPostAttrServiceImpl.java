/*
 * Copyright (C) 2016-2020 the original author or authors. 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */
package com.viglet.shio.persistence.service.post;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;

import static com.viglet.shio.persistence.spec.post.ShPostAttrSpecs.conditionParams;
import static com.viglet.shio.persistence.spec.post.ShPostAttrSpecs.hasShPostTypeAttr;
import static com.viglet.shio.persistence.spec.post.ShPostAttrSpecs.hasSite;
import static org.springframework.data.jpa.domain.Specification.where;

/**
 * @author Alexandre Oliveira
 * @since 0.3.7
 */
@Service
public class ShPostAttrServiceImpl implements ShPostAttrService {

	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;

	public List<ShPostAttr> findByShPostTypeAttrAndValueAndConditionAndSites(ShPostTypeAttr shPostTypeAttr,
			String value, String condition, List<String> siteIds) {

		Specification<ShPostAttr> specFinal = null;
		Specification<ShPostAttr> specs = where(conditionParams(condition, value))
				.and(hasShPostTypeAttr(shPostTypeAttr));
		specFinal = specs;
		if (siteIds != null && !siteIds.isEmpty()) {
			ShSite shSite = shSiteRepository.findById(siteIds.get(0)).orElse(null);
			specFinal = specs.and(hasSite(shSite));
		}
		
		return shPostAttrRepository.findAll(specFinal);
	}

}
