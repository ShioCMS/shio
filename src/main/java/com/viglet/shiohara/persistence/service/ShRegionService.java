package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.region.ShRegion;

public class ShRegionService extends ShBaseService {
	public void save(ShRegion shRegion) {
		em.getTransaction().begin();
		em.persist(shRegion);
		em.getTransaction().commit();
	}

	public List<ShRegion> listAll() {
		TypedQuery<ShRegion> q = em.createNamedQuery("ShRegion.findAll", ShRegion.class);
		return q.getResultList();
	}

	public ShRegion get(int regionId) {
		return em.find(ShRegion.class, regionId);
	}

	public boolean delete(int regionId) {
		ShRegion shRegion = em.find(ShRegion.class, regionId);
		em.getTransaction().begin();
		em.remove(shRegion);
		em.getTransaction().commit();
		return true;
	}
}
