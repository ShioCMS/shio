package com.viglet.shiohara.persistence.repository.post.type;

import com.viglet.shiohara.persistence.model.ShPostType;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShPostTypeRepository extends JpaRepository<ShPostType, Integer> {

	List<ShPostType> findAll();

	ShPostType findById(int id);

	ShPostType save(ShPostType shPostType);

	void delete(ShPostType shPostType);
}
