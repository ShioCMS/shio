package com.viglet.shiohara.persistence.repository.globalid;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;

public interface ShGlobalIdRepository extends JpaRepository<ShGlobalId, String> {

	List<ShGlobalId> findAll();

	Optional<ShGlobalId> findById(String id);
	
	ShGlobalId findByShObject(ShObject shObject);

	@SuppressWarnings("unchecked")
	ShGlobalId save(ShGlobalId shGlobalObject);

	@Modifying
	@Query("delete from ShGlobalId go where go.id = ?1")
	void delete(String shGlobalIdId);
}
