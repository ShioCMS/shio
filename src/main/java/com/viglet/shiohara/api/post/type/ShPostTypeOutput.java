package com.viglet.shiohara.api.post.type;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShPostTypeOutput extends ShPostType {
	private static final long serialVersionUID = 1L;

	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;
	

	public ShPostTypeOutput newInstance(ShPostType shPostType) {
		this.setId(shPostType.getId());
		this.setDate(shPostType.getDate());		
		this.setTitle(shPostType.getTitle());
		this.setDescription(shPostType.getDescription());
		this.setName(shPostType.getName());
		this.setShPosts(shPostType.getShPosts());
		this.setShPostTypeAttrs(shPostType.getShPostTypeAttrs());
		this.setShRegions(shPostType.getShRegions());
		this.setSystem(shPostType.getSystem());
		return this;

	}

	public UUID getGlobalId() {
		return shGlobalIdRepository.findByObjectId(this.getId()).getId();
	}
}
