package com.viglet.shiohara.api.folder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
@RestController
@RequestMapping("/api/v2/folder")
public class ShFolderAPI {

	@Autowired
	ShFolderRepository shFolderRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShFolderUtils shFolderUtils;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

	@RequestMapping(method = RequestMethod.GET)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShFolder> list() throws Exception {
		return shFolderRepository.findAll();
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder edit(@PathVariable UUID id) throws Exception {
		return shFolderRepository.findById(id);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder update(@PathVariable UUID id, @RequestBody ShFolder shFolder) throws Exception {

		ShFolder shFolderEdit = shFolderRepository.findById(id);

		shFolderEdit.setDate(new Date());
		shFolderEdit.setName(shFolder.getName());
		shFolderEdit.setParentFolder(shFolder.getParentFolder());
		shFolderEdit.setShSite(shFolder.getShSite());

		shFolderRepository.saveAndFlush(shFolderEdit);

		return shFolderEdit;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public boolean delete(@PathVariable UUID id) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id);
		shGlobalIdRepository.delete(shFolder.getShGlobalId().getId());
		return shFolderUtils.deleteFolder(shFolder);
	}

	@RequestMapping(method = RequestMethod.POST)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder add(@RequestBody ShFolder shFolder) throws Exception {
		shFolder.setDate(new Date());
		shFolderRepository.save(shFolder);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shFolder);
		shGlobalId.setType("FOLDER");

		shGlobalIdRepository.save(shGlobalId);

		return shFolder;

	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList list(@PathVariable UUID id) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id);

		String folderPath = shFolderUtils.folderPath(shFolder);
		ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
		ShSite shSite = breadcrumb.get(0).getShSite();
		ShFolderList shFolderList = new ShFolderList();
		shFolderList.setShFolders(shFolderRepository.findByParentFolder(shFolder));
		shFolderList.setShPosts(shPostRepository.findByShFolder(shFolder));
		shFolderList.setFolderPath(folderPath);
		shFolderList.setBreadcrumb(breadcrumb);
		shFolderList.setShSite(shSite);
		return shFolderList;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/list/{postTypeName}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList listByPostType(@PathVariable UUID id, @PathVariable String postTypeName) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id);
		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		String folderPath = shFolderUtils.folderPath(shFolder);
		ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
		ShSite shSite = breadcrumb.get(0).getShSite();
		ShFolderList shFolderList = new ShFolderList();
		shFolderList.setShFolders(shFolderRepository.findByParentFolder(shFolder));
		shFolderList.setShPosts(shPostRepository.findByShFolderAndShPostType(shFolder, shPostType));
		shFolderList.setFolderPath(folderPath);
		shFolderList.setBreadcrumb(breadcrumb);
		shFolderList.setShSite(shSite);
		return shFolderList;
	}

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/path")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderPath path(@PathVariable UUID id) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id);
		if (shFolder != null) {
			ShFolderPath shFolderPath = new ShFolderPath();
			String folderPath = shFolderUtils.folderPath(shFolder);
			ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
			ShSite shSite = breadcrumb.get(0).getShSite();
			shFolderPath.setFolderPath(folderPath);
			shFolderPath.setCurrentFolder(shFolderUtils.folderFromPath(shSite, folderPath));
			shFolderPath.setBreadcrumb(breadcrumb);
			shFolderPath.setShSite(shSite);
			return shFolderPath;
		} else {
			return null;
		}
	}

	@RequestMapping(method = RequestMethod.GET, value = "/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolder folderStructure() throws Exception {
		ShFolder shFolder = new ShFolder();
		return shFolder;

	}

}
