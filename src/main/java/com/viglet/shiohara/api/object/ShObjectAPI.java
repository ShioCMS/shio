package com.viglet.shiohara.api.object;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

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
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.url.ShURLFormatter;
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
	private ShPostRepository shPostRepository;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShURLFormatter shURLFormatter;
	
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectList() throws Exception {
		return shObjectRepository.findAll();
	}

	@GetMapping("/{id}/preview")
	public RedirectView shObjectPreview(@PathVariable String id, HttpServletResponse response,
			RedirectAttributes attributes) throws Exception {
		String redirect = null;
		ShObject shObject = shObjectRepository.findById(id).get();
		if (shObject instanceof ShSite) {
			redirect = shSiteUtils.generatePostLink((ShSite) shObject);
		} else if (shObject instanceof ShPost) {
			redirect = shPostUtils.generatePostLink((ShPost) shObject);
		} else if (shObject instanceof ShFolder) {
			redirect = shFolderUtils.generateFolderLink((ShFolder) shObject);
		}

		RedirectView redirectView = new RedirectView(new String(redirect.getBytes("UTF-8"), "ISO-8859-1"));
		redirectView.setHttp10Compatible(false);
		return redirectView;
	}

	@PutMapping("/moveto/{globallIdDest}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectMoveTo(@PathVariable String globallIdDest, @RequestBody List<String> globalIds)
			throws Exception {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		for (String globalId : globalIds) {
			ShObject shObject = shObjectRepository.findById(globalId).get();
			ShObject shObjectDest = shObjectRepository.findById(globallIdDest).get();
			if (shObjectDest instanceof ShFolder) {
				ShFolder shFolderDest = (ShFolder) shObjectDest;
				if (shObject instanceof ShPost) {
					ShPost shPost = (ShPost) shObject;
					shPost.setShFolder(shFolderDest);
					shPost.setFurl(shURLFormatter.format(shPost.getTitle()));
					shPostRepository.save(shPost);
					shObjects.add(shPost);
				} else if (shObject instanceof ShFolder) {
					ShFolder shFolder = (ShFolder) shObject;
					shFolder.setParentFolder(shFolderDest);
					shFolder.setRootFolder((byte) 0);
					shFolder.setShSite(null);
					shFolder.setFurl(shURLFormatter.format(shFolder.getName()));
					shFolderRepository.save(shFolder);
					shObjects.add(shFolder);
				}
			} else if (shObjectDest instanceof ShSite) {
				if (shObject instanceof ShFolder) {
					ShSite shSiteDest = (ShSite) shObjectDest;
					ShFolder shFolder = (ShFolder) shObject;
					shFolder.setParentFolder(null);
					shFolder.setRootFolder((byte) 1);
					shFolder.setShSite(shSiteDest);
					shFolder.setFurl(shURLFormatter.format(shFolder.getName()));
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
			ShObject shObject = shObjectRepository.findById(globalId).get();
			ShObject shObjectDest = shObjectRepository.findById(globallIdDest).get();
			if (shObjectDest instanceof ShFolder) {
				ShFolder shFolderDest = (ShFolder) shObjectDest;
				if (shObject instanceof ShPost) {
					ShPost shPost = (ShPost) shObject;
					shObjects.add(shPostUtils.copy(shPost, shFolderDest));
				} else if (shObject instanceof ShFolder) {
					ShFolder shFolder = (ShFolder) shObject;
					shObjects.add(shFolderUtils.copy(shFolder, shObjectDest));
				}
			} else if (shObjectDest instanceof ShSite) {
				if (shObject instanceof ShFolder) {
					ShFolder shFolder = (ShFolder) shObject;
					shObjects.add(shFolderUtils.copy(shFolder, shObjectDest));
				}
			}
		}
		return shObjects;
	}

	@GetMapping("/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shObjectListItem(@PathVariable String id) throws Exception {
		ShObject shObject = shObjectRepository.findById(id).get();
		if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
			String folderPath = shFolderUtils.folderPath(shFolder, true);
			ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
			ShSite shSite = breadcrumb.get(0).getShSite();
			ShFolderList shFolderList = new ShFolderList();
			shFolderList.setShFolders(shFolderRepository.findByParentFolder(shFolder));
			shFolderList.setShPosts(shPostRepository.findByShFolder(shFolder));
			shFolderList.setFolderPath(folderPath);
			shFolderList.setBreadcrumb(breadcrumb);
			shFolderList.setShSite(shSite);
			return shFolderList;
		} else if (shObject instanceof ShSite) {
			ShSite shSite = (ShSite) shObject;
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
		ShObject shObject = shObjectRepository.findById(id).get();
		ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
		if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
			String folderPath = shFolderUtils.folderPath(shFolder, true);
			ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
			ShSite shSite = breadcrumb.get(0).getShSite();
			ShFolderList shFolderList = new ShFolderList();
			shFolderList.setShFolders(shFolderRepository.findByParentFolder(shFolder));
			shFolderList.setShPosts(shPostRepository.findByShFolderAndShPostType(shFolder, shPostType));
			shFolderList.setFolderPath(folderPath);
			shFolderList.setBreadcrumb(breadcrumb);
			shFolderList.setShSite(shSite);
			return shFolderList;
		} else if (shObject instanceof ShSite) {
			ShSite shSite = (ShSite) shObject;
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
		ShObject shObject = shObjectRepository.findById(id).get();
		if (shObject instanceof ShSite) {
			ShSite shSite = (ShSite) shObject;
			if (shSite != null) {
				ShFolderPath shFolderPath = new ShFolderPath();
				shFolderPath.setFolderPath(null);
				shFolderPath.setCurrentFolder(null);
				shFolderPath.setBreadcrumb(null);
				shFolderPath.setShSite(shSite);
				return shFolderPath;
			}
		} else if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
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
			}
		} else if (shObject instanceof ShPost) {
			ShPost shPost = (ShPost) shObject;
			if (shPost != null) {
				ShFolder shFolder = shPost.getShFolder();
				ShFolderPath shFolderPath = new ShFolderPath();
				String folderPath = shFolderUtils.folderPath(shFolder, true);
				ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
				ShSite shSite = breadcrumb.get(0).getShSite();
				shFolderPath.setFolderPath(folderPath);
				shFolderPath.setCurrentFolder(shFolder);
				shFolderPath.setBreadcrumb(breadcrumb);
				shFolderPath.setShSite(shSite);
				shFolderPath.setShPost(shPost);
				return shFolderPath;
			}
		}
		return null;
	}
	
	@ApiOperation(value = "Sort Object")
	@PutMapping("/sort")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public Map<String, Integer> shObjectSort(@RequestBody Map<String, Integer> objectOrder) throws Exception {
		for (Entry<String, Integer> objectOrderItem : objectOrder.entrySet()) {
			int shObjectOrder = objectOrderItem.getValue();
			String shObjectId = objectOrderItem.getKey();
			
			ShObject shObject = shObjectRepository.findById(shObjectId).get();
			shObject.setPosition(shObjectOrder);
			shObjectRepository.save(shObject);
		}
		return objectOrder;

	}
}
