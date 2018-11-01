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

package com.viglet.shiohara.sites;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.script.ScriptException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.view.RedirectView;

import com.viglet.shiohara.cache.ShCacheManager;
import com.viglet.shiohara.cache.ShCachedObject;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShFormUtils;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShSiteUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Controller
public class ShSitesContext {
	@Resource
	private ApplicationContext applicationContext;
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
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShSitesContextURL shSitesContextURL;
	@Autowired
	private ShFormUtils shFormUtils;

	@PostMapping("/sites/**")
	private RedirectView sitesPostForm(HttpServletRequest request, HttpServletResponse response) throws IOException,
			ScriptException, InstantiationException, IllegalAccessException, ClassNotFoundException {
		shSitesContextURL.init(request, response);

		this.siteContextPost(shSitesContextURL);
		RedirectView redirectView = new RedirectView(
				new String((request.getRequestURL() + "/success").getBytes("UTF-8"), "ISO-8859-1"));
		redirectView.setHttp10Compatible(false);
		return redirectView;
	}

	public byte[] siteContextPost(ShSitesContextURL shSitesContextURL) throws IOException, ScriptException,
			InstantiationException, IllegalAccessException, ClassNotFoundException {
		
		shFormUtils.execute(shSitesContextURL);
		
		this.sitesFullGeneric(shSitesContextURL.getRequest(), shSitesContextURL.getResponse());

		return null;
	}

	@RequestMapping("/sites/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response)
			throws IOException, ScriptException {

		final int cacheMinutes = 1;

		shSitesContextURL.init(request, response);
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
						response.setCharacterEncoding("UTF-8");
					}
					response.getOutputStream().write((byte[]) shCacheObject.getObject());
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
			response.setCharacterEncoding("UTF-8");
			response.getWriter().write(shPageLayoutHTML);

			return shPageLayoutHTML.getBytes();
		}

	}
}
