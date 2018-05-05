package com.viglet.shiohara.persistence.repository.post;

import java.util.List;

import javax.persistence.EntityManager;
import javax.persistence.PersistenceContext;

import org.springframework.transaction.annotation.Transactional;
import org.apache.lucene.search.Query;
import org.hibernate.search.jpa.FullTextEntityManager;
import org.hibernate.search.jpa.Search;
import org.hibernate.search.query.dsl.QueryBuilder;

import com.viglet.shiohara.persistence.model.post.ShPost;

import javax.persistence.NoResultException;

public class ShPostRepositoryImpl implements ShPostRepositoryCustom {

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
			e.printStackTrace();
		}
		return status;
	}

	@Transactional
	public List<ShPost> fuzzySearch(String searchTerm) {

		FullTextEntityManager fullTextEntityManager = Search.getFullTextEntityManager(em);
		QueryBuilder qb = fullTextEntityManager.getSearchFactory().buildQueryBuilder().forEntity(ShPost.class).get();
		Query luceneQuery = qb.keyword().fuzzy().withEditDistanceUpTo(1).withPrefixLength(1)
				.onFields("title", "summary", "shPostAttrs.strValue").matching(searchTerm).createQuery();

		javax.persistence.Query jpaQuery = fullTextEntityManager.createFullTextQuery(luceneQuery, ShPost.class);

		// execute search

		List<ShPost> BaseballCardList = null;
		try {
			BaseballCardList = jpaQuery.getResultList();
		} catch (NoResultException nre) {
			;// do nothing

		}

		return BaseballCardList;

	}
}