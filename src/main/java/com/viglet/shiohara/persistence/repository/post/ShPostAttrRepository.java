package com.viglet.shiohara.persistence.repository.post;

import com.viglet.shiohara.persistence.model.ShPostAttr;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShPostAttrRepository extends JpaRepository<ShPostAttr, Integer> {

	List<ShPostAttr> findAll();

	ShPostAttr findById(int id);

	ShPostAttr save(ShPostAttr shPostAttr);

	void delete(ShPostAttr shPostAttr);
}
