package com.viglet.shiohara.persistence.repository.post;

import java.util.List;

import com.viglet.shiohara.persistence.model.post.ShPost;

interface ShPostRepositoryCustom {
	public boolean initializeHibernateSearch();
	public List<ShPost> fuzzySearch(String searchTerm);
}
