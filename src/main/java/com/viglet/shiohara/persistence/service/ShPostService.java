package com.viglet.shiohara.persistence.service;

import java.util.ArrayList;
import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.ShPost;
import com.viglet.shiohara.persistence.model.ShPostAttr;
import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.model.ShPostTypeAttr;

public class ShPostService extends ShBaseService {
	public void save(ShPost shPost) {
		ShPostTypeAttrService shPostTypeAttrService = new ShPostTypeAttrService();
		List<ShPostAttr> shPostAttrs = new ArrayList<ShPostAttr>();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			
			shPostAttr.setShPostType(em.merge(shPostAttr.getShPostType()));

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrService.get(shPostAttr.getShPostTypeAttrId());
			shPostTypeAttr.setShPostType(em.merge(shPostTypeAttr.getShPostType()));
			shPostTypeAttr.setShWidget(em.merge(shPostTypeAttr.getShWidget()));

			shPostAttr.setShPostTypeAttr(em.merge(shPostTypeAttr));
			shPostAttrs.add(shPostAttr);	
		}
		shPost.setShPostAttrs(null);
		shPost.setShPostType(em.merge(shPost.getShPostType()));
		em.getTransaction().begin();
		em.merge(shPost);
		em.getTransaction().commit();
		
		for (ShPostAttr shPostAttr : shPostAttrs) {				
			em.getTransaction().begin();			
			em.merge(shPostAttr);
			em.getTransaction().commit();
		}
		
	}

	public void saveByPostType(ShPostType shPostType, ShPost shPost) {
		ShPostTypeAttrService shPostTypeAttrService = new ShPostTypeAttrService();
		List<ShPostAttr> shPostAttrs = new ArrayList<ShPostAttr>();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			
			shPostAttr.setShPostType(em.merge(shPostType));

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrService.get(shPostAttr.getShPostTypeAttrId());
			shPostTypeAttr.setShPostType(em.merge(shPostTypeAttr.getShPostType()));
			shPostTypeAttr.setShWidget(em.merge(shPostTypeAttr.getShWidget()));

			shPostAttr.setShPostTypeAttr(em.merge(shPostTypeAttr));
			shPostAttrs.add(shPostAttr);			
		}
		shPost.setShPostAttrs(null);
		shPost.setShPostType(em.merge(shPostType));
		em.getTransaction().begin();
		em.persist(shPost);
		em.getTransaction().commit();

		
		
		for (ShPostAttr shPostAttr : shPostAttrs) {	
			ShPostAttr shPostAttrNew = new ShPostAttr(); 
			shPostAttrNew.setDateValue(shPostAttr.getDateValue());
			shPostAttrNew.setIntValue(shPostAttr.getIntValue());
			shPostAttrNew.setShPost(em.merge(shPost));
			shPostAttrNew.setShPostType(shPostAttr.getShPostType());
			shPostAttrNew.setShPostTypeAttr(shPostAttr.getShPostTypeAttr());
			shPostAttrNew.setShPostTypeAttrId(shPostAttr.getShPostTypeAttrId());
			shPostAttrNew.setStrValue(shPostAttr.getStrValue());
			shPostAttrNew.setType(shPostAttr.getType());		
			em.getTransaction().begin();			
			em.persist(shPostAttrNew);
			em.getTransaction().commit();
		}
		

	}

	public List<ShPost> listAll() {
		TypedQuery<ShPost> q = em.createNamedQuery("ShPost.findAll", ShPost.class);
		return q.getResultList();
	}

	public ShPost get(int postId) {
		return em.find(ShPost.class, postId);
	}

	public boolean delete(int postId) {
		ShPost shPost = em.find(ShPost.class, postId);
		em.getTransaction().begin();
		em.remove(shPost);
		em.getTransaction().commit();
		return true;
	}
}
