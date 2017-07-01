package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.ShPost;

public class ShPostService extends ShBaseService {
	public void save(ShPost shPost) {
		em.getTransaction().begin();
		em.persist(shPost);
		em.getTransaction().commit();
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
