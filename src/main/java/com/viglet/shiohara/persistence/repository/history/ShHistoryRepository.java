package com.viglet.shiohara.persistence.repository.history;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.history.ShHistory;

public interface ShHistoryRepository extends JpaRepository<ShHistory, String> {

	List<ShHistory> findAll();

	Set<ShHistory> findByShObject(String shObject);
	
	Set<ShHistory> findByShSite(String shSite);
	
	Optional<ShHistory> findById(String id);
	
	@SuppressWarnings("unchecked")
	ShHistory save(ShHistory shHistory);

	@Modifying
	@Query("delete from ShHistory h where h.id = ?1")
	void delete(String id);
}
