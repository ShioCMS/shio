package com.viglet.shiohara.persistence.repository.site;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.site.ShSite;

public interface ShSiteRepository extends JpaRepository<ShSite, String> {

	List<ShSite> findAll();

	Optional<ShSite> findById(String id);

	List<ShSite> findByOwner(String owner);
	
	ShSite findByName(String name);
	
	ShSite findByFurl(String furl);

	@SuppressWarnings("unchecked")
	ShSite save(ShSite shSite);

	@Modifying
	@Query("delete from ShSite p where p.id = ?1")
	void delete(String shSiteId);
}
