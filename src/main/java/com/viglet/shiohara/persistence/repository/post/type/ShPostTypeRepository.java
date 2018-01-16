package com.viglet.shiohara.persistence.repository.post.type;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viglet.shiohara.persistence.model.post.type.ShPostType;

public interface ShPostTypeRepository extends JpaRepository<ShPostType, Integer> {

	List<ShPostType> findAll();

	ShPostType findById(int id);
	
	ShPostType findByName(String name);

	ShPostType save(ShPostType shPostType);

	void delete(ShPostType shPostType);
}
