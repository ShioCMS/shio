package com.viglet.shiohara.persistence.repository.site;

import com.viglet.shiohara.persistence.model.ShSite;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShSiteRepository extends JpaRepository<ShSite, Integer> {

	List<ShSite> findAll();

	ShSite findById(int id);

	ShSite save(ShSite shSite);

	void delete(ShSite shSite);
}
