package com.viglet.shiohara.persistence.repository.system;

import com.viglet.shiohara.persistence.model.system.ShLocale;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShLocaleRepository extends JpaRepository<ShLocale, String> {

	static final String EN_US = "en_US";
	static final String EN_GB = "en_GB";
	static final String PT_BR = "pt_BR";
	
	List<ShLocale> findAll();

	ShLocale findByInitials(String initials);

	ShLocale save(ShLocale turLocale);

	void delete(ShLocale turConfigVar);
}
