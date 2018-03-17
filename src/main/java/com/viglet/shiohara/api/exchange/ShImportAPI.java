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
import com.viglet.shiohara.exchange.ShChannelExchange;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
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
	ShChannelRepository shChannelRepository;
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
			File extractFolder = new File(outputFolder.getAbsolutePath()
					.concat(File.separator + fileDetail.getFileName().replaceAll(".zip", "")));
			ShExchange shExchange = mapper.readValue(
					new FileInputStream(extractFolder.getAbsolutePath().concat(File.separator + "export.json")),
					ShExchange.class);
			for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
				shObjects.put(shSiteExchange.getId(), shSiteExchange);
				List<UUID> rootChannels = shSiteExchange.getRootChannels();

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

				for (ShChannelExchange shChannelExchange : shExchange.getChannels()) {

					shObjects.put(shChannelExchange.getId(), shChannelExchange);
					if (shChannelExchange.getParentChannel() != null) {
						if (shChildObjects.containsKey(shChannelExchange.getParentChannel())) {
							shChildObjects.get(shChannelExchange.getParentChannel()).add(shChannelExchange.getId());
						} else {
							List<UUID> childChannelList = new ArrayList<UUID>();
							childChannelList.add(shChannelExchange.getId());
							shChildObjects.put(shChannelExchange.getParentChannel(), childChannelList);
						}
					} else {
						if (rootChannels.contains(shChannelExchange.getId())) {
							if (shChildObjects.containsKey(shSite.getId())) {
								shChildObjects.get(shSite.getId()).add(shChannelExchange.getId());
							} else {
								List<UUID> childChannelList = new ArrayList<UUID>();
								childChannelList.add(shChannelExchange.getId());
								shChildObjects.put(shSite.getId(), childChannelList);
							}
						}

					}
				}

				for (ShPostExchange shPostExchange : shExchange.getPosts()) {

					shObjects.put(shPostExchange.getId(), shPostExchange);
					if (shPostExchange.getChannel() != null) {
						if (shChildObjects.containsKey(shPostExchange.getChannel())) {
							shChildObjects.get(shPostExchange.getChannel()).add(shPostExchange.getId());
						} else {
							List<UUID> childObjectList = new ArrayList<UUID>();
							childObjectList.add(shPostExchange.getId());
							shChildObjects.put(shPostExchange.getChannel(), childObjectList);
						}
					}
				}
				this.shChannelImportNested(shSiteExchange.getId(), extractFolder);
			}
			return shExchange;
		} else {
			return null;
		}
	}

	public void shChannelImportNested(UUID shObject, File extractFolder) throws IOException {
		if (shChildObjects.containsKey(shObject)) {
			for (UUID objectId : shChildObjects.get(shObject)) {
				if (shObjects.get(objectId) instanceof ShChannelExchange) {
					ShChannelExchange shChannelExchange = (ShChannelExchange) shObjects.get(objectId);
					ShChannel shChannelChild = new ShChannel();
					shChannelChild.setId(shChannelExchange.getId());
					shChannelChild.setDate(shChannelExchange.getDate());
					shChannelChild.setName(shChannelExchange.getName());
					if (shChannelExchange.getParentChannel() != null) {
						ShChannel parentChannel = shChannelRepository.findById(shChannelExchange.getParentChannel());
						shChannelChild.setParentChannel(parentChannel);
						shChannelChild.setRootChannel((byte) 0);
					} else {
						if (shObjects.get(shObject) instanceof ShSiteExchange) {
							ShSiteExchange shSiteExchange = (ShSiteExchange) shObjects.get(shObject);
							if (shSiteExchange.getRootChannels().contains(shChannelExchange.getId())) {
								shChannelChild.setRootChannel((byte) 1);
								ShSite parentSite = shSiteRepository.findById(shSiteExchange.getId());
								shChannelChild.setShSite(parentSite);
							}
						}
					}
					shChannelRepository.save(shChannelChild);

					ShGlobalId shGlobalId = new ShGlobalId();
					shGlobalId.setId(shChannelExchange.getGlobalId());
					shGlobalId.setShObject(shChannelChild);
					shGlobalId.setType("CHANNEL");

					shGlobalIdRepository.save(shGlobalId);

					this.shChannelImportNested(shChannelChild.getId(), extractFolder);
				}

				if (shObjects.get(objectId) instanceof ShPostExchange) {
					ShPostExchange shPostExchange = (ShPostExchange) shObjects.get(objectId);
					ShPost shPost = new ShPost();
					shPost.setId(shPostExchange.getId());
					shPost.setDate(shPostExchange.getDate());
					shPost.setShChannel(shChannelRepository.findById(shPostExchange.getChannel()));
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
							File directoryPath = shStaticFileUtils.dirPath(shPost.getShChannel());
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
