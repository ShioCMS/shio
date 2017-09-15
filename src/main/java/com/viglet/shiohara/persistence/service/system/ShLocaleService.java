package com.viglet.shiohara.persistence.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.system.ShLocale;
import com.viglet.shiohara.persistence.service.ShBaseService;

public class ShLocaleService extends ShBaseService {

	public void save(ShLocale shLocale) {
		em.getTransaction().begin();
		em.persist(shLocale);
		em.getTransaction().commit();
	}

	public List<ShLocale> listAll() {
		TypedQuery<ShLocale> q = em.createNamedQuery("ShLocale.findAll", ShLocale.class);
		return q.getResultList();
	}

	public ShLocale get(String shLocaleId) {
		return em.find(ShLocale.class, shLocaleId);
	}

	public boolean delete(String shLocaleId) {
		ShLocale shLocale = em.find(ShLocale.class, shLocaleId);
		em.getTransaction().begin();
		em.remove(shLocale);
		em.getTransaction().commit();
		return true;
	}

}
