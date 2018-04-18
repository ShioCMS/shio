package com.viglet.shiohara.onstartup.folder;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShFolderOnStartup {

	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	
	public void createDefaultRows() {

		if (shFolderRepository.findAll().isEmpty()) {

			ShSite shSite = shSiteRepository.findByName("Sample");

			// System Folder
			ShFolder shFolderSystem = new ShFolder();
			shFolderSystem.setName("System");
			shFolderSystem.setParentFolder(null);
			shFolderSystem.setShSite(shSite);
			shFolderSystem.setDate(new Date());
			shFolderSystem.setRootFolder((byte) 1);
			shFolderSystem.setOwner("admin");
			shFolderSystem.setFurl(shURLFormatter.format(shFolderSystem.getName()));
			
			shFolderRepository.save(shFolderSystem);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystem);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// System > Templates Folder
			ShFolder shFolderSystemTemplates = new ShFolder();
			shFolderSystemTemplates.setName("Templates");
			shFolderSystemTemplates.setParentFolder(shFolderSystem);
			shFolderSystemTemplates.setShSite(shSite);
			shFolderSystemTemplates.setDate(new Date());
			shFolderSystemTemplates.setRootFolder((byte) 0);
			shFolderSystemTemplates.setOwner("admin");
			shFolderSystemTemplates.setFurl(shURLFormatter.format(shFolderSystemTemplates.getName()));
			
			shFolderRepository.save(shFolderSystemTemplates);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystemTemplates);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// System > Layouts Folder
			ShFolder shFolderSystemLayouts = new ShFolder();
			shFolderSystemLayouts.setName("Layouts");
			shFolderSystemLayouts.setParentFolder(shFolderSystem);
			shFolderSystemLayouts.setShSite(shSite);
			shFolderSystemLayouts.setDate(new Date());
			shFolderSystemLayouts.setRootFolder((byte) 0);
			shFolderSystemLayouts.setOwner("admin");
			shFolderSystemLayouts.setFurl(shURLFormatter.format(shFolderSystemLayouts.getName()));
			
			shFolderRepository.save(shFolderSystemLayouts);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystemLayouts);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// System > Themes Folder
			ShFolder shFolderSystemThemes = new ShFolder();
			shFolderSystemThemes.setName("Themes");
			shFolderSystemThemes.setParentFolder(shFolderSystem);
			shFolderSystemThemes.setShSite(shSite);
			shFolderSystemThemes.setDate(new Date());
			shFolderSystemThemes.setRootFolder((byte) 0);
			shFolderSystemThemes.setOwner("admin");
			shFolderSystemThemes.setFurl(shURLFormatter.format(shFolderSystemThemes.getName()));
			
			shFolderRepository.save(shFolderSystemThemes);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystemThemes);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// Home Folder
			ShFolder shFolderHome = new ShFolder();
			shFolderHome.setName("Home");
			shFolderHome.setParentFolder(null);
			shFolderHome.setShSite(shSite);
			shFolderHome.setDate(new Date());
			shFolderHome.setRootFolder((byte) 1);
			shFolderHome.setOwner("admin");
			shFolderHome.setFurl(shURLFormatter.format(shFolderHome.getName()));

			shFolderRepository.save(shFolderHome);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderHome);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// Article Folder
			ShFolder shFolderArticle = new ShFolder();
			shFolderArticle.setName("Article");
			shFolderArticle.setParentFolder(shFolderHome);
			shFolderArticle.setShSite(shSite);
			shFolderArticle.setDate(new Date());
			shFolderArticle.setRootFolder((byte) 0);
			shFolderArticle.setOwner("admin");
			shFolderArticle.setFurl(shURLFormatter.format(shFolderArticle.getName()));
			
			shFolderRepository.save(shFolderArticle);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderArticle);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// Text Folder
			ShFolder shFolderText = new ShFolder();
			shFolderText.setName("Text");
			shFolderText.setParentFolder(shFolderHome);
			shFolderText.setShSite(shSite);
			shFolderText.setDate(new Date());
			shFolderText.setRootFolder((byte) 0);
			shFolderText.setOwner("admin");
			shFolderText.setFurl(shURLFormatter.format(shFolderText.getName()));
			
			shFolderRepository.save(shFolderText);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderText);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

			// News Folder
			ShFolder shFolderNews = new ShFolder();
			shFolderNews.setName("News");
			shFolderNews.setParentFolder(shFolderArticle);
			shFolderNews.setShSite(shSite);
			shFolderNews.setDate(new Date());
			shFolderNews.setRootFolder((byte) 0);
			shFolderNews.setOwner("admin");
			shFolderNews.setFurl(shURLFormatter.format(shFolderNews.getName()));
			
			shFolderRepository.save(shFolderNews);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderNews);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);

		}

	}
}
