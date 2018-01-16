package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.user.ShUser;

public class ShUserService extends ShBaseService {
	public void save(ShUser shUser) {
		em.getTransaction().begin();
		em.persist(shUser);
		em.getTransaction().commit();
	}

	public List<ShUser> listAll() {
		TypedQuery<ShUser> q = em.createNamedQuery("ShUser.findAll", ShUser.class);
		return q.getResultList();
	}

	public ShUser get(int userId) {
		return em.find(ShUser.class, userId);
	}

	public boolean delete(int userId) {
		ShUser shUser = em.find(ShUser.class, userId);
		em.getTransaction().begin();
		em.remove(shUser);
		em.getTransaction().commit();
		return true;
	}
}
