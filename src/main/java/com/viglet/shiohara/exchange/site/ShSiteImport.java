package com.viglet.shiohara.exchange.site;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFolderExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.exchange.folder.ShFolderImport;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.url.ShURLFormatter;

@Component
public class ShSiteImport {
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShFolderImport shFolderImport;

	public void importSite(ShExchange shExchange, String username, File extractFolder, Map<String, Object> shObjects,
			Map<String, List<String>> shChildObjects) throws IOException {
		for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
			this.prepareImport(shExchange, shSiteExchange, shObjects, shChildObjects);
			this.createShSite(shSiteExchange, username);
			// Create only Folders
			shFolderImport.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, true, shObjects,
					shChildObjects);
			// Create all objects
			shFolderImport.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, false, shObjects,
					shChildObjects);
		}
	}

	public ShSite createShSite(ShSiteExchange shSiteExchange, String username) {
		ShSite shSite = null;

		if (shSiteRepository.findById(shSiteExchange.getId()).isPresent()) {
			shSite = shSiteRepository.findById(shSiteExchange.getId()).get();
		} else {
			shSite = new ShSite();
			shSite.setId(shSiteExchange.getId());
			shSite.setName(shSiteExchange.getName());
			shSite.setUrl(shSiteExchange.getUrl());
			shSite.setDescription(shSiteExchange.getDescription());
			shSite.setPostTypeLayout(shSiteExchange.getPostTypeLayout());
			if (shSiteExchange.getOwner() != null) {
				shSite.setOwner(shSiteExchange.getOwner());
			} else {
				shSite.setOwner(username);
			}
			if (shSiteExchange.getFurl() != null) {
				shSite.setFurl(shSiteExchange.getFurl());
			} else {
				shSite.setFurl(shURLFormatter.format(shSiteExchange.getName()));
			}
			shSite.setDate(shSiteExchange.getDate());
			shSiteRepository.save(shSite);

		}

		return shSite;
	}

	public void prepareImport(ShExchange shExchange, ShSiteExchange shSiteExchange, Map<String, Object> shObjects,
			Map<String, List<String>> shChildObjects) {
		List<String> rootFolders = shSiteExchange.getRootFolders();

		shObjects.put(shSiteExchange.getId(), shSiteExchange);
		for (ShFolderExchange shFolderExchange : shExchange.getFolders()) {

			shObjects.put(shFolderExchange.getId(), shFolderExchange);
			if (shFolderExchange.getParentFolder() != null) {
				if (shChildObjects.containsKey(shFolderExchange.getParentFolder())) {
					shChildObjects.get(shFolderExchange.getParentFolder()).add(shFolderExchange.getId());
				} else {
					List<String> childFolderList = new ArrayList<String>();
					childFolderList.add(shFolderExchange.getId());
					shChildObjects.put(shFolderExchange.getParentFolder(), childFolderList);
				}
			} else {
				if (rootFolders.contains(shFolderExchange.getId())) {
					if (shChildObjects.containsKey(shSiteExchange.getId())) {
						shChildObjects.get(shSiteExchange.getId()).add(shFolderExchange.getId());
					} else {
						List<String> childFolderList = new ArrayList<String>();
						childFolderList.add(shFolderExchange.getId());
						shChildObjects.put(shSiteExchange.getId(), childFolderList);
					}
				}

			}
		}

		for (ShPostExchange shPostExchange : shExchange.getPosts()) {

			shObjects.put(shPostExchange.getId(), shPostExchange);
			if (shPostExchange.getFolder() != null) {
				if (shChildObjects.containsKey(shPostExchange.getFolder())) {
					shChildObjects.get(shPostExchange.getFolder()).add(shPostExchange.getId());
				} else {
					List<String> childObjectList = new ArrayList<String>();
					childObjectList.add(shPostExchange.getId());
					shChildObjects.put(shPostExchange.getFolder(), childObjectList);
				}
			}
		}
	}

}
