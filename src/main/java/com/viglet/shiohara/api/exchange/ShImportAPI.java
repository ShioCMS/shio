package com.viglet.shiohara.api.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import javax.ws.rs.Consumes;
import javax.ws.rs.DefaultValue;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.Context;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.UriInfo;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringUtils;
import org.glassfish.jersey.media.multipart.FormDataContentDisposition;
import org.glassfish.jersey.media.multipart.FormDataParam;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.api.SystemObjectView;
import com.viglet.shiohara.exchange.ShFolderExchange;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShStaticFileUtils;
import com.viglet.shiohara.utils.ShUtils;

@Component
@Path("/import")
public class ShImportAPI {

	@Autowired
	ShSiteRepository shSiteRepository;
	@Autowired
	ShFolderRepository shFolderRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	ShStaticFileUtils shStaticFileUtils;
	@Autowired
	ShUtils shUtils;
	Map<UUID, Object> shObjects = new HashMap<UUID, Object>();
	Map<UUID, List<UUID>> shChildObjects = new HashMap<UUID, List<UUID>>();

	@POST
	@Consumes(MediaType.MULTIPART_FORM_DATA)
	@Produces("application/json")
	@JsonView({ SystemObjectView.ShObject.class })
	public ShExchange siteImport(@DefaultValue("true") @FormDataParam("enabled") boolean enabled,
			@FormDataParam("file") InputStream inputStream,
			@FormDataParam("file") FormDataContentDisposition fileDetail, @Context UriInfo uriInfo) throws Exception {
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			File zipFile = new File(
					tmpDir.getAbsolutePath().concat(File.separator + "imp_" + fileDetail.getFileName()));

			OutputStream outputStream = new FileOutputStream(zipFile);
			IOUtils.copy(inputStream, outputStream);
			outputStream.close();
			File outputFolder = new File(tmpDir.getAbsolutePath().concat(File.separator + "imp_" + UUID.randomUUID()));
			shUtils.unZipIt(zipFile, outputFolder);
			ObjectMapper mapper = new ObjectMapper();
			File extractFolder = outputFolder;
			ShExchange shExchange = mapper.readValue(
					new FileInputStream(extractFolder.getAbsolutePath().concat(File.separator + "export.json")),
					ShExchange.class);
			for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
				shObjects.put(shSiteExchange.getId(), shSiteExchange);
				List<UUID> rootFolders = shSiteExchange.getRootFolders();

				ShSite shSite = new ShSite();
				shSite.setId(shSiteExchange.getId());
				shSite.setName(shSiteExchange.getName());
				shSite.setUrl(shSiteExchange.getUrl());
				shSite.setDescription(shSiteExchange.getDescription());
				shSite.setPostTypeLayout(shSiteExchange.getPostTypeLayout());
				shSite.setDate(shSiteExchange.getDate());

				shSiteRepository.save(shSite);

				ShGlobalId shGlobalId = new ShGlobalId();
				shGlobalId.setId(shSiteExchange.getGlobalId());
				shGlobalId.setShObject(shSite);
				shGlobalId.setType("SITE");

				shGlobalIdRepository.save(shGlobalId);

				for (ShFolderExchange shFolderExchange : shExchange.getFolders()) {

					shObjects.put(shFolderExchange.getId(), shFolderExchange);
					if (shFolderExchange.getParentFolder() != null) {
						if (shChildObjects.containsKey(shFolderExchange.getParentFolder())) {
							shChildObjects.get(shFolderExchange.getParentFolder()).add(shFolderExchange.getId());
						} else {
							List<UUID> childFolderList = new ArrayList<UUID>();
							childFolderList.add(shFolderExchange.getId());
							shChildObjects.put(shFolderExchange.getParentFolder(), childFolderList);
						}
					} else {
						if (rootFolders.contains(shFolderExchange.getId())) {
							if (shChildObjects.containsKey(shSite.getId())) {
								shChildObjects.get(shSite.getId()).add(shFolderExchange.getId());
							} else {
								List<UUID> childFolderList = new ArrayList<UUID>();
								childFolderList.add(shFolderExchange.getId());
								shChildObjects.put(shSite.getId(), childFolderList);
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
							List<UUID> childObjectList = new ArrayList<UUID>();
							childObjectList.add(shPostExchange.getId());
							shChildObjects.put(shPostExchange.getFolder(), childObjectList);
						}
					}
				}
				this.shFolderImportNested(shSiteExchange.getId(), extractFolder);
			}
			
			 try {
		            FileUtils.deleteDirectory(outputFolder);
		            FileUtils.deleteQuietly(zipFile);
		        } catch (IOException ex) {
		            ex.printStackTrace();
		        }
			 
			return shExchange;
		} else {
			return null;
		}
	}

	public void shFolderImportNested(UUID shObject, File extractFolder) throws IOException {
		if (shChildObjects.containsKey(shObject)) {
			for (UUID objectId : shChildObjects.get(shObject)) {
				if (shObjects.get(objectId) instanceof ShFolderExchange) {
					ShFolderExchange shFolderExchange = (ShFolderExchange) shObjects.get(objectId);
					ShFolder shFolderChild = new ShFolder();
					shFolderChild.setId(shFolderExchange.getId());
					shFolderChild.setDate(shFolderExchange.getDate());
					shFolderChild.setName(shFolderExchange.getName());
					if (shFolderExchange.getParentFolder() != null) {
						ShFolder parentFolder = shFolderRepository.findById(shFolderExchange.getParentFolder());
						shFolderChild.setParentFolder(parentFolder);
						shFolderChild.setRootFolder((byte) 0);
					} else {
						if (shObjects.get(shObject) instanceof ShSiteExchange) {
							ShSiteExchange shSiteExchange = (ShSiteExchange) shObjects.get(shObject);
							if (shSiteExchange.getRootFolders().contains(shFolderExchange.getId())) {
								shFolderChild.setRootFolder((byte) 1);
								ShSite parentSite = shSiteRepository.findById(shSiteExchange.getId());
								shFolderChild.setShSite(parentSite);
							}
						}
					}
					shFolderRepository.save(shFolderChild);

					ShGlobalId shGlobalId = new ShGlobalId();
					shGlobalId.setId(shFolderExchange.getGlobalId());
					shGlobalId.setShObject(shFolderChild);
					shGlobalId.setType("CHANNEL");

					shGlobalIdRepository.save(shGlobalId);

					this.shFolderImportNested(shFolderChild.getId(), extractFolder);
				}

				if (shObjects.get(objectId) instanceof ShPostExchange) {
					ShPostExchange shPostExchange = (ShPostExchange) shObjects.get(objectId);
					ShPost shPost = new ShPost();
					shPost.setId(shPostExchange.getId());
					shPost.setDate(shPostExchange.getDate());
					shPost.setShFolder(shFolderRepository.findById(shPostExchange.getFolder()));
					shPost.setShPostType(shPostTypeRepository.findByName(shPostExchange.getPostType()));

					for (Entry<String, Object> shPostField : shPostExchange.getFields().entrySet()) {
						ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository
								.findByShPostTypeAndName(shPost.getShPostType(), shPostField.getKey());
						if (shPostTypeAttr.getIsTitle() == (byte) 1) {
							shPost.setTitle(StringUtils.abbreviate((String) shPostField.getValue(), 255));
						} else if (shPostTypeAttr.getIsSummary() == (byte) 1) {
							shPost.setSummary(StringUtils.abbreviate((String) shPostField.getValue(), 255));
						}
						if (shPostTypeAttr.getName().equals("FILE") && shPostExchange.getPostType().equals("PT-FILE")) {
							String fileName = (String) shPostField.getValue();
							File directoryPath = shStaticFileUtils.dirPath(shPost.getShFolder());
							File fileSource = new File(extractFolder.getAbsolutePath()
									.concat(File.separator + shPostExchange.getGlobalId()));
							File fileDest = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));
							FileUtils.copyFile(fileSource, fileDest);
						}
					}
					shPostRepository.save(shPost);

					ShGlobalId shGlobalId = new ShGlobalId();
					shGlobalId.setId(shPostExchange.getGlobalId());
					shGlobalId.setShObject(shPost);
					shGlobalId.setType("POST");

					shGlobalIdRepository.save(shGlobalId);

					for (Entry<String, Object> shPostFields : shPostExchange.getFields().entrySet()) {
						ShPostAttr shPostAttr = new ShPostAttr();
						shPostAttr.setStrValue((String) shPostFields.getValue());
						shPostAttr.setShPost(shPost);
						shPostAttr.setShPostTypeAttr(shPostTypeAttrRepository
								.findByShPostTypeAndName(shPost.getShPostType(), shPostFields.getKey()));
						shPostAttr.setType(1);
						shPostAttrRepository.save(shPostAttr);
					}

				}
			}

		}
	}
}
