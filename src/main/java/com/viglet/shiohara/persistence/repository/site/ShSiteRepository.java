package com.viglet.shiohara.persistence.repository.site;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viglet.shiohara.persistence.model.site.ShSite;

public interface ShSiteRepository extends JpaRepository<ShSite, Integer> {

	List<ShSite> findAll();

	ShSite findById(int id);
	
	ShSite findByName(String name);

	ShSite save(ShSite shSite);

	void delete(ShSite shSite);
}
