/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

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
	
	List<ShPost> findByShPostType(ShPostType shPostType);

	List<ShPost> findByShFolderAndShPostTypeOrderByPositionAsc(ShFolder shFolder, ShPostType shPostType);

	Optional<ShPost> findById(String id);

	List<ShPost> findByTitle(String title);

	ShPost findByShFolderAndTitle(ShFolder shFolder, String title);

	ShPost findByShFolderAndFurl(ShFolder shFolder, String furl);

	@SuppressWarnings("unchecked")
	ShPost save(ShPost shPost);

	@Modifying
	@Query("delete from ShPost p where p.id = ?1")
	void delete(String shPostId);
}
