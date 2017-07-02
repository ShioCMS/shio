package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.ShPostTypeAttr;

public class ShPostTypeAttrService extends ShBaseService {
	public void save(ShPostTypeAttr shPostTypeAttr) {
		// Transform ShPostType unmanaged to managed
		if (shPostTypeAttr.getShPostType() != null) {
			shPostTypeAttr.setShPostType(em.merge(shPostTypeAttr.getShPostType()));
		}
		
		em.getTransaction().begin();
		if (shPostTypeAttr.getId() > 0) {
			em.merge(shPostTypeAttr);
		} else {
			em.persist(shPostTypeAttr);
		}
		em.getTransaction().commit();
	}

	public List<ShPostTypeAttr> listAll() {
		TypedQuery<ShPostTypeAttr> q = em.createNamedQuery("ShPostTypeAttr.findAll", ShPostTypeAttr.class);
		return q.getResultList();
	}

	public ShPostTypeAttr get(int postTypeAttrId) {
		return em.find(ShPostTypeAttr.class, postTypeAttrId);
	}

	public boolean delete(int postTypeAttrId) {
		ShPostTypeAttr shPostTypeAttr = em.find(ShPostTypeAttr.class, postTypeAttrId);
		em.getTransaction().begin();
		em.remove(shPostTypeAttr);
		em.getTransaction().commit();
		return true;
	}
}
