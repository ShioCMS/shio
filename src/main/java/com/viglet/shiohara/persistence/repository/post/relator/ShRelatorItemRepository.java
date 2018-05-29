package com.viglet.shiohara.persistence.repository.post.relator;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;

public interface ShRelatorItemRepository extends JpaRepository<ShRelatorItem, String> {

	List<ShRelatorItem> findAll();
	
	Optional<ShRelatorItem> findById(String id);

	@SuppressWarnings("unchecked")
	ShRelatorItem save(ShRelatorItem shRelatorItem);

	@Modifying
	@Query("delete from ShRelatorItem ri where ri.id = ?1")
	void delete(String shRelatorItemId);
}
