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

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

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

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/site")
@Api(tags="Site", description="Site API")
public class ShSiteAPI {

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShUtils shUtils;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShSite> shSiteList() throws Exception {
		return shSiteRepository.findAll();
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteEdit(@PathVariable UUID id) throws Exception {
		return shSiteRepository.findById(id).get();
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteUpdate(@PathVariable UUID id, @RequestBody ShSite shSite) throws Exception {
		ShSite shSiteEdit = shSiteRepository.findById(id).get();
		shSiteEdit.setDate(new Date());
		shSiteEdit.setName(shSite.getName());
		shSiteEdit.setPostTypeLayout(shSite.getPostTypeLayout());
		shSiteRepository.save(shSiteEdit);
		return shSiteEdit;
	}

	@DeleteMapping("/{id}")
	@Transactional
	public boolean shSiteDelete(@PathVariable UUID id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id).get();

		List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);

		for (ShFolder shFolder : shFolders) {
			shFolderUtils.deleteFolder(shFolder);
		}

		shGlobalIdRepository.delete(shSite.getShGlobalId().getId());
		shSiteRepository.delete(id);

		return true;
	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteAdd(@RequestBody ShSite shSite) throws Exception {
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

	@GetMapping("/{id}/folder")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shSiteRootFolder(@PathVariable UUID id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id).get();
		List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);
		ShFolderList shFolderList = new ShFolderList();
		shFolderList.setShFolders(shFolders);
		shFolderList.setShSite(shSite);
		return shFolderList;

	}

	@ResponseBody
	@GetMapping(value = "/{id}/export", produces = "application/zip")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public StreamingResponseBody shSiteExport(@PathVariable UUID id, HttpServletResponse response) throws Exception {
		String folderName = UUID.randomUUID().toString();
		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			ShExchange shExchange = new ShExchange();
			ShSite shSite = shSiteRepository.findById(id).get();

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

			response.addHeader("Content-disposition", "attachment;filename=" + folderName + ".zip");
		    response.setContentType("application/octet-stream");
		    response.setStatus(HttpServletResponse.SC_OK);
		    
			return new StreamingResponseBody() {
				@Override
				public void writeTo(java.io.OutputStream output) throws IOException {

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
						e.printStackTrace();
					}
				}
			};
		} else {
			return null;
		}

	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteStructure() throws Exception {
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
