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

import java.util.ArrayList;
import java.util.List;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Predicate;
import javax.persistence.criteria.Root;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.viglet.shio.persistence.model.post.ShPostAttr;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.repository.post.ShPostAttrRepository;

/**
 * @author Alexandre Oliveira
 * @since 0.3.7
 */

@Service
public class ShPostAttrServiceImpl implements ShPostAttrService {

	@Autowired
	private ShPostAttrRepository shPostAttrRepository;

	public List<ShPostAttr> findByShPostTypeAttrAndValue(ShPostTypeAttr shPostTypeAttr, String value) {
		return shPostAttrRepository.findAll(new Specification<ShPostAttr>() {
			
			private static final long serialVersionUID = 1L;

			@Override
			public Predicate toPredicate(Root<ShPostAttr> root, CriteriaQuery<?> query,
					CriteriaBuilder criteriaBuilder) {
				List<Predicate> predicates = new ArrayList<>();
				if (shPostTypeAttr != null) {
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("shPostTypeAttr"), shPostTypeAttr)));
					predicates.add(criteriaBuilder.and(criteriaBuilder.equal(root.get("strValue"), value)));
				}
				return criteriaBuilder.and(predicates.toArray(new Predicate[predicates.size()]));
			}
		});
	}

}
