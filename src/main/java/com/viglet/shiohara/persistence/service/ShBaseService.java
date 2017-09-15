package com.viglet.shiohara.persistence.service;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

public class ShBaseService {

	EntityManagerFactory factory = Persistence.createEntityManagerFactory("shiohara-app");
	protected EntityManager em = factory.createEntityManager();

}
