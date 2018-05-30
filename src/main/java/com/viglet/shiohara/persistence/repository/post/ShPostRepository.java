package com.viglet.shiohara.persistence.repository.post;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;

public interface ShPostRepository extends JpaRepository<ShPost, String>, ShPostRepositoryCustom {
	
	List<ShPost> findAll();

	List<ShPost> findByShFolder(ShFolder shFolder);
	
	List<ShPost> findByShFolderAndShPostType(ShFolder shFolder, ShPostType shPostType);
	
	Optional<ShPost> findById(String id);
	
	ShPost findByTitle(String title);
	
	ShPost findByShFolderAndTitle(ShFolder shFolder, String title);
	
	ShPost findByShFolderAndFurl(ShFolder shFolder, String furl);
	
	@SuppressWarnings("unchecked")
	ShPost save(ShPost shPost);

	@Modifying
	@Query("delete from ShPost p where p.id = ?1")
	void delete(String shPostId);
}
