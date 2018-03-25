package com.viglet.shiohara.onstartup.folder;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;

@Component
public class ShFolderOnStartup {

	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

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

			shFolderRepository.save(shFolderSystem);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystem);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// System > Templates Folder
			ShFolder shFolderSystemTemplates = new ShFolder();
			shFolderSystemTemplates.setName("Templates");
			shFolderSystemTemplates.setParentFolder(shFolderSystem);
			shFolderSystemTemplates.setShSite(shSite);
			shFolderSystemTemplates.setDate(new Date());
			shFolderSystemTemplates.setRootFolder((byte) 0);

			shFolderRepository.save(shFolderSystemTemplates);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystemTemplates);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// System > Layouts Folder
			ShFolder shFolderSystemLayouts = new ShFolder();
			shFolderSystemLayouts.setName("Layouts");
			shFolderSystemLayouts.setParentFolder(shFolderSystem);
			shFolderSystemLayouts.setShSite(shSite);
			shFolderSystemLayouts.setDate(new Date());
			shFolderSystemLayouts.setRootFolder((byte) 0);

			shFolderRepository.save(shFolderSystemLayouts);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystemLayouts);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// System > Themes Folder
			ShFolder shFolderSystemThemes = new ShFolder();
			shFolderSystemThemes.setName("Themes");
			shFolderSystemThemes.setParentFolder(shFolderSystem);
			shFolderSystemThemes.setShSite(shSite);
			shFolderSystemThemes.setDate(new Date());
			shFolderSystemThemes.setRootFolder((byte) 0);

			shFolderRepository.save(shFolderSystemThemes);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderSystemThemes);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// Home Folder
			ShFolder shFolderHome = new ShFolder();
			shFolderHome.setName("Home");
			shFolderHome.setParentFolder(null);
			shFolderHome.setShSite(shSite);
			shFolderHome.setDate(new Date());
			shFolderHome.setRootFolder((byte) 1);

			shFolderRepository.save(shFolderHome);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderHome);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// Article Folder
			ShFolder shFolderArticle = new ShFolder();
			shFolderArticle.setName("Article");
			shFolderArticle.setParentFolder(shFolderHome);
			shFolderArticle.setShSite(shSite);
			shFolderArticle.setDate(new Date());
			shFolderArticle.setRootFolder((byte) 0);

			shFolderRepository.save(shFolderArticle);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderArticle);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// Text Folder
			ShFolder shFolderText = new ShFolder();
			shFolderText.setName("Text");
			shFolderText.setParentFolder(shFolderHome);
			shFolderText.setShSite(shSite);
			shFolderText.setDate(new Date());
			shFolderText.setRootFolder((byte) 0);

			shFolderRepository.save(shFolderText);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderText);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

			// News Folder
			ShFolder shFolderNews = new ShFolder();
			shFolderNews.setName("News");
			shFolderNews.setParentFolder(shFolderArticle);
			shFolderNews.setShSite(shSite);
			shFolderNews.setDate(new Date());
			shFolderNews.setRootFolder((byte) 0);

			shFolderRepository.save(shFolderNews);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shFolderNews);
			shGlobalId.setType("CHANNEL");

			shGlobalIdRepository.save(shGlobalId);

		}

	}
}
