package com.viglet.shiohara.persistence.repository.reference;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.reference.ShReference;

public interface ShReferenceRepository extends JpaRepository<ShReference, String> {

	List<ShReference> findAll();

	Optional<ShReference> findById(String id);
	
	List<ShReference> findByShObjectFrom(ShObject shObjectFrom);
	
	List<ShReference> findByShObjectTo(ShObject shObjectTo);

	@SuppressWarnings("unchecked")
	ShReference save(ShReference shReference);

	@Modifying
	@Query("delete from ShReference r where r.id = ?1")
	void delete(String id);
}
