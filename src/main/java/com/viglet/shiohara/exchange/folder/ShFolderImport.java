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
package com.viglet.shiohara.exchange.folder;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.exchange.ShFolderExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.exchange.post.ShPostImport;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.url.ShURLFormatter;

/**
 * @author Alexandre Oliveira
 */
@Component
public class ShFolderImport {
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShPostImport shPostImport;

	public void shFolderImportNested(String shObject, File extractFolder, String username, boolean importOnlyFolders,
			Map<String, Object> shObjects, Map<String, List<String>> shChildObjects) throws IOException {
		if (shChildObjects.containsKey(shObject)) {
			for (String objectId : shChildObjects.get(shObject)) {
				if (shObjects.get(objectId) instanceof ShFolderExchange) {
					ShFolderExchange shFolderExchange = (ShFolderExchange) shObjects.get(objectId);
					this.createShFolder(shFolderExchange, extractFolder, username, shObject, importOnlyFolders,
							shObjects, shChildObjects);
				}

				if (!importOnlyFolders && shObjects.get(objectId) instanceof ShPostExchange) {
					ShPostExchange shPostExchange = (ShPostExchange) shObjects.get(objectId);
					shPostImport.createShPost(shPostExchange, extractFolder, username, shObjects);
				}
			}

		}
	}

	public ShFolder createShFolder(ShFolderExchange shFolderExchange, File extractFolder, String username,
			String shObject, boolean importOnlyFolders, Map<String, Object> shObjects,
			Map<String, List<String>> shChildObjects) throws IOException {
		ShFolder shFolderChild = null;
		Optional<ShFolder> shFolderOptional = shFolderRepository.findById(shFolderExchange.getId());
		if (shFolderOptional.isPresent()) {
			shFolderChild = shFolderOptional.get();
		} else {
			shFolderChild = new ShFolder();
			shFolderChild.setId(shFolderExchange.getId());
			shFolderChild.setDate(shFolderExchange.getDate());
			shFolderChild.setName(shFolderExchange.getName());
			if (shFolderExchange.getPosition() > 0) {
				shFolderChild.setPosition(shFolderExchange.getPosition());
			}
			if (shFolderExchange.getOwner() != null) {
				shFolderChild.setOwner(shFolderExchange.getOwner());
			} else {
				shFolderChild.setOwner(username);
			}
			if (shFolderExchange.getFurl() != null) {
				shFolderChild.setFurl(shFolderExchange.getFurl());
			} else {
				shFolderChild.setFurl(shURLFormatter.format(shFolderExchange.getName()));
			}
			if (shFolderExchange.getParentFolder() != null) {
				ShFolder parentFolder = shFolderRepository.findById(shFolderExchange.getParentFolder()).orElse(null);
				shFolderChild.setParentFolder(parentFolder);
				shFolderChild.setRootFolder((byte) 0);
			} else {
				if (shObjects.get(shObject) instanceof ShSiteExchange) {
					ShSiteExchange shSiteExchange = (ShSiteExchange) shObjects.get(shObject);
					if (shSiteExchange.getRootFolders().contains(shFolderExchange.getId())) {
						shFolderChild.setRootFolder((byte) 1);
						ShSite parentSite = shSiteRepository.findById(shSiteExchange.getId()).orElse(null);
						shFolderChild.setShSite(parentSite);
					}
				}
			}
			shFolderRepository.save(shFolderChild);
		}

		this.shFolderImportNested(shFolderChild.getId(), extractFolder, username, importOnlyFolders, shObjects,
				shChildObjects);

		return shFolderChild;
	}
}
