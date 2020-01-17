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
package com.viglet.shiohara.api.folder;

import java.io.IOException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.function.Consumer;

import javax.servlet.http.HttpServletResponse;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.spreedsheet.ShSpreadsheet;
import com.viglet.shiohara.turing.ShTuringIntegration;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShHistoryUtils;
import com.viglet.shiohara.utils.ShObjectUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

/**
 * @author Alexandre Oliveira
 */
@RestController
@RequestMapping("/api/v2/folder")
@Api(tags = "Folder", description = "Folder API")
public class ShFolderAPI {
	static final Logger logger = LogManager.getLogger(ShFolderAPI.class.getName());
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShObjectUtils shObjectUtils;
	@Autowired
	private ShTuringIntegration shTuringIntegration;
	@Autowired
	private ShSpreadsheet shSpreadsheet;
	@Autowired
	private ShHistoryUtils shHistoryUtils;
	
	@ApiOperation(value = "Folder list")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShFolder> shFolderList() {
		return shFolderRepository.findAll();
	}

	@ApiOperation(value = "Show a folder")
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ResponseEntity<?> shFolderGet(@PathVariable String id, Principal principal) {
		if (shObjectUtils.canAccess(principal, id)) {
			return new ResponseEntity<>(shFolderRepository.findById(id).orElse(null), HttpStatus.OK);
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);
	}

	@ApiOperation(value = "Update a folder")
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ResponseEntity<?> shFolderUpdate(@PathVariable String id, @RequestBody ShFolder shFolder,
			Principal principal) {
		if (shObjectUtils.canAccess(principal, id)) {
			Optional<ShFolder> shFolderOptional = shFolderRepository.findById(id);
			if (shFolderOptional.isPresent()) {
				ShFolder shFolderEdit = shFolderOptional.get();

				shFolderEdit.setDate(new Date());
				shFolderEdit.setName(shFolder.getName());
				shFolderEdit.setParentFolder(shFolder.getParentFolder());
				shFolderEdit.setShSite(shFolder.getShSite());
				shFolderEdit.setFurl(shURLFormatter.format(shFolderEdit.getName()));
				shFolderRepository.saveAndFlush(shFolderEdit);

				shTuringIntegration.indexObject(shFolderEdit);

				shHistoryUtils.commit(shFolder, principal, ShHistoryUtils.UPDATE);
				
				return new ResponseEntity<>(shFolderEdit, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(null, HttpStatus.FORBIDDEN);

	}

	@Transactional
	@ApiOperation(value = "Delete a folder")
	@DeleteMapping("/{id}")
	public ResponseEntity<?> shFolderDelete(@PathVariable String id, Principal principal) {
		if (shObjectUtils.canAccess(principal, id)) {
			shFolderRepository.findById(id).ifPresent(new Consumer<ShFolder>() {
				@Override
				public void accept(ShFolder shFolder) {
					try {
						shFolderUtils.deleteFolder(shFolder);
						shHistoryUtils.commit(shFolder, principal, ShHistoryUtils.DELETE);
					} catch (IOException e) {
						logger.error("FolderDeleteException", e);
					}
				}
			});
			
			
			return new ResponseEntity<>(true, HttpStatus.OK);
		}
		return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
	}

	@ApiOperation(value = "Create a folder")
	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ResponseEntity<?> shFolderAdd(@RequestBody ShFolder shFolder, Principal principal) {
		ShObject shParentObject = null;
		if (shFolder != null) {
			if (shFolder.getRootFolder() == 1 && shFolder.getShSite() != null && shFolder.getShSite().getId() != null)
				shParentObject = shFolder.getShSite();
			else if (shFolder.getParentFolder() != null && shFolder.getParentFolder().getId() != null)
				shParentObject = shFolder.getParentFolder();

			if (shObjectUtils.canAccess(principal, shParentObject.getId())) {
				List<ShObject> shObjects = new ArrayList<>();
				shObjects.add(shParentObject);

				ShObject shObject = shObjectRepository.findById(shParentObject.getId()).get();

				shFolder.setDate(new Date());
				shFolder.setFurl(shURLFormatter.format(shFolder.getName()));
				shFolder.setShGroups(new HashSet<String>(shObject.getShGroups()));
				shFolder.setShUsers(new HashSet<String>(shObject.getShUsers()));
				shFolderRepository.saveAndFlush(shFolder);

				shTuringIntegration.indexObject(shFolder);

				shHistoryUtils.commit(shFolder, principal, ShHistoryUtils.CREATE);
				
				return new ResponseEntity<>(shFolder, HttpStatus.OK);
			}
		}
		return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);

	}

	@ApiOperation(value = "Create a folder from Parent Object")
	@PostMapping("/object/{objectId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ResponseEntity<?> shFolderAddFromParentObject(@RequestBody ShFolder shFolder, @PathVariable String objectId,
			Principal principal) {
		if (shObjectUtils.canAccess(principal, objectId)) {

			ShObject shObject = shObjectRepository.findById(objectId).get();

			List<ShObject> shObjects = new ArrayList<>();
			shObjects.add(shObject);

			ShFolder shNewFolder = new ShFolder();
			shNewFolder.setDate(new Date());
			shNewFolder.setName(shFolder.getName());
			shNewFolder.setShGroups(new HashSet<String>(shObject.getShGroups()));
			shNewFolder.setShUsers(new HashSet<String>(shObject.getShUsers()));
			shNewFolder.setFurl(shURLFormatter.format(shNewFolder.getName()));

			ShObject shParentObject = shObjectRepository.findById(objectId).orElse(null);
			if (shParentObject instanceof ShFolder) {
				ShFolder shParentFolder = (ShFolder) shParentObject;
				shNewFolder.setParentFolder(shParentFolder);
				shNewFolder.setRootFolder((byte) 0);
				shNewFolder.setShSite(null);

			} else if (shParentObject instanceof ShSite) {
				ShSite shSite = (ShSite) shParentObject;
				shNewFolder.setParentFolder(null);
				shNewFolder.setRootFolder((byte) 1);
				shNewFolder.setShSite(shSite);
			}

			shFolderRepository.save(shNewFolder);

			shHistoryUtils.commit(shNewFolder, principal, ShHistoryUtils.CREATE);
			
			return new ResponseEntity<>(shFolder, HttpStatus.OK);
		}

		return new ResponseEntity<>(false, HttpStatus.FORBIDDEN);
	}

	@ApiOperation(value = "Folder path")
	@GetMapping("/{id}/path")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderPath shFolderPath(@PathVariable String id) {
		ShFolder shFolder = shFolderRepository.findById(id).orElse(null);
		if (shFolder != null) {
			ShFolderPath shFolderPath = new ShFolderPath();
			String folderPath = shFolderUtils.folderPath(shFolder, true, false);
			ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
			ShSite shSite = breadcrumb.get(0).getShSite();
			shFolderPath.setFolderPath(folderPath);
			shFolderPath.setCurrentFolder(shFolder);
			shFolderPath.setBreadcrumb(breadcrumb);
			shFolderPath.setShSite(shSite);
			return shFolderPath;
		} else {
			return null;
		}
	}

	@ApiOperation(value = "Folder model")
	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderStructure() {
		ShFolder shFolder = new ShFolder();
		return shFolder;

	}

	@ApiOperation(value = "Export SpreadSheet from Folder")
	@GetMapping("/{id}/spreadsheet")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public void shFolderSpreadSheet(@PathVariable String id, HttpServletResponse response) {
		ShFolder shFolder = shFolderRepository.findById(id).orElse(null);
		if (shFolder != null)
			shSpreadsheet.generate(shFolder, response);
	}
}
