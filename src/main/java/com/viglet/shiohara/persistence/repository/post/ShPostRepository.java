package com.viglet.shiohara.persistence.repository.post;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.post.ShPost;

public interface ShPostRepository extends JpaRepository<ShPost, Integer> {

	List<ShPost> findAll();

	ShPost findById(int id);
	
	ShPost findByTitle(String title);

	ShPost save(ShPost shPost);

	@Modifying
	@Query("delete from ShPost p where p.id = ?1")
	void delete(int shPostId);
}
