package com.viglet.shiohara.exchange;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.Map.Entry;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.relator.ShRelatorItem;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.relator.ShRelatorItemRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;
import com.viglet.shiohara.utils.ShUtils;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShImportExchange {

	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShRelatorItemRepository shRelatorItemRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShUtils shUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShURLFormatter shURLFormatter;

	private Map<UUID, Object> shObjects = new HashMap<UUID, Object>();
	private Map<UUID, List<UUID>> shChildObjects = new HashMap<UUID, List<UUID>>();

	public ShExchange importFromMultipartFile(MultipartFile multipartFile, String username)
			throws IllegalStateException, IOException, ArchiveException {
		File extractFolder = this.extractZipFile(multipartFile);
		File parentExtractFolder = null;

		if (extractFolder != null) {
			// Check if export.json exists, if it is not exist try access a sub directory
			if (!(new File(extractFolder, "export.json").exists()) && (extractFolder.listFiles().length == 1)) {
				for (File fileOrDirectory : extractFolder.listFiles()) {
					if (fileOrDirectory.isDirectory() && new File(fileOrDirectory, "export.json").exists()) {
						parentExtractFolder = extractFolder;
						extractFolder = fileOrDirectory;
					}
				}
			}
			ObjectMapper mapper = new ObjectMapper();

			ShExchange shExchange = mapper.readValue(
					new FileInputStream(extractFolder.getAbsolutePath().concat(File.separator + "export.json")),
					ShExchange.class);

			this.importSite(shExchange, username, extractFolder);

			try {
				FileUtils.deleteDirectory(extractFolder);
				if (parentExtractFolder != null) {
					FileUtils.deleteDirectory(parentExtractFolder);
				}
			} catch (IOException ex) {
				ex.printStackTrace();
			}
			return shExchange;
		} else {
			return null;
		}
	}

	public ShExchange importFromFile(File file, String username)
			throws IOException, IllegalStateException, ArchiveException {

		FileInputStream input = new FileInputStream(file);
		MultipartFile multipartFile = new MockMultipartFile(file.getName(), IOUtils.toByteArray(input));

		return this.importFromMultipartFile(multipartFile, username);
	}

	public File extractZipFile(MultipartFile file) throws IllegalStateException, IOException, ArchiveException {
		shObjects.clear();
		shChildObjects.clear();

		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			File zipFile = new File(tmpDir.getAbsolutePath()
					.concat(File.separator + "imp_" + file.getOriginalFilename() + UUID.randomUUID()));

			file.transferTo(zipFile);
			File extractFolder = new File(tmpDir.getAbsolutePath().concat(File.separator + "imp_" + UUID.randomUUID()));
			shUtils.unZipIt(zipFile, extractFolder);
			FileUtils.deleteQuietly(zipFile);
			return extractFolder;
		} else {
			return null;
		}
	}

	public void importSite(ShExchange shExchange, String username, File extractFolder) throws IOException {
		for (ShSiteExchange shSiteExchange : shExchange.getSites()) {
			this.prepareImport(shExchange, shSiteExchange);
			this.createShSite(shSiteExchange, username);
			// Create only Folders
			this.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, true);
			// Create all objects
			this.shFolderImportNested(shSiteExchange.getId(), extractFolder, username, false);
		}
	}

	public void prepareImport(ShExchange shExchange, ShSiteExchange shSiteExchange) {
		List<UUID> rootFolders = shSiteExchange.getRootFolders();

		shObjects.put(shSiteExchange.getId(), shSiteExchange);
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
					if (shChildObjects.containsKey(shSiteExchange.getId())) {
						shChildObjects.get(shSiteExchange.getId()).add(shFolderExchange.getId());
					} else {
						List<UUID> childFolderList = new ArrayList<UUID>();
						childFolderList.add(shFolderExchange.getId());
						shChildObjects.put(shSiteExchange.getId(), childFolderList);
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
	}

	public void shFolderImportNested(UUID shObject, File extractFolder, String username, boolean importOnlyFolders)
			throws IOException {
		if (shChildObjects.containsKey(shObject)) {
			for (UUID objectId : shChildObjects.get(shObject)) {
				if (shObjects.get(objectId) instanceof ShFolderExchange) {
					ShFolderExchange shFolderExchange = (ShFolderExchange) shObjects.get(objectId);
					this.createShFolder(shFolderExchange, extractFolder, username, shObject, importOnlyFolders);
				}

				if (!importOnlyFolders && shObjects.get(objectId) instanceof ShPostExchange) {
					ShPostExchange shPostExchange = (ShPostExchange) shObjects.get(objectId);
					this.createShPost(shPostExchange, extractFolder, username);
				}
			}

		}
	}

	public ShSite createShSite(ShSiteExchange shSiteExchange, String username) {
		ShSite shSite = null;

		if (shSiteRepository.findById(shSiteExchange.getId()).isPresent()) {
			shSite = shSiteRepository.findById(shSiteExchange.getId()).get();
		} else {
			shSite = new ShSite();
			shSite.setId(shSiteExchange.getId());
			shSite.setName(shSiteExchange.getName());
			shSite.setUrl(shSiteExchange.getUrl());
			shSite.setDescription(shSiteExchange.getDescription());
			shSite.setPostTypeLayout(shSiteExchange.getPostTypeLayout());
			if (shSiteExchange.getOwner() != null) {
				shSite.setOwner(shSiteExchange.getOwner());
			} else {
				shSite.setOwner(username);
			}
			if (shSiteExchange.getFurl() != null) {
				shSite.setFurl(shSiteExchange.getFurl());
			} else {
				shSite.setFurl(shURLFormatter.format(shSiteExchange.getName()));
			}
			shSite.setDate(shSiteExchange.getDate());

			shSiteRepository.save(shSite);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setId(shSiteExchange.getGlobalId());
			shGlobalId.setShObject(shSite);
			shGlobalId.setType(ShObjectType.SITE);

			shGlobalIdRepository.save(shGlobalId);
		}

		return shSite;

	}

	public ShFolder createShFolder(ShFolderExchange shFolderExchange, File extractFolder, String username,
			UUID shObject, boolean importOnlyFolders) throws IOException {
		ShFolder shFolderChild = null;
		if (shFolderRepository.findById(shFolderExchange.getId()).isPresent()) {
			shFolderChild = shFolderRepository.findById(shFolderExchange.getId()).get();
		} else {
			shFolderChild = new ShFolder();
			shFolderChild.setId(shFolderExchange.getId());
			shFolderChild.setDate(shFolderExchange.getDate());
			shFolderChild.setName(shFolderExchange.getName());
			if (shFolderExchange.getOwner() != null) {
				shFolderChild.setOwner(shFolderExchange.getOwner());
			} else {
				shFolderChild.setOwner(username);
			}
			if (shFolderExchange.getFurl() != null) {
				shFolderChild.setFurl(shFolderExchange.getFurl());
			} else {
				shFolderChild.setFurl(shURLFormatter.format(shFolderExchange.getName()));
			}
			if (shFolderExchange.getParentFolder() != null) {
				ShFolder parentFolder = shFolderRepository.findById(shFolderExchange.getParentFolder()).get();
				shFolderChild.setParentFolder(parentFolder);
				shFolderChild.setRootFolder((byte) 0);
			} else {
				if (shObjects.get(shObject) instanceof ShSiteExchange) {
					ShSiteExchange shSiteExchange = (ShSiteExchange) shObjects.get(shObject);
					if (shSiteExchange.getRootFolders().contains(shFolderExchange.getId())) {
						shFolderChild.setRootFolder((byte) 1);
						ShSite parentSite = shSiteRepository.findById(shSiteExchange.getId()).get();
						shFolderChild.setShSite(parentSite);
					}
				}
			}
			shFolderRepository.save(shFolderChild);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setId(shFolderExchange.getGlobalId());
			shGlobalId.setShObject(shFolderChild);
			shGlobalId.setType(ShObjectType.FOLDER);

			shGlobalIdRepository.save(shGlobalId);
		}
		this.shFolderImportNested(shFolderChild.getId(), extractFolder, username, importOnlyFolders);

		return shFolderChild;
	}

	@SuppressWarnings({ "unchecked" })
	public ShPost createShPost(ShPostExchange shPostExchange, File extractFolder, String username) {
		ShPost shPost = null;
		if (shPostRepository.findById(shPostExchange.getId()).isPresent()) {
			shPost = shPostRepository.findById(shPostExchange.getId()).get();
		} else {
			shPost = new ShPost();
			shPost.setId(shPostExchange.getId());
			shPost.setDate(shPostExchange.getDate());
			shPost.setShFolder(shFolderRepository.findById(shPostExchange.getFolder()).get());
			shPost.setShPostType(shPostTypeRepository.findByName(shPostExchange.getPostType()));
			if (shPostExchange.getOwner() != null) {
				shPost.setOwner(shPostExchange.getOwner());
			} else {
				shPost.setOwner(username);
			}

			for (Entry<String, Object> shPostField : shPostExchange.getFields().entrySet()) {
				ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPost.getShPostType(),
						shPostField.getKey());
				if (shPostTypeAttr.getIsTitle() == (byte) 1) {
					shPost.setTitle(StringUtils.abbreviate((String) shPostField.getValue(), 255));
				} else if (shPostTypeAttr.getIsSummary() == (byte) 1) {
					shPost.setSummary(StringUtils.abbreviate((String) shPostField.getValue(), 255));
				}
				if (shPostTypeAttr.getName().equals(ShSystemPostTypeAttr.FILE)
						&& shPostExchange.getPostType().equals(ShSystemPostType.FILE)) {
					String fileName = (String) shPostField.getValue();
					File directoryPath = shStaticFileUtils.dirPath(shPost.getShFolder());
					File fileSource = new File(
							extractFolder.getAbsolutePath().concat(File.separator + shPostExchange.getGlobalId()));
					File fileDest = new File(directoryPath.getAbsolutePath().concat(File.separator + fileName));
					try {
						FileUtils.copyFile(fileSource, fileDest);
					} catch (IOException e) {
						// TODO Auto-generated catch block
						e.printStackTrace();
					}
				}
			}

			if (shPostExchange.getFurl() != null) {
				shPost.setFurl(shPostExchange.getFurl());
			} else {
				shPost.setFurl(shURLFormatter.format(shPost.getTitle()));
			}
			shPostRepository.saveAndFlush(shPost);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setId(shPostExchange.getGlobalId());
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.saveAndFlush(shGlobalId);

			shPost.setShGlobalId(shGlobalId);

			for (Entry<String, Object> shPostFields : shPostExchange.getFields().entrySet()) {
				ShPostType shPostType = shPostTypeRepository.findByName(shPostExchange.getPostType());
				ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
						shPostFields.getKey());
				if ((shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.FILE)
						|| shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.CONTENT_SELECT))
						&& shPostFields.getValue() != null && !shPostType.getName().equals(ShSystemPostType.FILE)) {
					try {
						UUID shReferencedPostUUID = UUID.fromString((String) shPostFields.getValue());
						if (!shPostRepository.findById(shReferencedPostUUID).isPresent()) {
							// So the referenced Post not exists, need create first
							if (shObjects.get(shReferencedPostUUID) instanceof ShPostExchange) {
								ShPostExchange shReferencedPostExchange = (ShPostExchange) shObjects
										.get(shReferencedPostUUID);
								this.createShPost(shReferencedPostExchange, extractFolder, username);
							}
						}
					} catch (IllegalArgumentException iae) {
						// iae.printStackTrace();
					}
				}
				if (shPostTypeAttr.getShWidget().getName().equals(ShSystemWidget.RELATOR)) {

					LinkedHashMap<String, Object> relatorFields = (LinkedHashMap<String, Object>) shPostFields
							.getValue();

					ShPostAttr shPostAttr = new ShPostAttr();
					shPostAttr.setShPost(shPost);
					shPostAttr.setId(UUID.fromString((String) relatorFields.get("id")));
					shPostAttr.setStrValue((String) relatorFields.get("name"));
					shPostAttr.setShPostTypeAttr(shPostTypeAttrRepository
							.findByShPostTypeAndName(shPost.getShPostType(), shPostFields.getKey()));
					shPostAttr.setType(1);

					shPostAttrRepository.save(shPostAttr);

					for (Object shSubPost : (ArrayList<Object>) relatorFields.get("shSubPosts")) {
						ShRelatorItem shRelatorItem = new ShRelatorItem();
						shRelatorItem.setShParentPostAttr(shPostAttr);

						shRelatorItemRepository.save(shRelatorItem);

						for (Entry<String, Object> shSubPostFields : ((Map<String, Object>) shSubPost).entrySet()) {
							ShPostAttr shSubPostAttrs = new ShPostAttr();
							shSubPostAttrs.setStrValue((String) shSubPostFields.getValue());
							shSubPostAttrs.setShPostTypeAttr(shPostTypeAttrRepository.findByShParentPostTypeAttrAndName(
									shPostAttr.getShPostTypeAttr(), shSubPostFields.getKey()));
							shSubPostAttrs.setType(1);
							shSubPostAttrs.setShParentRelatorItem(shRelatorItem);

							shPostAttrRepository.save(shSubPostAttrs);

							shPostUtils.referencedObject(shPostAttr, shPost);

							shPostAttrRepository.save(shPostAttr);

						}
					}
				} else {
					ShPostAttr shPostAttr = new ShPostAttr();
					shPostAttr.setStrValue((String) shPostFields.getValue());
					shPostAttr.setShPost(shPost);
					shPostAttr.setShPostTypeAttr(shPostTypeAttrRepository
							.findByShPostTypeAndName(shPost.getShPostType(), shPostFields.getKey()));
					shPostAttr.setType(1);
					shPostAttrRepository.save(shPostAttr);

					shPostUtils.referencedObject(shPostAttr, shPost);

					shPostAttrRepository.save(shPostAttr);
				}
			}
		}
		return shPost;
	}
}
