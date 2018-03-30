package com.viglet.shiohara.api.folder;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShFolderUtils;

@Component
@Path("/folder")
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})	
	public List<ShFolder> list() throws Exception {
		return shFolderRepository.findAll();
	}

	@Path("/{folderId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})
	public ShFolder edit(@PathParam("folderId") UUID id) throws Exception {
		return shFolderRepository.findById(id);
	}

	@Path("/{folderId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})
	public ShFolder update(@PathParam("folderId") UUID id, ShFolder shFolder) throws Exception {

		ShFolder shFolderEdit = shFolderRepository.findById(id);

		shFolderEdit.setDate(new Date());
		shFolderEdit.setName(shFolder.getName());
		shFolderEdit.setParentFolder(shFolder.getParentFolder());
		shFolderEdit.setShSite(shFolder.getShSite());

		shFolderRepository.saveAndFlush(shFolderEdit);

		return shFolderEdit;
	}

	@Path("/{folderId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("folderId") UUID id) throws Exception {
		ShFolder shFolder = shFolderRepository.findById(id);
		shGlobalIdRepository.delete(shFolder.getShGlobalId().getId());
		return shFolderUtils.deleteFolder(shFolder);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})
	public ShFolder add(ShFolder shFolder) throws Exception {
		shFolder.setDate(new Date());
		shFolderRepository.save(shFolder);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shFolder);
		shGlobalId.setType("FOLDER");

		shGlobalIdRepository.save(shGlobalId);
		
		return shFolder;

	}

	@Path("/{folderId}/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})	
	public ShFolderList list(@PathParam("folderId") UUID id) throws Exception {
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

	@Path("/{folderId}/list/{postTypeName}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})	
	public ShFolderList listByPostType(@PathParam("folderId") UUID id, @PathParam("postTypeName") String postTypeName)
			throws Exception {
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

	@Path("/{folderId}/path")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})	
	public ShFolderPath path(@PathParam("folderId") UUID id) throws Exception {
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

	@Path("/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ShJsonView.ShJsonViewObject.class})	
	public ShFolder folderStructure() throws Exception {
		ShFolder shFolder = new ShFolder();
		return shFolder;

	}

}
