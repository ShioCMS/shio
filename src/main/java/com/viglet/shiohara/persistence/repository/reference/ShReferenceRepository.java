package com.viglet.shiohara.persistence.repository.reference;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.reference.ShReference;

public interface ShReferenceRepository extends JpaRepository<ShReference, UUID> {

	List<ShReference> findAll();

	Optional<ShReference> findById(UUID id);
	
	List<ShReference> findByShGlobalFromId(ShGlobalId shGlobalFromId);
	
	List<ShReference> findByShGlobalToId(ShGlobalId shGlobalToId);

	ShReference save(ShReference shReference);

	@Modifying
	@Query("delete from ShReference r where r.id = ?1")
	void delete(UUID id);
}
