package com.viglet.shiohara.api.site;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.WebApplicationException;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import javax.ws.rs.core.StreamingOutput;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.api.folder.ShFolderList;
import com.viglet.shiohara.exchange.ShFolderExchange;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
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
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;
import com.viglet.shiohara.utils.ShUtils;

@Component
@Path("/site")
public class ShSiteAPI {

	@Autowired
	ShSiteRepository shSiteRepository;
	@Autowired
	ShFolderRepository shFolderRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShFolderUtils shFolderUtils;
	@Autowired
	ShStaticFileUtils shStaticFileUtils;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	ShUtils shUtils;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShSite> list() throws Exception {
		return shSiteRepository.findAll();
	}

	@Path("/{siteId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite edit(@PathParam("siteId") UUID id) throws Exception {
		return shSiteRepository.findById(id);
	}

	@Path("/{siteId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite update(@PathParam("siteId") UUID id, ShSite shSite) throws Exception {
		ShSite shSiteEdit = shSiteRepository.findById(id);
		shSiteEdit.setDate(new Date());
		shSiteEdit.setName(shSite.getName());
		shSiteEdit.setPostTypeLayout(shSite.getPostTypeLayout());
		shSiteRepository.save(shSiteEdit);
		return shSiteEdit;
	}

	@Path("/{siteId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("siteId") UUID id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id);

		List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);
		
		for (ShFolder shFolder : shFolders) {
			shFolderUtils.deleteFolder(shFolder);
		}

		shGlobalIdRepository.delete(shSite.getShGlobalId().getId());
		shSiteRepository.delete(id);

		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite add(ShSite shSite) throws Exception {
		shSite.setDate(new Date());

		shSiteRepository.save(shSite);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shSite);
		shGlobalId.setType("SITE");

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
		shGlobalId.setType("FOLDER");

		shGlobalIdRepository.save(shGlobalId);

		// Folder Index

		ShPostType shPostFolderIndex = shPostTypeRepository.findByName("PT-FOLDER-INDEX");

		ShPost shPost = new ShPost();
		shPost.setDate(new Date());
		shPost.setShPostType(shPostFolderIndex);
		shPost.setSummary("Folder Index");
		shPost.setTitle("index");
		shPost.setShFolder(shFolderHome);

		shPostRepository.save(shPost);

		shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPost);
		shGlobalId.setType("POST");

		shGlobalIdRepository.save(shGlobalId);

		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostFolderIndex, "TITLE");

		ShPostAttr shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getTitle());
		shPostAttr.setType(1);
		shPostAttrRepository.save(shPostAttr);

		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostFolderIndex, "DESCRIPTION");

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getSummary());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);
		return shSite;

	}

	@Path("/{siteId}/folder")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList rootFolder(@PathParam("siteId") UUID id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id);
		List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);
		ShFolderList shFolderList = new ShFolderList();
		shFolderList.setShFolders(shFolders);
		shFolderList.setShSite(shSite);
		return shFolderList;

	}

	@Path("/{siteId}/export")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public Response siteExport(@PathParam("siteId") UUID id) throws Exception {
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			ShExchange shExchange = new ShExchange();
			ShSite shSite = shSiteRepository.findById(id);

			List<ShFolder> rootFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);

			List<UUID> rootFoldersUUID = new ArrayList<UUID>();
			for (ShFolder shFolder : rootFolders) {
				rootFoldersUUID.add(shFolder.getId());
			}

			ShSiteExchange shSiteExchange = new ShSiteExchange();
			shSiteExchange.setId(shSite.getId());
			shSiteExchange.setName(shSite.getName());
			shSiteExchange.setUrl(shSite.getUrl());
			shSiteExchange.setDescription(shSite.getDescription());
			shSiteExchange.setPostTypeLayout(shSite.getPostTypeLayout());
			shSiteExchange.setDate(shSite.getDate());
			shSiteExchange.setRootFolders(rootFoldersUUID);
			shSiteExchange.setGlobalId(shSite.getShGlobalId().getId());

			List<ShSiteExchange> shSiteExchanges = new ArrayList<ShSiteExchange>();
			shSiteExchanges.add(shSiteExchange);
			shExchange.setSites(shSiteExchanges);

			ShExchange shExchangeFolder = this.shFolderExchangeIterate(rootFolders);

			shExchange.setFolders(shExchangeFolder.getFolders());
			shExchange.setPosts(shExchangeFolder.getPosts());
			File exportDir = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName));
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}

			for (ShFileExchange fileExchange : shExchangeFolder.getFiles()) {
				FileUtils.copyFile(fileExchange.getFile(),
						new File(exportDir.getAbsolutePath().concat(File.separator + fileExchange.getGlobalId())));
			}
			// Object to JSON in file
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(
					new File(exportDir.getAbsolutePath().concat(File.separator + "export.json")), shExchange);

			File zipFile = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName + ".zip"));

			shUtils.addFilesToZip(exportDir, zipFile);

			StreamingOutput fileStream = new StreamingOutput() {
				@Override
				public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
					try {
						java.nio.file.Path path = Paths.get(zipFile.getAbsolutePath());
						byte[] data = Files.readAllBytes(path);
						output.write(data);
						output.flush();

						FileUtils.deleteDirectory(exportDir);
						FileUtils.deleteQuietly(zipFile);

					} catch (IOException ex) {
						ex.printStackTrace();
					} catch (Exception e) {
						throw new WebApplicationException("File Not Found");
					}
				}
			};

			return Response.ok(fileStream, MediaType.APPLICATION_OCTET_STREAM)
					.header("content-disposition", "attachment; filename = " + folderName + ".zip").build();
		} else {
			return null;
		}

	}

	@Path("/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite siteStructure() throws Exception {
		ShSite shSite = new ShSite();
		return shSite;

	}

	public ShExchange shFolderExchangeNested(ShFolder shFolder) {
		List<ShFolder> childFolders = shFolderRepository.findByParentFolder(shFolder);
		return this.shFolderExchangeIterate(childFolders);
	}

	public ShExchange shFolderExchangeIterate(List<ShFolder> shFolders) {
		ShExchange shExchange = new ShExchange();
		List<ShFolderExchange> shFolderExchanges = new ArrayList<ShFolderExchange>();
		List<ShPostExchange> shPostExchanges = new ArrayList<ShPostExchange>();
		List<ShFileExchange> files = new ArrayList<ShFileExchange>();

		for (ShFolder shFolder : shFolders) {

			for (ShPost shPost : shPostRepository.findByShFolder(shFolder)) {
				ShPostExchange shPostExchange = new ShPostExchange();
				shPostExchange.setId(shPost.getId());
				shPostExchange.setFolder(shPost.getShFolder().getId());
				shPostExchange.setDate(shPost.getDate());
				shPostExchange.setPostType(shPost.getShPostType().getName());

				shPostExchange.setGlobalId(shPost.getShGlobalId().getId());
				Map<String, Object> fields = new HashMap<String, Object>();

				for (ShPostAttr shPostAttr : shPostAttrRepository.findByShPost(shPost)) {
					if (shPostAttr != null && shPostAttr.getShPostTypeAttr() != null) {
						fields.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
						if (shPostAttr.getShPostTypeAttr().getName().equals("FILE")
								&& shPost.getShPostType().getName().equals("PT-FILE")) {
							String fileName = shPostAttr.getStrValue();
							File directoryPath = shStaticFileUtils.dirPath(shPost.getShFolder());
							File file = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));
							ShFileExchange shFileExchange = new ShFileExchange();
							shFileExchange.setGlobalId(shPost.getShGlobalId().getId());
							shFileExchange.setId(shPost.getId());
							shFileExchange.setFile(file);
							files.add(shFileExchange);
						}
					}
				}

				shPostExchange.setFields(fields);

				shPostExchanges.add(shPostExchange);
			}
			ShFolderExchange shFolderExchangeChild = new ShFolderExchange();
			shFolderExchangeChild.setId(shFolder.getId());
			shFolderExchangeChild.setGlobalId(shFolder.getShGlobalId().getId());
			shFolderExchangeChild.setDate(shFolder.getDate());
			shFolderExchangeChild.setName(shFolder.getName());
			if (shFolder.getParentFolder() != null) {
				shFolderExchangeChild.setParentFolder(shFolder.getParentFolder().getId());
			}
			shFolderExchanges.add(shFolderExchangeChild);
			ShExchange shExchangeChild = this.shFolderExchangeNested(shFolder);
			shFolderExchanges.addAll(shExchangeChild.getFolders());
			shPostExchanges.addAll(shExchangeChild.getPosts());
			files.addAll(shExchangeChild.getFiles());
		}
		shExchange.setFolders(shFolderExchanges);
		shExchange.setPosts(shPostExchanges);
		shExchange.setFiles(files);
		return shExchange;
	}

}
