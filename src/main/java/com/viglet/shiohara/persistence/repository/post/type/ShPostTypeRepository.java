package com.viglet.shiohara.persistence.repository.post.type;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;

public interface ShPostTypeRepository extends JpaRepository<ShPostType, UUID> {

	List<ShPostType> findAll();

	Optional<ShPostType> findById(UUID id);
	
	ShPostType findByName(String name);

	ShPostType save(ShPostType shPostType);

	void delete(ShPostType shPostType);
	
	@Modifying
	@Query("delete from ShPostType pt where pt.id = ?1")
	void delete(UUID shPostTypeId);
}
