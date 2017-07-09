package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.ShPostAttr;
import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.model.ShPostTypeAttr;

public class ShPostAttrService extends ShBaseService {
	public void save(ShPostAttr shPostAttr) {
		// Transform ShPostType unmanaged to managed		
		if (shPostAttr.getShPostType() != null) {
			shPostAttr.setShPostType(em.merge(shPostAttr.getShPostType()));
		}
		if (shPostAttr.getShPost() != null) {
			shPostAttr.setShPost(em.merge(shPostAttr.getShPost()));
		}
		
		em.getTransaction().begin();
		if (shPostAttr.getId() > 0) {
			em.merge(shPostAttr);
		} else {
			em.persist(shPostAttr);
		}
		em.getTransaction().commit();
	}

	public void saveByPostType(ShPostType shPostType, ShPostAttr shPostAttr) {
		
		shPostAttr.setShPostType(em.merge(shPostType));
		System.out.println(shPostAttr.getShPostType().getName());
		ShPostTypeAttrService shPostTypeAttrService = new ShPostTypeAttrService();
		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrService.get(shPostAttr.getShPostTypeAttrId());
		shPostTypeAttr.setShPostType(em.merge(shPostType));
		shPostTypeAttr.setShWidget(em.merge(shPostTypeAttr.getShWidget()));
		shPostAttr.setShPostTypeAttr(em.merge(shPostTypeAttr));
		
		// Transform ShPostType unmanaged to managed
		if (shPostAttr.getShPost() != null) {
			shPostAttr.setShPost(em.merge(shPostAttr.getShPost()));
		}
		
		em.getTransaction().begin();
		if (shPostAttr.getId() > 0) {
			System.out.println("Merge");
			em.merge(shPostAttr);
		} else {
			System.out.println("Persist");
			em.persist(shPostAttr);
		}
		em.getTransaction().commit();
	}
	public List<ShPostAttr> listAll() {
		TypedQuery<ShPostAttr> q = em.createNamedQuery("ShPostAttr.findAll", ShPostAttr.class);
		return q.getResultList();
	}

	public ShPostAttr get(int postAttrId) {
		return em.find(ShPostAttr.class, postAttrId);
	}

	public boolean delete(int postAttrId) {
		ShPostAttr shPostAttr = em.find(ShPostAttr.class, postAttrId);
		em.getTransaction().begin();
		em.remove(shPostAttr);
		em.getTransaction().commit();
		return true;
	}
}
