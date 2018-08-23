package com.viglet.shiohara.utils;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;

@Component
public class ShPostTypeUtils {
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;

	public Map<String, ShPostTypeAttr> toMap(ShPostType shPostType) {

		Set<ShPostTypeAttr> shPostTypeAttrList = shPostType.getShPostTypeAttrs();

		Map<String, ShPostTypeAttr> shPostTypeMap = new HashMap<String, ShPostTypeAttr>();
		for (ShPostTypeAttr shPostTypeAttr : shPostTypeAttrList)
			shPostTypeMap.put(shPostTypeAttr.getName(), shPostTypeAttr);

		return shPostTypeMap;

	}
}