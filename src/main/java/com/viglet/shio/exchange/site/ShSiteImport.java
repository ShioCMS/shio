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
package com.viglet.shio.exchange.site;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;
import java.util.Optional;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shio.exchange.ShExchange;
import com.viglet.shio.exchange.ShFolderExchange;
import com.viglet.shio.exchange.ShPostExchange;
import com.viglet.shio.exchange.ShSiteExchange;
import com.viglet.shio.exchange.folder.ShFolderImport;
import com.viglet.shio.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shio.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;
import com.viglet.shio.post.type.ShSystemPostType;
import com.viglet.shio.url.ShURLFormatter;
import com.viglet.shio.widget.ShSystemWidget;

/**
 * Import Site.
 *
 * @author Alexandre Oliveira
 * @since 0.3.0
 */
@Component
public class ShSiteImport {
	private static final Log logger = LogFactory.getLog(ShSiteImport.class);
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShFolderImport shFolderImport;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	public void importSite(ShExchange shExchange, String username, File extractFolder, Map<String, Object> shObjects,
			Map<String, List<String>> shChildObjects) throws IOException {
		for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
			this.prepareImport(shExchange, shSiteExchange, shObjects, shChildObjects);
			this.createShSite(shSiteExchange, username);
			// Create only Folders
			shFolderImport.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, true, shObjects,
					shChildObjects, false);
			// Create all objects
			shFolderImport.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, false, shObjects,
					shChildObjects, false);
		}
	}

	public ShExchange cloneSite(ShExchange shExchange, String username, File extractFolder,
			Map<String, Object> shObjects, Map<String, List<String>> shChildObjects, ShSite shSite) throws IOException {
		shExchange = this.prepareClone(shExchange, extractFolder);

		for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
			shSiteExchange.setDate(new Date());

			if (shSite != null) {
				if (shSite.getId() != null && shSite.getId().trim().length() > 0) {
					shSiteExchange.setId(shSite.getId());
				}
				shSiteExchange.setOwner(shSite.getOwner());
				shSiteExchange.setFurl(shSite.getFurl());
				shSiteExchange.setName(shSite.getName());
				shSiteExchange.setDescription(shSite.getDescription());
				shSiteExchange.setUrl(shSite.getUrl());
			}

			this.prepareImport(shExchange, shSiteExchange, shObjects, shChildObjects);

			this.createShSite(shSiteExchange, username);
			// Create only Folders
			shFolderImport.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, true, shObjects,
					shChildObjects, true);
			// Create all objects
			shFolderImport.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, false, shObjects,
					shChildObjects, true);
		}

		return shExchange;
	}

	public ShSite createShSite(ShSiteExchange shSiteExchange, String username) {
		ShSite shSite = null;
		Optional<ShSite> shSiteOptional = shSiteRepository.findById(shSiteExchange.getId());
		if (shSiteOptional.isPresent()) {
			shSite = shSiteOptional.get();
		} else {
			shSite = new ShSite();
			shSite.setId(shSiteExchange.getId());
			shSite.setName(shSiteExchange.getName());
			shSite.setUrl(shSiteExchange.getUrl());
			shSite.setDescription(shSiteExchange.getDescription());
			shSite.setPostTypeLayout(shSiteExchange.getPostTypeLayout());
			shSite.setSearchablePostTypes(shSiteExchange.getSearchablePostTypes());
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

		prepareSiteImport(shSiteExchange, shObjects);
		prepareFolderImport(shExchange, shSiteExchange, shObjects, shChildObjects, rootFolders);
		preparePostImport(shExchange, shObjects, shChildObjects);
	}

	private void prepareSiteImport(ShSiteExchange shSiteExchange, Map<String, Object> shObjects) {
		shObjects.put(shSiteExchange.getId(), shSiteExchange);
	}

	private void prepareFolderImport(ShExchange shExchange, ShSiteExchange shSiteExchange,
			Map<String, Object> shObjects, Map<String, List<String>> shChildObjects, List<String> rootFolders) {
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
	}

	private void preparePostImport(ShExchange shExchange, Map<String, Object> shObjects,
			Map<String, List<String>> shChildObjects) {
		if (shExchange.getPosts() != null) {
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

	public ShExchange prepareClone(ShExchange shExchange, File extractFolder) {
		ShExchange shExchangeWithNewIds = new ShExchange();

		Map<String, String> shNewIds = new HashMap<String, String>();
		Map<String, String> shNewIdsReverse = new HashMap<String, String>();
		List<ShSiteExchange> shSiteExchangeWithNewIds = new ArrayList<ShSiteExchange>();
		for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
			List<String> rootFolders = new ArrayList<String>();
			for (String rootFolderId : shSiteExchange.getRootFolders()) {
				if (!shNewIds.containsKey(rootFolderId)) {
					String newUUID = UUID.randomUUID().toString();
					shNewIds.put(rootFolderId, newUUID);
					shNewIdsReverse.put(newUUID, rootFolderId);
				}
				rootFolders.add(shNewIds.get(rootFolderId));
			}
			shSiteExchange.setRootFolders(rootFolders);

			if (!shNewIds.containsKey(shSiteExchange.getId())) {
				String newUUID = UUID.randomUUID().toString();
				shNewIds.put(shSiteExchange.getId(), newUUID);
				shNewIdsReverse.put(newUUID, shSiteExchange.getId());
			}

			shSiteExchange.setId(shNewIds.get(shSiteExchange.getId()));

			shSiteExchangeWithNewIds.add(shSiteExchange);
		}

		List<ShFolderExchange> shFolderExchangeWithNewIds = new ArrayList<ShFolderExchange>();
		for (ShFolderExchange shFolderExchange : shExchange.getFolders()) {
			if (!shNewIds.containsKey(shFolderExchange.getId())) {
				String newUUID = UUID.randomUUID().toString();
				shNewIds.put(shFolderExchange.getId(), newUUID);
				shNewIdsReverse.put(newUUID, shFolderExchange.getId());
			}
			shFolderExchange.setId(shNewIds.get(shFolderExchange.getId()));

			if (shFolderExchange.getParentFolder() != null) {
				if (!shNewIds.containsKey(shFolderExchange.getParentFolder())) {
					String newUUID = UUID.randomUUID().toString();
					shNewIds.put(shFolderExchange.getParentFolder(), newUUID);
					shNewIdsReverse.put(newUUID, shFolderExchange.getParentFolder());
				}
				shFolderExchange.setParentFolder(shNewIds.get(shFolderExchange.getParentFolder()));
			}
			shFolderExchangeWithNewIds.add(shFolderExchange);
		}
		shExchange.setFolders(shFolderExchangeWithNewIds);

		List<ShPostExchange> shPostExchangeWithNewIds = new ArrayList<ShPostExchange>();
		for (ShPostExchange shPostExchange : shExchange.getPosts()) {
			if (!shNewIds.containsKey(shPostExchange.getId())) {
				String newUUID = UUID.randomUUID().toString();
				shNewIds.put(shPostExchange.getId(), newUUID);
				shNewIdsReverse.put(newUUID, shPostExchange.getId());
			}
			if (!shNewIds.containsKey(shPostExchange.getFolder())) {
				String newUUID = UUID.randomUUID().toString();
				shNewIds.put(shPostExchange.getFolder(), newUUID);
				shNewIdsReverse.put(newUUID, shPostExchange.getFolder());
			}

			shPostExchange.setId(shNewIds.get(shPostExchange.getId()));
			shPostExchange.setFolder(shNewIds.get(shPostExchange.getFolder()));

			shPostExchange.setFields(this.updateFieldRelation(shNewIds, shNewIdsReverse, shPostExchange,
					shPostExchange.getFields(), extractFolder));
			shPostExchangeWithNewIds.add(shPostExchange);

		}

		shExchange.setPosts(shPostExchangeWithNewIds);
		shExchangeWithNewIds.setFiles(shExchange.getFiles());
		shExchangeWithNewIds.setFolders(shFolderExchangeWithNewIds);
		shExchangeWithNewIds.setPosts(shPostExchangeWithNewIds);
		shExchangeWithNewIds.setSites(shSiteExchangeWithNewIds);
		return shExchangeWithNewIds;
	}

	@SuppressWarnings("unchecked")
	private Map<String, Object> updateFieldRelation(Map<String, String> shNewIds, Map<String, String> shNewIdsReverse,
			ShPostExchange shPostExchange, Map<String, Object> shPostFields, File extractFolder) {
		Map<String, Object> fieldsWithNewIds = new HashMap<String, Object>();
		for (Entry<String, Object> shPostField : shPostFields.entrySet()) {

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(
					shPostTypeRepository.findByName(shPostExchange.getPostType()), shPostField.getKey());
			if (shPostTypeAttr != null) {
				if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.RELATOR)) {
					LinkedHashMap<String, Object> relatorFields = (LinkedHashMap<String, Object>) shPostField
							.getValue();
					for (Object shSubPost : (ArrayList<Object>) relatorFields.get("shSubPosts")) {
						shPostField.setValue(this.updateFieldRelation(shNewIds, shNewIdsReverse, shPostExchange,
								(Map<String, Object>) shSubPost, extractFolder));
					}
				} else if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)
						&& shPostExchange.getPostType().equals(ShSystemPostType.FILE)) {
					File fileSource = new File(extractFolder.getAbsolutePath()
							.concat(File.separator + shNewIdsReverse.get(shPostExchange.getId())));
					File fileDest = new File(
							extractFolder.getAbsolutePath().concat(File.separator + shPostExchange.getId()));
					try {
						FileUtils.moveFile(fileSource, fileDest);
					} catch (IOException e) {
						logger.error("updateFieldRelationException", e);
					}

				} else if ((shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)
						&& !shPostExchange.getPostType().equals(ShSystemPostType.FILE))
						|| shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT)) {

					if (shPostField.getValue() != null && shPostField.getKey().trim().length() > 0) {
						if (!shNewIds.containsKey(shPostField.getValue())) {
							String newUUID = UUID.randomUUID().toString();
							shNewIds.put((String) shPostField.getValue(), newUUID);
							shNewIdsReverse.put(newUUID, (String) shPostField.getValue());
						}
						shPostField.setValue(shNewIds.get(shPostField.getValue()));
					}

				}
				fieldsWithNewIds.put(shPostField.getKey(), shPostField.getValue());
			}
		}

		return fieldsWithNewIds;
	}
}
