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
	
	Optional<ShFolder> findById(String id);

	@SuppressWarnings("unchecked")
	ShFolder save(ShFolder shFolder);

	@Modifying
	@Query("delete from ShFolder p where p.id = ?1")
	void delete(String shFolderId);
}
