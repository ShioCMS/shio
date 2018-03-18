package com.viglet.shiohara.api.site;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.zip.ZipOutputStream;

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
import com.viglet.shiohara.api.SystemObjectView;
import com.viglet.shiohara.api.channel.ShChannelList;
import com.viglet.shiohara.exchange.ShChannelExchange;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShFileExchange;
import com.viglet.shiohara.exchange.ShPostExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShChannelUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;
import com.viglet.shiohara.utils.ShUtils;

@Component
@Path("/site")
public class ShSiteAPI {

	@Autowired
	ShSiteRepository shSiteRepository;
	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShChannelUtils shChannelUtils;
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
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShSite> list() throws Exception {
		return shSiteRepository.findAll();
	}

	@Path("/{siteId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public ShSite edit(@PathParam("siteId") UUID id) throws Exception {
		return shSiteRepository.findById(id);
	}

	@Path("/{siteId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
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

		for (ShChannel shChannel : shSite.getShChannels()) {			
			shChannelUtils.deleteChannel(shChannel);
		}

		shGlobalIdRepository.delete(shSite.getShGlobalId().getId());
		shSiteRepository.delete(id);

		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public ShSite add(ShSite shSite) throws Exception {
		shSite.setDate(new Date());

		shSiteRepository.save(shSite);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shSite);
		shGlobalId.setType("SITE");

		shGlobalIdRepository.save(shGlobalId);

		// Home Channel
		ShChannel shChannelHome = new ShChannel();
		shChannelHome.setName("Home");
		shChannelHome.setParentChannel(null);
		shChannelHome.setShSite(shSite);
		shChannelHome.setDate(new Date());
		shChannelHome.setRootChannel((byte) 1);

		shChannelRepository.save(shChannelHome);

		shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shChannelHome);
		shGlobalId.setType("CHANNEL");

		shGlobalIdRepository.save(shGlobalId);

		// Channel Index

		ShPostType shPostChannelIndex = shPostTypeRepository.findByName("PT-CHANNEL-INDEX");

		ShPost shPost = new ShPost();
		shPost.setDate(new Date());
		shPost.setShPostType(shPostChannelIndex);
		shPost.setSummary("Channel Index");
		shPost.setTitle("index");
		shPost.setShChannel(shChannelHome);

		shPostRepository.save(shPost);

		shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPost);
		shGlobalId.setType("POST");

		shGlobalIdRepository.save(shGlobalId);

		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "TITLE");

		ShPostAttr shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getTitle());
		shPostAttr.setType(1);
		shPostAttrRepository.save(shPostAttr);

		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "DESCRIPTION");

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getSummary());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);
		return shSite;

	}

	@Path("/{siteId}/channel")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public ShChannelList rootChannel(@PathParam("siteId") UUID id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id);

		ShChannelList shChannelList = new ShChannelList();
		shChannelList.setShChannels(shChannelRepository.findByShSiteAndRootChannel(shSite, (byte) 1));
		shChannelList.setShPosts(shPostRepository.findByShChannel(null));
		shChannelList.setShSite(shSite);
		return shChannelList;

	}

	@Path("/{siteId}/export")
	@GET
	@Produces(MediaType.APPLICATION_OCTET_STREAM)
	@JsonView({ SystemObjectView.ShObject.class })
	public Response siteExport(@PathParam("siteId") UUID id) throws Exception {
		FileOutputStream fos = null;
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			ShExchange shExchange = new ShExchange();
			ShSite shSite = shSiteRepository.findById(id);

			List<ShChannel> rootChannels = shChannelRepository.findByShSiteAndRootChannel(shSite, (byte) 1);

			List<UUID> rootChannelsUUID = new ArrayList<UUID>();
			for (ShChannel shChannel : rootChannels) {
				rootChannelsUUID.add(shChannel.getId());
			}

			ShSiteExchange shSiteExchange = new ShSiteExchange();
			shSiteExchange.setId(shSite.getId());
			shSiteExchange.setName(shSite.getName());
			shSiteExchange.setUrl(shSite.getUrl());
			shSiteExchange.setDescription(shSite.getDescription());
			shSiteExchange.setPostTypeLayout(shSite.getPostTypeLayout());
			shSiteExchange.setDate(shSite.getDate());
			shSiteExchange.setRootChannels(rootChannelsUUID);
			shSiteExchange.setGlobalId(shSite.getShGlobalId().getId());

			List<ShSiteExchange> shSiteExchanges = new ArrayList<ShSiteExchange>();
			shSiteExchanges.add(shSiteExchange);
			shExchange.setSites(shSiteExchanges);

			ShExchange shExchangeChannel = this.shChannelExchangeIterate(rootChannels);

			shExchange.setChannels(shExchangeChannel.getChannels());
			shExchange.setPosts(shExchangeChannel.getPosts());
			File exportDir = new File(tmpDir.getAbsolutePath().concat(File.separator + folderName));
			if (!exportDir.exists()) {
				exportDir.mkdirs();
			}

			for (ShFileExchange fileExchange : shExchangeChannel.getFiles()) {
				FileUtils.copyFile(fileExchange.getFile(),
						new File(exportDir.getAbsolutePath().concat(File.separator + fileExchange.getGlobalId())));
			}
			// Object to JSON in file
			ObjectMapper mapper = new ObjectMapper();
			mapper.writerWithDefaultPrettyPrinter().writeValue(
					new File(exportDir.getAbsolutePath().concat(File.separator + "export.json")), shExchange);

			String fileZip = tmpDir.getAbsolutePath().concat(File.separator + folderName + ".zip");
			fos = new FileOutputStream(fileZip);
			ZipOutputStream zipOut = new ZipOutputStream(fos);
			shUtils.zipFile(exportDir, exportDir.getName(), zipOut);
			zipOut.close();
			fos.close();

			StreamingOutput fileStream = new StreamingOutput() {
				@Override
				public void write(java.io.OutputStream output) throws IOException, WebApplicationException {
					try {
						java.nio.file.Path path = Paths.get(fileZip);
						byte[] data = Files.readAllBytes(path);
						output.write(data);
						output.flush();
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
	@JsonView({ SystemObjectView.ShObject.class })
	public ShSite siteStructure() throws Exception {
		ShSite shSite = new ShSite();
		return shSite;

	}

	public ShExchange shChannelExchangeNested(ShChannel shChannel) {
		List<ShChannel> childChannels = shChannelRepository.findByParentChannel(shChannel);
		return this.shChannelExchangeIterate(childChannels);
	}

	public ShExchange shChannelExchangeIterate(List<ShChannel> shChannels) {
		ShExchange shExchange = new ShExchange();
		List<ShChannelExchange> shChannelExchanges = new ArrayList<ShChannelExchange>();
		List<ShPostExchange> shPostExchanges = new ArrayList<ShPostExchange>();
		List<ShFileExchange> files = new ArrayList<ShFileExchange>();

		for (ShChannel shChannel : shChannels) {
			for (ShPost shPost : shChannel.getShPosts()) {
				ShPostExchange shPostExchange = new ShPostExchange();
				shPostExchange.setId(shPost.getId());
				shPostExchange.setChannel(shPost.getShChannel().getId());
				shPostExchange.setDate(shPost.getDate());
				shPostExchange.setPostType(shPost.getShPostType().getName());

				shPostExchange.setGlobalId(shPost.getShGlobalId().getId());
				Map<String, Object> fields = new HashMap<String, Object>();
				for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
					if (shPostAttr != null && shPostAttr.getShPostTypeAttr() != null) {
						fields.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
						if (shPostAttr.getShPostTypeAttr().getName().equals("FILE")
								&& shPost.getShPostType().getName().equals("PT-FILE")) {
							String fileName = shPostAttr.getStrValue();
							File directoryPath = shStaticFileUtils.dirPath(shPost.getShChannel());
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
			ShChannelExchange shChannelExchangeChild = new ShChannelExchange();
			shChannelExchangeChild.setId(shChannel.getId());
			shChannelExchangeChild.setGlobalId(shChannel.getShGlobalId().getId());
			shChannelExchangeChild.setDate(shChannel.getDate());
			shChannelExchangeChild.setName(shChannel.getName());
			if (shChannel.getParentChannel() != null) {
				shChannelExchangeChild.setParentChannel(shChannel.getParentChannel().getId());
			}
			shChannelExchanges.add(shChannelExchangeChild);
			ShExchange shExchangeChild = this.shChannelExchangeNested(shChannel);
			shChannelExchanges.addAll(shExchangeChild.getChannels());
			shPostExchanges.addAll(shExchangeChild.getPosts());
			files.addAll(shExchangeChild.getFiles());
		}
		shExchange.setChannels(shChannelExchanges);
		shExchange.setPosts(shPostExchanges);
		shExchange.setFiles(files);
		return shExchange;
	}

}