package com.viglet.shiohara.persistence.service.system;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;
import com.viglet.shiohara.persistence.service.ShBaseService;

public class ShConfigVarService extends ShBaseService {
	public void save(ShConfigVar shConfigVar) {
		em.getTransaction().begin();
		em.persist(shConfigVar);
		em.getTransaction().commit();
	}

	public List<ShConfigVar> listAll() {
		TypedQuery<ShConfigVar> q = em.createNamedQuery("ShConfigVar.findAll", ShConfigVar.class);
		return q.getResultList();
	}

	public ShConfigVar get(String shConfigVarId) {
		return em.find(ShConfigVar.class, shConfigVarId);
	}

	public boolean delete(String shConfigVarId) {
		ShConfigVar shConfigVar = em.find(ShConfigVar.class, shConfigVarId);
		em.getTransaction().begin();
		em.remove(shConfigVar);
		em.getTransaction().commit();
		return true;
	}

}
