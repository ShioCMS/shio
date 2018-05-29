package com.viglet.shiohara.api.object;

import java.util.ArrayList;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.mvc.support.RedirectAttributes;
import org.springframework.web.servlet.view.RedirectView;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.api.folder.ShFolderList;
import com.viglet.shiohara.api.folder.ShFolderPath;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@RequestMapping("/api/v2/object")
@Api(tags = "Object", description = "Object API")
public class ShObjectAPI {

	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	@GetMapping("/{id}/preview")
	public RedirectView shObjectPreview(@PathVariable String id, HttpServletResponse response,
			RedirectAttributes attributes) throws Exception {
		String redirect = null;
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(id).get();
		if (shGlobalId.getType().equals(ShObjectType.SITE)) {
			ShSite shSite = shSiteRepository.findById(shGlobalId.getShObject().getId()).get();
			redirect = shSiteUtils.generatePostLink(shSite);
		} else if (shGlobalId.getType().equals(ShObjectType.POST)) {
			ShPost shPost = shPostRepository.findById(shGlobalId.getShObject().getId()).get();
			redirect = shPostUtils.generatePostLink(shPost);
		} else if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
			ShFolder shFolder = shFolderRepository.findById(shGlobalId.getShObject().getId()).get();
			redirect = shFolderUtils.generateFolderLink(shFolder);
		}

		RedirectView redirectView = new RedirectView(new String(redirect.getBytes("UTF-8"), "ISO-8859-1"));

		return redirectView;
	}

	@PutMapping("/moveto/{globallIdDest}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectMoveTo(@PathVariable String globallIdDest, @RequestBody List<String> globalIds)
			throws Exception {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		for (String globalId : globalIds) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(globalId).get();
			ShGlobalId shGlobalIdDest = shGlobalIdRepository.findById(globallIdDest).get();
			if (shGlobalIdDest.getType().equals(ShObjectType.FOLDER)) {
				ShFolder shFolderDest = (ShFolder) shGlobalIdDest.getShObject();
				if (shGlobalId.getType().equals(ShObjectType.POST)) {
					ShPost shPost = (ShPost) shGlobalId.getShObject();
					shPost.setShFolder(shFolderDest);
					shPostRepository.save(shPost);
					shObjects.add(shPost);
				} else if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
					ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
					shFolder.setParentFolder(shFolderDest);
					shFolder.setRootFolder((byte) 0);
					shFolder.setShSite(null);
					shFolderRepository.save(shFolder);
					shObjects.add(shFolder);
				}
			} else if (shGlobalIdDest.getType().equals(ShObjectType.SITE)) {
				if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
					ShSite shSiteDest = (ShSite) shGlobalIdDest.getShObject();
					ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
					shFolder.setParentFolder(null);
					shFolder.setRootFolder((byte) 1);
					shFolder.setShSite(shSiteDest);
					shFolderRepository.save(shFolder);
					shObjects.add(shFolder);
				}
			}
		}
		return shObjects;
	}

	@PutMapping("/copyto/{globallIdDest}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectCopyTo(@PathVariable String globallIdDest, @RequestBody List<String> globalIds)
			throws Exception {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		for (String globalId : globalIds) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(globalId).get();
			ShGlobalId shGlobalIdDest = shGlobalIdRepository.findById(globallIdDest).get();
			if (shGlobalIdDest.getType().equals(ShObjectType.FOLDER)) {
				ShFolder shFolderDest = (ShFolder) shGlobalIdDest.getShObject();
				if (shGlobalId.getType().equals(ShObjectType.POST)) {
					ShPost shPost = (ShPost) shGlobalId.getShObject();
					shObjects.add(shPostUtils.copy(shPost, shFolderDest));
				} else if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
					ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
					shObjects.add(shFolderUtils.copy(shFolder, shGlobalIdDest));
				}
			} else if (shGlobalIdDest.getType().equals(ShObjectType.SITE)) {
				if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
					ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
					shObjects.add(shFolderUtils.copy(shFolder, shGlobalIdDest));
				}
			}
		}
		return shObjects;
	}

	@GetMapping("/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shObjectListItem(@PathVariable String id) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(id).get();
		if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
			ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
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
		} else if (shGlobalId.getType().equals(ShObjectType.SITE)) {
			ShSite shSite = (ShSite) shGlobalId.getShObject();
			List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);
			ShFolderList shFolderList = new ShFolderList();
			shFolderList.setShFolders(shFolders);
			shFolderList.setShSite(shSite);
			return shFolderList;
		} else {
			return null;
		}
	}

	@GetMapping("/{id}/list/{postTypeName}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shFolderListByPostType(@PathVariable String id, @PathVariable String postTypeName)
			throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(id).get();
		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
			ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
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
		} else if (shGlobalId.getType().equals(ShObjectType.SITE)) {
			ShSite shSite = (ShSite) shGlobalId.getShObject();
			List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);
			ShFolderList shFolderList = new ShFolderList();
			shFolderList.setShFolders(shFolders);
			shFolderList.setShSite(shSite);
			return shFolderList;
		} else {
			return null;
		}
	}

	@ApiOperation(value = "Object path")
	@GetMapping("/{id}/path")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderPath shObjectPath(@PathVariable String id) throws Exception {
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(id).get();
		if (shGlobalId.getType().equals(ShObjectType.SITE)) {
			ShSite shSite = (ShSite) shGlobalId.getShObject();
			if (shSite != null) {
				ShFolderPath shFolderPath = new ShFolderPath();
				shFolderPath.setFolderPath(null);
				shFolderPath.setCurrentFolder(null);
				shFolderPath.setBreadcrumb(null);
				shFolderPath.setShSite(shSite);
				return shFolderPath;
			} else {
				return null;
			}
		} else if (shGlobalId.getType().equals(ShObjectType.FOLDER)) {
			ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
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
		} else if (shGlobalId.getType().equals(ShObjectType.POST)) {
			ShPost shPost = (ShPost) shGlobalId.getShObject();
			if (shPost != null) {
				ShFolder shFolder = shPost.getShFolder();
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
		} else {
			return null;
		}
	}
}
