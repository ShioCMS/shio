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
package com.viglet.shio.persistence.repository.post;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.viglet.shio.exchange.site.ShSiteImport;
import com.viglet.shio.persistence.model.post.ShPost;

/**
 * Post Repository Implementation.
 *
 * @author Alexandre Oliveira
 * @since 0.3.0
 */
public class ShPostRepositoryImpl implements ShPostRepositoryCustom {
	private static final Log logger = LogFactory.getLog(ShSiteImport.class);
	@PersistenceContext
	private EntityManager em;

	@Transactional
	public boolean initializeHibernateSearch() {
		boolean status = false;
		try {
			FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
			fullTextEntityManager.createIndexer().startAndWait();
			status = true;
		} catch (InterruptedException e) {
			logger.error("initializeHibernateSearchException", e);
			// Restore interrupted state...
		    Thread.currentThread().interrupt();
		}
		return status;
	}

	@SuppressWarnings("unchecked")
	@Transactional
	public List<ShPost> fuzzySearch(String searchTerm) {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(ShPost.class).get();
		Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1)
				.onFields("title", "summary", "shPostAttrs.strValue").matching(searchTerm).createQuery();

		javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, ShPost.class);

		return jpaQuery.getResultList();

	}
}