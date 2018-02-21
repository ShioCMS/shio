package com.viglet.shiohara.api.post;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShPostOutput extends ShPost {

	private static final long serialVersionUID = 1L;

	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;

	public ShPostOutput instance(ShPost shPost) {
		this.setId(shPost.getId());
		this.setDate(shPost.getDate());
		this.setShChannel(shPost.getShChannel());
		this.setShPostAttrs(shPost.getShPostAttrs());
		this.setShPostType(shPost.getShPostType());
		this.setShRegions(shPost.getShRegions());
		this.setSummary(shPost.getSummary());
		this.setTitle(shPost.getTitle());
		return this;

	}

	public UUID getGlobalId() {
		return shGlobalIdRepository.findByObjectId(this.getId()).getId();
	}

}
