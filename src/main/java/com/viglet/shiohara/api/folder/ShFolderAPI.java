package com.viglet.shiohara.api.folder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;
import java.util.function.Consumer;

import org.springframework.beans.factory.annotation.Autowired;
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
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/folder")
@Api(tags = "Folder", description = "Folder API")
public class ShFolderAPI {

	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

	@ApiOperation(value = "Folder list")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShFolder> shFolderList() throws Exception {
		return shFolderRepository.findAll();
	}

	@ApiOperation(value = "Show a folder")
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderGet(@PathVariable UUID id) throws Exception {
		return shFolderRepository.findById(id).get();
	}

	@ApiOperation(value = "Update a folder")
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderUpdate(@PathVariable UUID id, @RequestBody ShFolder shFolder) throws Exception {

		ShFolder shFolderEdit = shFolderRepository.findById(id).get();

		shFolderEdit.setDate(new Date());
		shFolderEdit.setName(shFolder.getName());
		shFolderEdit.setParentFolder(shFolder.getParentFolder());
		shFolderEdit.setShSite(shFolder.getShSite());

		shFolderRepository.saveAndFlush(shFolderEdit);

		return shFolderEdit;
	}

	@Transactional
	@ApiOperation(value = "Delete a folder")
	@DeleteMapping("/{id}")
	public boolean shFolderDelete(@PathVariable UUID id) throws Exception {
		shFolderRepository.findById(id).ifPresent(new Consumer<ShFolder>() {
			@Override
			public void accept(ShFolder shFolder) {
				shGlobalIdRepository.delete(shFolder.getShGlobalId().getId());
				shFolderUtils.deleteFolder(shFolder);
			}
		});
		return true;
	}

	@ApiOperation(value = "Create a folder")
	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderAdd(@RequestBody ShFolder shFolder) throws Exception {
		shFolder.setDate(new Date());
		shFolderRepository.save(shFolder);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shFolder);
		shGlobalId.setType(ShObjectType.FOLDER);

		shGlobalIdRepository.save(shGlobalId);

		return shFolder;

	}

	@ApiOperation(value = "Create a folder from Parent Object")
	@PostMapping("/object/{objectId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderAddFromParentObject(@RequestBody ShFolder shFolder, @PathVariable UUID objectId)
			throws Exception {

		ShFolder shNewFolder = new ShFolder();
		shNewFolder.setDate(new Date());
		shNewFolder.setName(shFolder.getName());

		ShGlobalId shParentGlobalId = shGlobalIdRepository.findById(objectId).get();
		if (shParentGlobalId.getType().equals(ShObjectType.FOLDER)) {
			ShFolder shParentFolder = (ShFolder) shParentGlobalId.getShObject();
			shNewFolder.setParentFolder(shParentFolder);
			shNewFolder.setRootFolder((byte) 0);
			shNewFolder.setShSite(null);

		} else if (shParentGlobalId.getType().equals(ShObjectType.SITE)) {
			ShSite shSite = (ShSite) shParentGlobalId.getShObject();
			shNewFolder.setParentFolder(null);
			shNewFolder.setRootFolder((byte) 1);
			shNewFolder.setShSite(shSite);
		}

		shFolderRepository.save(shNewFolder);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shNewFolder);
		shGlobalId.setType(ShObjectType.FOLDER);

		shGlobalIdRepository.save(shGlobalId);

		return shFolder;

	}

	@ApiOperation(value = "Folder path")
	@GetMapping("/{id}/path")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderPath shFolderPath(@PathVariable UUID id) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id).get();
		if (shFolder != null) {
			ShFolderPath shFolderPath = new ShFolderPath();
			String folderPath = shFolderUtils.folderPath(shFolder);
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
	public ShFolder shFolderStructure() throws Exception {
		ShFolder shFolder = new ShFolder();
		return shFolder;

	}

}
