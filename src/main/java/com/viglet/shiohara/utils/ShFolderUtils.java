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

package com.viglet.shiohara.utils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Set;

import org.apache.http.client.ClientProtocolException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.turing.ShTuringIntegration;
import com.viglet.shiohara.url.ShURLScheme;

@Component
public class ShFolderUtils {
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShURLScheme shURLScheme;
	@Autowired
	private ShTuringIntegration shTuringIntegration;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;

	public ShPost getFolderIndex(ShFolder shFolder) {
		ShPostType shPostType = shPostTypeRepository.findByName(ShSystemPostType.FOLDER_INDEX);
		List<ShPost> shFolderIndexPosts = shPostRepository.findByShFolderAndShPostTypeOrderByPositionAsc(shFolder,
				shPostType);
		if (shFolderIndexPosts.size() > 0) {
			ShPost shFolderIndexPost = shFolderIndexPosts.get(0);
			return shFolderIndexPost;
		}
		return null;
	}

	public JSONObject toJSON(ShFolder shFolder) {
		JSONObject shFolderItemAttrs = new JSONObject();

		JSONObject shFolderItemSystemAttrs = new JSONObject();
		shFolderItemSystemAttrs.put("id", shFolder.getId());
		shFolderItemSystemAttrs.put("title", shFolder.getName());
		shFolderItemSystemAttrs.put("link", this.folderPath(shFolder, true));

		shFolderItemAttrs.put("system", shFolderItemSystemAttrs);

		return shFolderItemAttrs;
	}

	public ArrayList<ShFolder> breadcrumb(ShFolder shFolder) {
		if (shFolder != null) {
			boolean rootFolder = false;
			ArrayList<ShFolder> folderBreadcrumb = new ArrayList<ShFolder>();
			folderBreadcrumb.add(shFolder);
			ShFolder parentFolder = shFolder.getParentFolder();
			while (parentFolder != null && !rootFolder) {
				folderBreadcrumb.add(parentFolder);
				if ((parentFolder.getRootFolder() == (byte) 1) || (parentFolder.getParentFolder() == null)) {
					rootFolder = true;
				} else {
					parentFolder = parentFolder.getParentFolder();
				}
			}

			Collections.reverse(folderBreadcrumb);
			return folderBreadcrumb;
		} else {
			return null;
		}
	}

	public String folderPath(ShFolder shFolder, boolean usingFurl) {
		return this.folderPath(shFolder, "/", usingFurl);
	}

	public String folderPath(ShFolder shFolder, String separator, boolean usingFurl) {
		if (shFolder != null) {
			boolean rootFolder = false;
			ArrayList<String> pathContexts = new ArrayList<String>();
			if (!(shFolder.getFurl().equals("home") && shFolder.getRootFolder() == (byte) 1)) {
				pathContexts.add(shFolder.getFurl());
			}
			ShFolder parentFolder = shFolder.getParentFolder();
			while (parentFolder != null && !rootFolder) {

				if ((parentFolder.getRootFolder() == (byte) 1) || (parentFolder.getParentFolder() == null)) {
					rootFolder = true;
					if (!parentFolder.getName().toLowerCase().equals("home")) {
						if (usingFurl) {
							pathContexts.add(parentFolder.getFurl());
						} else {
							pathContexts.add(parentFolder.getName());
						}
					}
				} else {
					if (usingFurl) {
						pathContexts.add(parentFolder.getFurl());
					} else {
						pathContexts.add(parentFolder.getName());
					}
					parentFolder = parentFolder.getParentFolder();
				}
			}

			String path = "";

			for (String context : pathContexts) {
				path = context + separator + path;
			}
			path = separator + path;
			return path;
		} else {
			return separator;
		}

	}

	public String directoryPath(ShFolder shFolder, String separator) {
		if (shFolder != null) {
			boolean rootFolder = false;
			ArrayList<String> pathContexts = new ArrayList<String>();
			pathContexts.add(shFolder.getName());
			ShFolder parentFolder = shFolder.getParentFolder();
			while (parentFolder != null && !rootFolder) {
				pathContexts.add(parentFolder.getName());
				if ((parentFolder.getRootFolder() == (byte) 1) || (parentFolder.getParentFolder() == null)) {
					rootFolder = true;
				} else {
					parentFolder = parentFolder.getParentFolder();
				}
			}

			String path = "";

			for (String context : pathContexts) {
				path = context + separator + path;
			}
			path = separator + path;
			return path;
		} else {
			return separator;
		}

	}

