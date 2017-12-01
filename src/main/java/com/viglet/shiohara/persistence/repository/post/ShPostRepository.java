package com.viglet.shiohara.persistence.repository.post;

import com.viglet.shiohara.persistence.model.ShPost;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShPostRepository extends JpaRepository<ShPost, Integer> {

	List<ShPost> findAll();

	ShPost findById(int id);

	ShPost save(ShPost shPost);

	void delete(ShPost shPost);
}
