package com.viglet.shiohara.persistence.repository.reference;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.model.reference.ShReferenceId;

public interface ShReferenceRepository extends JpaRepository<ShReference, ShReferenceId> {

	List<ShReference> findAll();

	ShReference findById(ShReferenceId id);
	
	List<ShReference> findByIdFromId(UUID fromId);
	
	List<ShReference> findByIdToId(UUID toId);

	ShReference save(ShReference shReference);

	@Modifying
	@Query("delete from ShReference r where r.id = ?1")
	void delete(ShReferenceId shReferenceId);
}
