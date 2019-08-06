/*
 * Copyright (C) 2016-2018 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
 * 
 * This program is free software: you can redistribute it and/or modify
 * it under the terms of the GNU General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * This program is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU General Public License
 * along with this program.  If not, see <https://www.gnu.org/licenses/>.
 */

package com.viglet.shiohara.api.object;

import java.io.UnsupportedEncodingException;
import java.security.Principal;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Optional;
import java.util.Set;

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
import com.viglet.shiohara.bean.ShFolderTinyBean;
import com.viglet.shiohara.bean.ShPostTinyBean;
import com.viglet.shiohara.cache.component.ShCacheObject;
import com.viglet.shiohara.persistence.model.auth.ShGroup;
import com.viglet.shiohara.persistence.model.auth.ShUser;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.auth.ShUserRepository;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.object.ShObjectRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;
import com.viglet.shiohara.utils.stage.ShStageFolderUtils;
import com.viglet.shiohara.utils.stage.ShStageObjectUtils;
import com.viglet.shiohara.utils.stage.ShStagePostUtils;
import com.viglet.shiohara.workflow.ShWorkflow;

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
	private ShStageFolderUtils shStageFolderUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShObjectRepository shObjectRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShStagePostUtils shStagePostUtils;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShCacheObject shCacheObject;
	@Autowired
	private ShStageObjectUtils shStageObjectUtils;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShWorkflow shWorkflow;

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectList() throws Exception {
		return shObjectRepository.findAll();
	}

	@GetMapping("/{id}/preview")
	public RedirectView shObjectPreview(@PathVariable String id, HttpServletResponse response,
			RedirectAttributes attributes) throws UnsupportedEncodingException {
		String redirect = null;
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
		if (shObject instanceof ShSite) {
			redirect = shSiteUtils.generatePostLink((ShSite) shObject);
		} else if (shObject instanceof ShPost) {
			redirect = shStagePostUtils.generatePostLink((ShPost) shObject);
		} else if (shObject instanceof ShFolder) {
			redirect = shStageFolderUtils.generateFolderLink((ShFolder) shObject);
		}

		RedirectView redirectView = new RedirectView(new String(redirect.getBytes("UTF-8"), "ISO-8859-1"));
		redirectView.setHttp10Compatible(false);
		return redirectView;
	}

	@GetMapping("{id}/clear-cache")
	public ShObject shObjectClearCache(@PathVariable String id, HttpServletResponse response) {
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
		shCacheObject.deleteCache(id);
		return shObject;
	}

	@GetMapping("{id}/request-workflow/{publishStatus}")
	public ShObject shObjectRequestWorkflow(@PathVariable String id, @PathVariable String publishStatus,
			Principal principal) {
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
		shWorkflow.requestWorkFlow(shObject, principal);
		return shObject;
	}

	@PutMapping("/moveto/{globallIdDest}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectMoveTo(@PathVariable String globallIdDest, @RequestBody List<String> globalIds) {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		ShObject shObjectDest = shObjectRepository.findById(globallIdDest).orElse(null);
		for (String globalId : globalIds) {
			shCacheObject.deleteCache(globalId);
			ShObject shObject = shObjectRepository.findById(globalId).orElse(null);
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

		shCacheObject.deleteCache(globallIdDest);
		return shObjects;
	}

	@PutMapping("/copyto/{globallIdDest}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectCopyTo(@PathVariable String globallIdDest, @RequestBody List<String> globalIds) {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		for (String globalId : globalIds) {
			ShObject shObject = shObjectRepository.findById(globalId).orElse(null);
			ShObject shObjectDest = shObjectRepository.findById(globallIdDest).orElse(null);
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
		shCacheObject.deleteCache(globallIdDest);
		return shObjects;
	}

	@GetMapping("/{id}/list")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shObjectListItem(@PathVariable String id, final Principal principal) {
		Optional<ShObject> shObjectOptional = shObjectRepository.findById(id);
		if (shObjectOptional.isPresent()) {
			ShUser shUser = null;
			if (principal != null)
				shUser = shUserRepository.findByUsername(principal.getName());
			ShObject shObject = shObjectOptional.get();
			if (shObject instanceof ShFolder) {

				ShFolder shFolder = (ShFolder) shObject;
				String folderPath = shFolderUtils.folderPath(shFolder, true, false);
				ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
				ShSite shSite = breadcrumb.get(0).getShSite();
				ShFolderList shFolderList = new ShFolderList();
				shFolderList.setShFolders(allowedFolders(shUser, shObject));
				shFolderList.setShPosts(allowedPosts(shUser, shObject));
				shFolderList.setFolderPath(folderPath);
				shFolderList.setBreadcrumb(breadcrumb);
				shFolderList.setShSite(shSite);

				return shFolderList;
			} else if (shObject instanceof ShSite) {
				ShSite shSite = (ShSite) shObject;
				ShFolderList shFolderList = new ShFolderList();
				shFolderList.setShFolders(allowedFolders(shUser, shObject));
				shFolderList.setShSite(shSite);
				return shFolderList;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	private Set<ShFolderTinyBean> allowedFolders(ShUser shUser, ShObject shObject) {

		Set<ShFolderTinyBean> folders = new HashSet<>();
		if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
			folders = shFolderRepository.findByParentFolderTiny(shFolder);
		} else if (shObject instanceof ShSite) {
			ShSite shSite = (ShSite) shObject;
			folders = shFolderRepository.findByShSiteAndRootFolderTiny(shSite, (byte) 1);
		}

		Set<ShGroup> shGroups = new HashSet<>();
		if (shUser != null && shUser.getShGroups() != null) {
			Set<ShFolderTinyBean> shFolders = new HashSet<>();
			shGroups = shUser.getShGroups();

			for (ShFolderTinyBean folder : folders)
				if (shObjectRepository.countByIdAndShGroupsInOrIdAndShGroupsIsNull(folder.getId(), shGroups,
						folder.getId()) > 0)
					shFolders.add(folder);

			return shFolders;
		} else {
			return folders;
		}

	}

	private List<ShPostTinyBean> allowedPosts(ShUser shUser, ShObject shObject) {

		List<ShPostTinyBean> posts = new ArrayList<>();
		if (shObject instanceof ShFolder) {
			ShFolder shFolder = (ShFolder) shObject;
			posts = shPostRepository.findByShFolderTiny(shFolder.getId());
		}

		Set<ShGroup> shGroups = new HashSet<>();
		if (shUser != null && shUser.getShGroups() != null) {
			List<ShPostTinyBean> shPosts = new ArrayList<>();
			shGroups = shUser.getShGroups();
			for (ShPostTinyBean post : posts)
				if (shObjectRepository.countByIdAndShGroupsInOrIdAndShGroupsIsNull(post.getId(), shGroups,
						post.getId()) > 0)
					shPosts.add(post);
			return shPosts;
		} else {
			return posts;
		}

	}

	@GetMapping("/{id}/list/{postTypeName}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shFolderListByPostType(@PathVariable String id, @PathVariable String postTypeName) {
		Optional<ShObject> shObjectOptional = shObjectRepository.findById(id);
		if (shObjectOptional.isPresent()) {
			ShObject shObject = shObjectOptional.get();
			ShPostType shPostType = shPostTypeRepository.findByName(postTypeName);
			if (shObject instanceof ShFolder) {
				ShFolder shFolder = (ShFolder) shObject;
				String folderPath = shFolderUtils.folderPath(shFolder, true, false);
				ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
				ShSite shSite = breadcrumb.get(0).getShSite();
				ShFolderList shFolderList = new ShFolderList();
				shFolderList.setShFolders(shFolderRepository.findByParentFolderTiny(shFolder));
				String postTypeId = null;
				if (shPostType != null) {
					postTypeId = shPostType.getId();
				}
				shFolderList.setShPosts(shPostRepository.findByShFolderAndShPostTypeTiny(shFolder.getId(), postTypeId));
				shFolderList.setFolderPath(folderPath);
				shFolderList.setBreadcrumb(breadcrumb);
				shFolderList.setShSite(shSite);
				return shFolderList;
			} else if (shObject instanceof ShSite) {
				ShSite shSite = (ShSite) shObject;
				Set<ShFolderTinyBean> shFolders = shFolderRepository.findByShSiteAndRootFolderTiny(shSite, (byte) 1);
				ShFolderList shFolderList = new ShFolderList();
				shFolderList.setShFolders(shFolders);
				shFolderList.setShSite(shSite);
				return shFolderList;
			} else {
				return null;
			}
		} else {
			return null;
		}
	}

	@ApiOperation(value = "Object URL")
	@GetMapping("/{id}/url")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public String shObjectURL(@PathVariable String id) {
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
		String label = "";
		if (shObject instanceof ShPost) {
			label = ((ShPost) shObject).getTitle();
		} else if (shObject instanceof ShFolder) {
			label = ((ShFolder) shObject).getName();
		} else if (shObject instanceof ShSite) {
			label = ((ShSite) shObject).getName();
		}

		return String.format("{ \"url\" : \"%s\", \"label\" : \"%s\"}", shStageObjectUtils.generateObjectLinkById(id),
				label);
	}

	@ApiOperation(value = "Object path")
	@GetMapping("/{id}/path")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderPath shObjectPath(@PathVariable String id) {
		ShObject shObject = shObjectRepository.findById(id).orElse(null);
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
				String folderPath = shFolderUtils.folderPath(shFolder, true, false);
				ArrayList<ShFolder> breadcrumb = shFolderUtils.breadcrumb(shFolder);
				ShSite shSite = breadcrumb.get(0).getShSite();
				shFolderPath.setFolderPath(folderPath);
				shFolderPath.setCurrentFolder(shFolder);
				shFolderPath.setBreadcrumb(breadcrumb);
				shFolderPath.setShSite(shSite);
				return shFolderPath;
			}
		} else if (shObject instanceof ShPost) {
			ShPost shPost = shPostUtils.loadLazyPost(shObject.getId(), false);
			if (shPost != null) {
				ShFolder shFolder = shPost.getShFolder();
				ShFolderPath shFolderPath = new ShFolderPath();
				String folderPath = shFolderUtils.folderPath(shFolder, true, false);
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
	public Map<String, Integer> shObjectSort(@RequestBody Map<String, Integer> objectOrder) {
		boolean first = true;
		String parentFolderId = null;

		for (Entry<String, Integer> objectOrderItem : objectOrder.entrySet()) {
			int shObjectOrder = objectOrderItem.getValue();
			String shObjectId = objectOrderItem.getKey();
			Optional<ShObject> shObjectOptional = shObjectRepository.findById(shObjectId);
			if (shObjectOptional.isPresent()) {
				ShObject shObject = shObjectOptional.get();
				shObject.setPosition(shObjectOrder);
				shObjectRepository.save(shObject);
				if (first) {
					first = false;
					ShFolder shFolder = shFolderUtils.getParentFolder(shObject);
					if (shFolder != null)
						parentFolderId = shFolder.getId();
				}
			}
		}

		if (parentFolderId != null)
			shCacheObject.deleteCache(parentFolderId);

		return objectOrder;

	}
}
