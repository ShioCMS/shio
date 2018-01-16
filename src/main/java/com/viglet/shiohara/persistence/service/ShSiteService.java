package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.site.ShSite;

public class ShSiteService extends ShBaseService {
	public void save(ShSite shSite) {
		em.getTransaction().begin();
		em.persist(shSite);
		em.getTransaction().commit();
	}

	public List<ShSite> listAll() {
		TypedQuery<ShSite> q = em.createNamedQuery("ShSite.findAll", ShSite.class);
		return q.getResultList();
	}

	public ShSite get(int siteId) {
		return em.find(ShSite.class, siteId);
	}

	public boolean delete(int siteId) {
		ShSite shSite = em.find(ShSite.class, siteId);
		em.getTransaction().begin();
		em.remove(shSite);
		em.getTransaction().commit();
		return true;
	}
}
