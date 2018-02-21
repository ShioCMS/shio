package com.viglet.shiohara.api.site;

import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShSiteOutput extends ShSite {
	private static final long serialVersionUID = 1L;
	
	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;
	public ShSiteOutput newInstance(ShSite shSite) {
	this.setDate(shSite.getDate());
	this.setDescription(shSite.getDescription());
	this.setId(shSite.getId());
	this.setName(shSite.getName());
	this.setPostTypeLayout(shSite.getPostTypeLayout());
	this.setShChannels(shSite.getShChannels());
	this.setUrl(shSite.getUrl());
		return this;

	}
	public UUID getGlobalId() {
		return shGlobalIdRepository.findByObjectId(this.getId()).getId();
	}
}
