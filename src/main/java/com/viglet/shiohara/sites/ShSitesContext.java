package com.viglet.shiohara.sites;

import java.io.File;
import java.io.IOException;
import java.util.Date;
import java.util.Enumeration;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.activation.MimetypesFileTypeMap;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;

import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import com.viglet.shiohara.api.post.ShPostAPI;
import com.viglet.shiohara.cache.ShCacheManager;
import com.viglet.shiohara.cache.ShCachedObject;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Controller
public class ShSitesContext {
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostUtils shPostUtils;
	@Autowired
	private ShSiteUtils shSiteUtils;
	@Autowired
	private ShSitesContextComponent shSitesContextComponent;
	@Autowired
	private ShPostAPI shPostAPI;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShSitesContextURL shSitesContextURL;

	@PostMapping("/sites/**")
	private void sitesPostForm(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ScriptException {
		shSitesContextURL.init(request);
		this.siteContextPost(shSitesContextURL, request, response);
	}

	public byte[] siteContextPost(ShSitesContextURL shSitesContextURL, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ScriptException {
		if (shSitesContextURL.getShObject() instanceof ShFolder
				|| (shSitesContextURL.getShObject() instanceof ShPost && ((ShPost) shSitesContextURL.getShObject())
						.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX))) {
			ShFolder shFolder = null;
			if (shSitesContextURL.getShObject() instanceof ShFolder) {
				shFolder = (ShFolder) shSitesContextURL.getShObject();
			} else {
				shFolder = ((ShPost) shSitesContextURL.getShObject()).getShFolder();
			}
			String shPostTypeName = request.getParameter("__sh-post-type");
			ShPostType shPostType = shPostTypeRepository.findByName(shPostTypeName);

			Enumeration<String> parameters = request.getParameterNames();
			if (shPostTypeName != null) {
				ShPost shPost = new ShPost();
				shPost.setDate(new Date());
				shPost.setOwner("anonymous");
				shPost.setShFolder(shFolder);

				shPost.setShPostType(shPostType);
				Set<ShPostAttr> shPostAttrs = new HashSet<ShPostAttr>();
				while (parameters.hasMoreElements()) {
					String param = parameters.nextElement();
					String paramValue = request.getParameter(param);

					if (param.startsWith("__sh-post-type-attr-")) {
						String attribute = param.replaceFirst("__sh-post-type-attr-", "");

						ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType,
								attribute);

						ShPostAttr shPostAttr = new ShPostAttr();
						shPostAttr.setShPost(shPost);
						shPostAttr.setShPostTypeAttr(shPostTypeAttr);
						shPostAttr.setStrValue(paramValue);

						shPostAttrs.add(shPostAttr);
					}

				}
				shPost.setShPostAttrs(shPostAttrs);
				shPostAPI.postSave(shPost);
			}
		}
		this.sitesFullGeneric(request, response);

		return null;
	}

	@RequestMapping("/sites/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ScriptException {

		final int cacheMinutes = 1;

		shSitesContextURL.init(request);
		if (shSitesContextURL.getShSite() != null) {
			if (!shSitesContextURL.isCacheEnabled()) {
				this.siteContext(shSitesContextURL, request, response);
			} else {
				ShCachedObject shCacheObject = (ShCachedObject) ShCacheManager
						.getCache(shSitesContextURL.getContextURL());
				if (shCacheObject != null) {
					String extension = FilenameUtils.getExtension(shSitesContextURL.getContextURL());

					if (extension.isEmpty() || extension == null) {
						response.setContentType(MediaType.TEXT_HTML_VALUE);
					} else {
						MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
						response.setContentType(mimetypesFileTypeMap.getContentType(shSitesContextURL.getContextURL()));
					}
					response.getOutputStream().write((byte[]) shCacheObject.object);
				} else {
					byte[] html = this.siteContext(shSitesContextURL, request, response);
					if (html != null) {
						shCacheObject = new ShCachedObject(html, shSitesContextURL.getContextURL(), cacheMinutes);
						/* Place the object into the cache! */
						ShCacheManager.putCache(shCacheObject);
					}
				}
			}
		}
	}

