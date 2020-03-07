/*
 * Copyright (C) 2016-2020 the original author or authors. 
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
package com.viglet.shio.persistence.repository.folder;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viglet.shio.bean.ShFolderTinyBean;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.site.ShSite;

/**
 * @author Alexandre Oliveira
 */
@Repository
public interface ShFolderRepository extends JpaRepository<ShFolder, String> {

	List<ShFolder> findAll();

	@Query("select new com.viglet.shio.bean.ShFolderTinyBean(f.id, f.name, f.position, f.date) from ShFolder f where f.shSite = ?1 and f.rootFolder = ?2")
	Set<ShFolderTinyBean> findByShSiteAndRootFolderTiny(ShSite shSite, byte rootFolder);
	
	
	Set<ShFolder> findByShSiteAndRootFolder(ShSite shSite, byte rootFolder);

	ShFolder findByShSiteAndName(ShSite shSite, String name);

	ShFolder findByShSiteAndFurl(ShSite shSite, String furl);

	ShFolder findByParentFolderAndName(ShFolder parentFolder, String name);

	ShFolder findByParentFolderAndFurl(ShFolder parentFolder, String furl);

	@Query("select new com.viglet.shio.bean.ShFolderTinyBean(f.id, f.name, f.position, f.date) from ShFolder f where f.parentFolder = ?1")
	Set<ShFolderTinyBean> findByParentFolderTiny(ShFolder parentFolder);

	Set<ShFolder> findByParentFolder(ShFolder parentFolder);

	List<ShFolder> findByParentFolderOrderByPositionAsc(ShFolder parentFolder);

	Optional<ShFolder> findById(String id);

	@SuppressWarnings("unchecked")
	ShFolder save(ShFolder shFolder);

	@Modifying
	@Query("delete from ShFolder p where p.id = ?1")
	void delete(String shFolderId);
}