	public ShSite getSite(ShFolder shFolder) {
		ShSite shSite = null;
		if (shFolder != null) {
			boolean rootFolder = false;
			if ((shFolder.getRootFolder() == (byte) 1) || (shFolder.getParentFolder() == null)) {
				shSite = shFolder.getShSite();
			} else {
				ShFolder parentFolder = shFolder.getParentFolder();
				while (parentFolder != null && !rootFolder) {
					if ((parentFolder.getRootFolder() == (byte) 1) || (parentFolder.getParentFolder() == null)) {
						rootFolder = true;
						shSite = parentFolder.getShSite();
					} else {
						parentFolder = parentFolder.getParentFolder();
					}
				}
			}
		}
		return shSite;
	}

	public ShFolder folderFromPath(ShSite shSite, String folderPath) {
		return this.folderFromPath(shSite, folderPath, "/");
	}

	public ShFolder folderFromPath(ShSite shSite, String folderPath, String separator) {
		ShFolder currentFolder = null;
		String[] contexts = folderPath.split(separator);
		if (contexts.length == 0) {
			// Root Folder (Home)
			currentFolder = shFolderRepository.findByShSiteAndFurl(shSite, "home");
		} else {
			for (int i = 1; i < contexts.length; i++) {
				if (i == 1) {
					// When is null folder, because is rootFolder and it contains shSite attribute
					currentFolder = shFolderRepository.findByShSiteAndFurl(shSite, contexts[i]);
					if (currentFolder == null) {

						// Is not Root Folder, will try use the Home Folder
						currentFolder = shFolderRepository.findByShSiteAndFurl(shSite, "home");
						// Now will try access the first Folder non Root
						currentFolder = shFolderRepository.findByParentFolderAndFurl(currentFolder, contexts[i]);
					}
				} else {
					currentFolder = shFolderRepository.findByParentFolderAndFurl(currentFolder, contexts[i]);
				}

			}
		}
		return currentFolder;
	}

	public String generateFolderLink(ShFolder shFolder) {
		String link = shURLScheme.get(shFolder).toString();
		link = link + this.folderPath(shFolder, true);
		return link;
	}

	public String generateFolderLinkById(String folderID) {
		ShFolder shFolder = shFolderRepository.findById(folderID).get();
		return this.generateFolderLink(shFolder);
	}

	@Transactional
	public boolean deleteFolder(ShFolder shFolder) throws ClientProtocolException, IOException {
		shTuringIntegration.deindexObject(shFolder);

		for (ShPost shPost : shPostRepository.findByShFolder(shFolder)) {
			// TODO: Check relation and show to user decides.
			List<ShReference> shGlobalFromId = shReferenceRepository.findByShObjectFrom(shPost);
			List<ShReference> shGlobalToId = shReferenceRepository.findByShObjectTo(shPost);
			shReferenceRepository.deleteInBatch(shGlobalFromId);
			shReferenceRepository.deleteInBatch(shGlobalToId);
		}

		for (ShPost shPost : shPostRepository.findByShFolder(shFolder)) {
			Set<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
			shPostAttrRepository.deleteInBatch(shPostAttrs);
		}

		shPostRepository.deleteInBatch(shPostRepository.findByShFolder(shFolder));

		for (ShFolder shFolderChild : shFolderRepository.findByParentFolder(shFolder)) {
			this.deleteFolder(shFolderChild);
		}

		shFolderRepository.delete(shFolder.getId());

		return true;
	}

	public ShFolder copy(ShFolder shFolder, ShObject shObjectDest) {
		// TODO: Copy objects into Folder
		ShFolder shFolderCopy = new ShFolder();
		if (shObjectDest instanceof ShFolder) {
			ShFolder shFolderDest = (ShFolder) shObjectDest;
			shFolderCopy.setParentFolder(shFolderDest);
			shFolderCopy.setShSite(null);
			shFolderCopy.setRootFolder((byte) 0);
		} else if (shObjectDest instanceof ShSite) {
			ShSite shSiteDest = (ShSite) shObjectDest;
			shFolderCopy.setParentFolder(null);
			shFolderCopy.setShSite(shSiteDest);
			shFolderCopy.setRootFolder((byte) 1);
		} else {
			return null;
		}
		shFolderCopy.setDate(new Date());
		shFolderCopy.setName(shFolder.getName());
		shFolderCopy.setFurl(shFolder.getFurl());
		shFolderRepository.save(shFolderCopy);

		return shFolderCopy;
	}

}