	public byte[] siteContext(ShSitesContextURL shSitesContextURL, HttpServletRequest request,
			HttpServletResponse response) throws IOException, ScriptException {
		ShSite shSite = shSitesContextURL.getShSite();
		File staticFile = null;
		if (shSitesContextURL.getShObject() != null && shSitesContextURL.getShObject() instanceof ShPost
				&& ((ShPost) shSitesContextURL.getShObject()).getShPostType().getName().equals(ShSystemPostType.FILE)) {

			ShPost shPost = (ShPost) shSitesContextURL.getShObject();
			staticFile = shStaticFileUtils.filePath(shPost);
			if (staticFile != null && staticFile.exists()) {

				byte[] binaryFile = FileUtils.readFileToByteArray(staticFile);
				MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
				response.setContentType(mimetypesFileTypeMap.getContentType(staticFile));
				response.getOutputStream().write(binaryFile);
				return binaryFile;
			} else
				return null;
		} else {

			String javascriptVar = null;

			String pageLayoutHTML = null;

			String pageLayoutJS = null;

			// Folder
			if (shSitesContextURL.getShObject() instanceof ShFolder) {
				ShFolder shFolderItem = (ShFolder) shSitesContextURL.getShObject();

				Map<String, ShPostAttr> shFolderPageLayoutMap = shSitesContextComponent
						.shFolderPageLayoutMapFactory(shFolderItem, shSite);

				pageLayoutHTML = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
				pageLayoutJS = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

				String shPostThemeId = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
				JSONObject shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

				JSONObject shPostItemAttrs = new JSONObject();

				shPostItemAttrs.put("theme", shThemeAttrs);

				JSONObject shFolderItemAttrs = shFolderUtils.toJSON(shFolderItem);

				shFolderItemAttrs.put("theme", shThemeAttrs);
				shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
				shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
				shFolderItemAttrs.put("post", shPostItemAttrs);
				shFolderItemAttrs.put("site", shSiteUtils.toJSON(shSite));

				javascriptVar = "var shContent = " + shFolderItemAttrs.toString() + ";";
			}
			// Post
			else if (shSitesContextURL.getShObject() instanceof ShPost) {
				ShPost shPostItem = (ShPost) shSitesContextURL.getShObject();

				if (shPostItem.getShPostType().getName().equals(ShSystemPostType.FOLDER_INDEX)) {
					// Folder Index
					ShFolder shFolderItem = null;
					shFolderItem = shSitesContextComponent.shFolderItemFactory(shPostItem);

					Map<String, ShPostAttr> shFolderPageLayoutMap = shSitesContextComponent
							.shFolderPageLayoutMapFactory(shPostItem, shSite);

					pageLayoutHTML = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
					pageLayoutJS = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

					String shPostThemeId = shFolderPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
					JSONObject shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

					JSONObject shPostItemAttrs = new JSONObject();

					shPostItemAttrs.put("theme", shThemeAttrs);

					JSONObject shFolderItemAttrs = shFolderUtils.toJSON(shFolderItem);

					shFolderItemAttrs.put("theme", shThemeAttrs);
					shFolderItemAttrs.put("posts", shSitesContextComponent.shPostItemsFactory(shFolderItem));
					shFolderItemAttrs.put("folders", shSitesContextComponent.shChildFolderItemsFactory(shFolderItem));
					shFolderItemAttrs.put("post", shPostItemAttrs);
					shFolderItemAttrs.put("site", shSiteUtils.toJSON(shSite));

					javascriptVar = "var shContent = " + shFolderItemAttrs.toString() + ";";

				} else {
					// Other Post
					JSONObject postTypeLayout = new JSONObject();

					if (shSite.getPostTypeLayout() != null) {
						postTypeLayout = new JSONObject(shSite.getPostTypeLayout());
					}

					String pageLayoutName = (String) postTypeLayout.get(shPostItem.getShPostType().getName());
					List<ShPost> shPostPageLayouts = shPostRepository.findByTitle(pageLayoutName);

					Map<String, ShPostAttr> shPostPageLayoutMap = null;

					if (shPostPageLayouts != null) {
						for (ShPost shPostPageLayout : shPostPageLayouts) {
							if (shPostUtils.getSite(shPostPageLayout).getId().equals(shSite.getId())) {
								shPostPageLayoutMap = shPostUtils.postToMap(shPostPageLayout);

							}
						}
					}

					if (shPostPageLayoutMap != null) {

						pageLayoutHTML = shPostPageLayoutMap.get(ShSystemPostTypeAttr.HTML).getStrValue();
						pageLayoutJS = shPostPageLayoutMap.get(ShSystemPostTypeAttr.JAVASCRIPT).getStrValue();

						String shPostThemeId = shPostPageLayoutMap.get(ShSystemPostTypeAttr.THEME).getStrValue();
						JSONObject shThemeAttrs = shSitesContextComponent.shThemeFactory(shPostThemeId);

						JSONObject shSiteItemAttrs = shSiteUtils.toJSON(shSite);

						JSONObject shPostItemAttrs = shPostUtils.toJSON(shPostItem);

						shPostItemAttrs.put("theme", shThemeAttrs);
						shPostItemAttrs.put("site", shSiteItemAttrs);

						javascriptVar = "var shContent = " + shPostItemAttrs.toString() + ";";

					}

				}
			}
			String shPageLayoutHTML = shSitesContextComponent.shPageLayoutFactory(javascriptVar, pageLayoutJS,
					pageLayoutHTML, request, shSite);

			response.setContentType(MediaType.TEXT_HTML_VALUE);

			response.getWriter().write(shPageLayoutHTML);

			return shPageLayoutHTML.getBytes();
		}

	}
}
