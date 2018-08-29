package com.viglet.shiohara.api.folder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
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
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.turing.ShTuringIntegration;
import com.viglet.shiohara.url.ShURLFormatter;
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
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShTuringIntegration shTuringIntegration;
	
	private boolean turingEnabled = true;
	
	@ApiOperation(value = "Folder list")
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShFolder> shFolderList() throws Exception {
		return shFolderRepository.findAll();
	}

	@ApiOperation(value = "Show a folder")
	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderGet(@PathVariable String id) throws Exception {
		return shFolderRepository.findById(id).get();
	}

	@ApiOperation(value = "Update a folder")
	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderUpdate(@PathVariable String id, @RequestBody ShFolder shFolder) throws Exception {

		ShFolder shFolderEdit = shFolderRepository.findById(id).get();

		shFolderEdit.setDate(new Date());
		shFolderEdit.setName(shFolder.getName());
		shFolderEdit.setParentFolder(shFolder.getParentFolder());
		shFolderEdit.setShSite(shFolder.getShSite());
		shFolderEdit.setFurl(shURLFormatter.format(shFolderEdit.getName()));

		shFolderRepository.saveAndFlush(shFolderEdit);
		if (turingEnabled) {
			shTuringIntegration.prepareFolder(shFolderEdit);
		}
		return shFolderEdit;
	}

	@Transactional
	@ApiOperation(value = "Delete a folder")
	@DeleteMapping("/{id}")
	public boolean shFolderDelete(@PathVariable String id) throws Exception {
		shFolderRepository.findById(id).ifPresent(new Consumer<ShFolder>() {
			@Override
			public void accept(ShFolder shFolder) {
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
		shFolder.setFurl(shURLFormatter.format(shFolder.getName()));
		shFolderRepository.save(shFolder);
		
		if (turingEnabled) {
			shTuringIntegration.prepareFolder(shFolder);
		}
		return shFolder;

	}

	@ApiOperation(value = "Create a folder from Parent Object")
	@PostMapping("/object/{objectId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder shFolderAddFromParentObject(@RequestBody ShFolder shFolder, @PathVariable String objectId)
			throws Exception {

		ShFolder shNewFolder = new ShFolder();
		shNewFolder.setDate(new Date());
		shNewFolder.setName(shFolder.getName());
		shNewFolder.setFurl(shURLFormatter.format(shNewFolder.getName()));

		ShObject shParentObject = shObjectRepository.findById(objectId).get();
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

		return shFolder;
	}

	@ApiOperation(value = "Folder path")
	@GetMapping("/{id}/path")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderPath shFolderPath(@PathVariable String id) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id).get();
		if (shFolder != null) {
			ShFolderPath shFolderPath = new ShFolderPath();
			String folderPath = shFolderUtils.folderPath(shFolder, true);
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
