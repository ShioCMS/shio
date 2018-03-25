package com.viglet.shiohara.persistence.repository.folder;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;

public interface ShFolderRepository extends JpaRepository<ShFolder, UUID> {

	List<ShFolder> findAll();

	List<ShFolder> findByShSiteAndRootFolder(ShSite shSite, byte rootFolder);
	
	ShFolder findByShSiteAndName(ShSite shSite, String name);
	
	ShFolder findByParentFolderAndName(ShFolder parentFolder, String name);
	
	List<ShFolder> findByParentFolder(ShFolder parentFolder);
	
	ShFolder findById(UUID id);

	ShFolder save(ShFolder shFolder);

	@Modifying
	@Query("delete from ShFolder p where p.id = ?1")
	void delete(UUID shFolderId);
}
