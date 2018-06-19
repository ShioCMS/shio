package com.viglet.shiohara.persistence.repository.object;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.object.ShObject;

public interface ShObjectRepository extends JpaRepository<ShObject, String> {

	List<ShObject> findAll();

	Optional<ShObject> findById(String id);
	
	@SuppressWarnings("unchecked")
	ShObject save(ShObject shObject);

	@Modifying
	@Query("delete from ShObject o where o.id = ?1")
	void delete(String shObjectId);
}
