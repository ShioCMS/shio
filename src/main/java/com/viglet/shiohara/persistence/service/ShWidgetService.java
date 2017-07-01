package com.viglet.shiohara.persistence.service;

import java.util.List;

import javax.persistence.TypedQuery;

import com.viglet.shiohara.persistence.model.ShWidget;

public class ShWidgetService extends ShBaseService {
	public void save(ShWidget shWidget) {
		em.getTransaction().begin();
		em.persist(shWidget);
		em.getTransaction().commit();
	}

	public List<ShWidget> listAll() {
		TypedQuery<ShWidget> q = em.createNamedQuery("ShWidget.findAll", ShWidget.class);
		return q.getResultList();
	}

	public ShWidget get(int widgetId) {
		return em.find(ShWidget.class, widgetId);
	}

	public boolean delete(int widgetId) {
		ShWidget shWidget = em.find(ShWidget.class, widgetId);
		em.getTransaction().begin();
		em.remove(shWidget);
		em.getTransaction().commit();
		return true;
	}
}
