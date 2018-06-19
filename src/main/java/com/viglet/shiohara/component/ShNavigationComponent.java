package com.viglet.shiohara.component;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShNavigationComponent {
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;

	public List<ShFolder> navigation(String siteName, boolean home) {
		ShSite shSite = shSiteRepository.findByName(siteName);
		ShFolder homeFolder = shFolderRepository.findByShSiteAndName(shSite, "Home");
		List<ShFolder> shFolders = new ArrayList<ShFolder>();
		if (home) {
			shFolders.add(homeFolder);
		}
		shFolders.addAll(shFolderRepository.findByParentFolderOrderByPositionAsc(homeFolder));
		return shFolders;
	}
	
	public List<ShFolder> navigationFolder(String folderId, boolean home) {
		ShFolder shParentFolder = shFolderRepository.findById(folderId).get();
		List<ShFolder> shFolders = shFolderRepository.findByParentFolderOrderByPositionAsc(shParentFolder);
		if (home) {
			shFolders.add(shParentFolder);
		}
		return shFolders;
	}
}
