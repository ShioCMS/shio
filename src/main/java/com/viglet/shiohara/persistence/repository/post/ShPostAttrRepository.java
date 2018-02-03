package com.viglet.shiohara.persistence.repository.post;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;

public interface ShPostAttrRepository extends JpaRepository<ShPostAttr, Integer> {

	List<ShPostAttr> findAll();

	ShPostAttr findById(UUID id);

	ShPostAttr save(ShPostAttr shPostAttr);

	@Modifying
	@Query("delete from ShPostAttr pa where pa.id = ?1")
	void delete(UUID shPostAttrId);
}
