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

package com.viglet.shiohara.persistence.repository.folder;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;

public interface ShFolderRepository extends JpaRepository<ShFolder, String> {

	List<ShFolder> findAll();

	List<ShFolder> findByShSiteAndRootFolder(ShSite shSite, byte rootFolder);
	
	ShFolder findByShSiteAndName(ShSite shSite, String name);
	
	ShFolder findByShSiteAndFurl(ShSite shSite, String furl);
	
	ShFolder findByParentFolderAndName(ShFolder parentFolder, String name);
	
	ShFolder findByParentFolderAndFurl(ShFolder parentFolder, String furl);
	
	List<ShFolder> findByParentFolder(ShFolder parentFolder);
	
	List<ShFolder> findByParentFolderOrderByPositionAsc(ShFolder parentFolder);
	
	Optional<ShFolder> findById(String id);

	@SuppressWarnings("unchecked")
	ShFolder save(ShFolder shFolder);

	@Modifying
	@Query("delete from ShFolder p where p.id = ?1")
	void delete(String shFolderId);
}
