package com.viglet.shiohara.persistence.repository.post.type;

import com.viglet.shiohara.persistence.model.ShPostTypeAttr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

public interface ShPostTypeAttrRepository extends JpaRepository<ShPostTypeAttr, Integer> {

	List<ShPostTypeAttr> findAll();

	ShPostTypeAttr findById(int id);

	ShPostTypeAttr save(ShPostTypeAttr shPostTypeAttr);
	
	@Modifying
	@Query("delete from ShPostTypeAttr pta where pta.id = ?1")
	void delete(int shPostTypeAttrId);
}
