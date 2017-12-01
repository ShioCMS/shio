package com.viglet.shiohara.persistence.repository.post.type;

import com.viglet.shiohara.persistence.model.ShPostTypeAttr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShPostTypeAttrRepository extends JpaRepository<ShPostTypeAttr, Integer> {

	List<ShPostTypeAttr> findAll();

	ShPostTypeAttr findById(int id);

	ShPostTypeAttr save(ShPostTypeAttr shPostTypeAttr);

	void delete(ShPostTypeAttr shPostTypeAttr);
}
