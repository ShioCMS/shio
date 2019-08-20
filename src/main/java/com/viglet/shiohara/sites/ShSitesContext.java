/*
 * Copyright (C) 2016-2019 Alexandre Oliveira <alexandre.oliveira@viglet.com> 
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
import java.util.Date;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.io.FileUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.sites.cache.component.ShCachePage;
import com.viglet.shiohara.sites.cache.component.ShCachePageBean;
import com.viglet.shiohara.utils.ShFormUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Controller
public class ShSitesContext {
	private static final Log logger = LogFactory.getLog(ShSitesContext.class);
	@Resource
	private ApplicationContext applicationContext;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShSitesContextURLProcess shSitesContextURLProcess;
	@Autowired
	private ShFormUtils shFormUtils;
	@Autowired
	private ShCachePage shCachePage;
	@Autowired
	private ShPostRepository shPostRepository;

	@PostMapping("/sites/**")
	private ModelAndView sitesPostForm(HttpServletRequest request, HttpServletResponse response){
		// TODO: Use Database, need to be cached
		ShSitesContextURL shSitesContextURL = shSitesContextURLProcess.getContextURL(request, response);

		try {
			shFormUtils.execute(shSitesContextURL);
		} catch (InstantiationException | IllegalAccessException | ClassNotFoundException | IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}	
		return new ModelAndView("redirect:" + request.getRequestURL() + "/success");
	}

	@RequestMapping("/sites/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response) throws Exception {

		// TODO: Use Database, need be cached
		ShSitesContextURL shSitesContextURL = shSitesContextURLProcess.getContextURL(request, response);
		if (shSitesContextURL.getInfo().getSiteId() != null)
			this.siteContext(shSitesContextURL);
	}

	public void siteContext(ShSitesContextURL shSitesContextURL) throws Exception {

		File staticFile = null;
		if (shSitesContextURL.getInfo().isStaticFile()) {
			ShPost shPost = shPostRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
			staticFile = shStaticFileUtils.filePath(shPost);
			if (staticFile != null && staticFile.exists()) {
				byte[] binaryFile = FileUtils.readFileToByteArray(staticFile);
				MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
				shSitesContextURL.getResponse().setContentType(mimetypesFileTypeMap.getContentType(staticFile));
				shSitesContextURL.getResponse().getOutputStream().write(binaryFile);
			}
		} else if (shSitesContextURL.getInfo().getObjectId() != null) {
			ShCachePageBean shCachePageBean = shCachePage.cache(shSitesContextURL);
			if (shCachePageBean.getExpirationDate() != null
					&& shCachePageBean.getExpirationDate().compareTo(new Date()) < 0) {
				shCachePage.deleteCache(shSitesContextURL.getInfo().getObjectId(),
						shSitesContextURL.getInfo().getContextURLOriginal());
				shCachePageBean = shCachePage.cache(shSitesContextURL);
				if (logger.isDebugEnabled()) {
					logger.debug(String.format("Expired Cache for id %s and URL %s",
							shSitesContextURL.getInfo().getObjectId(),
							shSitesContextURL.getInfo().getContextURLOriginal()));
				}

			}
			shSitesContextURL.getResponse().setContentType(shCachePageBean.getContentType());
			shSitesContextURL.getResponse().setCharacterEncoding("UTF-8");
			if (shCachePageBean != null && shCachePageBean.getBody() != null) {
				shSitesContextURL.getResponse().getWriter().write(shCachePageBean.getBody());
			}
		} else
			shSitesContextURL.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
	}

}
