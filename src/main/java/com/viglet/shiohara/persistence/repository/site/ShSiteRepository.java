package com.viglet.shiohara.persistence.repository.site;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.site.ShSite;

public interface ShSiteRepository extends JpaRepository<ShSite, Integer> {

	List<ShSite> findAll();

	ShSite findById(UUID id);

	ShSite findByName(String name);

	ShSite save(ShSite shSite);

	@Modifying
	@Query("delete from ShWidget p where p.id = ?1")
	void delete(UUID shWidgetId);
}
