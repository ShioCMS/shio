/*
 * Copyright (C) 2016-2020 the original author or authors. 
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
package com.viglet.shio.website;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import javax.activation.MimetypesFileTypeMap;
import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.logging.Log;
import org.apache.commons.logging.LogFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.servlet.ModelAndView;

import com.viglet.shio.persistence.model.auth.ShGroup;
import com.viglet.shio.persistence.model.auth.ShUser;
import com.viglet.shio.persistence.model.post.impl.ShPostImpl;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.auth.ShUserRepository;
import com.viglet.shio.persistence.repository.post.ShPostRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;
import com.viglet.shio.utils.ShFormUtils;
import com.viglet.shio.utils.ShStaticFileUtils;
import com.viglet.shio.utils.ShUserUtils;
import com.viglet.shio.website.cache.component.ShCachePage;
import com.viglet.shio.website.cache.component.ShCachePageBean;
import com.viglet.shio.website.utils.ShSitesObjectUtils;

/**
 * 
 * Site Context
 * 
 * @author Alexandre Oliveira
 * 
 */
@Controller
public class ShSitesContext {

	private static final Log logger = LogFactory.getLog(ShSitesContext.class);
	private static final String USERNAME_SESSION = "shUsername";
	private static final String USER_GROUPS_SESSION = "shUserGroups";
	private static final String LOGIN_CALLBACK_SESSION = "shLoginCallBack";
	private static final String LOGIN_PAGE = "/login-page";
	@Resource
	private ApplicationContext applicationContext;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShSitesContextURLProcess shSitesContextURLProcess;
	@Autowired
	private ShFormUtils shFormUtils;
	@Autowired
	private ShSitesObjectUtils shSitesObjectUtils;
	@Autowired
	private ShCachePage shCachePage;
	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShUserUtils shUserUtils;

	@PostMapping("/sites/**")
	private ModelAndView sitesPostForm(HttpServletRequest request, HttpServletResponse response) {
		ShSitesContextURL shSitesContextURL = shSitesContextURLProcess.getContextURL(request, response);

		shFormUtils.execute(shSitesContextURL);

		Optional<ShSite> shSite = shSiteRepository.findById(shSitesContextURL.getInfo().getSiteId());
		if (shSite.isPresent()) {
			String successUrl = shSitesObjectUtils.generateObjectLinkById(shSite.get().getFormSuccess());

			return new ModelAndView("redirect:" + successUrl);
		}
		try {
			response.sendError(HttpServletResponse.SC_NOT_FOUND);
		} catch (IOException e) {
			logger.error(e);
		}
		return null;
	}

	@RequestMapping("/sites/**")
	private void sitesFullGeneric(HttpServletRequest request, HttpServletResponse response, HttpSession session) {

		ShSitesContextURL shSitesContextURL = shSitesContextURLProcess.getContextURL(request, response);

		boolean showPage = checkIfShowPage(shSitesContextURL, session);

		renderPage(request, response, session, showPage, shSitesContextURL);
	}

	private boolean checkIfShowPage(ShSitesContextURL shSitesContextURL, HttpSession session) {
		return isRestrictPage(shSitesContextURL, session) ? checkIfShowRestrictPage(shSitesContextURL, session)
				: isPublicPage(shSitesContextURL);
	}

	private boolean checkIfShowRestrictPage(ShSitesContextURL shSitesContextURL, HttpSession session) {
		boolean showPage = false;
		String[] groups = (String[]) session.getAttribute(USER_GROUPS_SESSION);
		String[] pageGroups = shSitesContextURL.getInfo().getShPageGroups();

		if (pageGroups != null && pageGroups.length > 0) {
			if (groups.length > 0)
				for (String group : groups)
					if (StringUtils.indexOfAny(group, pageGroups) >= 0)
						showPage = true;

		} else {
			showPage = true;
		}
		return showPage;
	}

	private boolean isRestrictPage(ShSitesContextURL shSitesContextURL, HttpSession session) {
		return (String) session.getAttribute(USERNAME_SESSION) != null
				&& shSitesContextURL.getInfo().isPageAllowRegisterUser();
	}

	private boolean isPublicPage(ShSitesContextURL shSitesContextURL) {
		return shSitesContextURL.getInfo().isPageAllowGuestUser();
	}

	private void renderPage(HttpServletRequest request, HttpServletResponse response, HttpSession session,
			boolean showPage, ShSitesContextURL shSitesContextURL) {

		String username = (String) session.getAttribute(USERNAME_SESSION);
		try {
			if (showPage) {
				if (shSitesContextURL.getInfo().getSiteId() != null)
					this.siteContext(shSitesContextURL);
				else
					response.sendError(HttpServletResponse.SC_NOT_FOUND);

			} else {
				if (username != null) {
					if (shSitesContextURL.getInfo().isPageAllowGuestUser())
						response.sendError(HttpServletResponse.SC_NOT_FOUND);
					else
						response.sendError(HttpServletResponse.SC_FORBIDDEN);
				} else {
					String callback = this.getCurrentUrlFromRequest(request);
					session.setAttribute(LOGIN_CALLBACK_SESSION, callback);
					response.sendRedirect(LOGIN_PAGE);
				}
			}
		} catch (IOException e) {
			logger.error(e);
		}
	}

	@RequestMapping("/login-page")
	private String sitesLoginPage(HttpServletRequest request, HttpServletResponse response) {

		return "login/login";
	}

	@RequestMapping("/logout-page")
	private void sitesLogoutPage(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		session.removeAttribute(USERNAME_SESSION);
		try {
			response.sendRedirect(LOGIN_PAGE);
		} catch (IOException e) {
			logger.error("sitesLogoutPage IOException: ", e);
		}
	}

	@PostMapping("/login-page")
	private void sitesLoginPagePost(HttpServletRequest request, HttpServletResponse response, HttpSession session) {
		try {
			String username = request.getParameter("username");
			String password = request.getParameter("password");

			if (shUserUtils.isValidUserAndPassword(username, password)) {
				ShUser shUser = shUserRepository.findByUsername(username);
				String callback = (String) session.getAttribute(LOGIN_CALLBACK_SESSION);

				List<String> groupList = new ArrayList<>();
				for (ShGroup group : shUser.getShGroups()) {
					groupList.add(group.getName());
				}

				String[] groups = groupList.toArray(new String[groupList.size()]);

				session.setAttribute(USERNAME_SESSION, username);
				session.setAttribute(USER_GROUPS_SESSION, groups);

				if (callback != null)
					response.sendRedirect(callback);
				else
					response.sendRedirect("/");
			} else {
				response.sendRedirect(LOGIN_PAGE);
			}
		} catch (IOException e) {
			logger.error("sitesLoginPagePost IOException: ", e);
		}
	}

	public String getCurrentUrlFromRequest(HttpServletRequest request) {
		String queryString = request.getQueryString();
		return queryString == null ? request.getRequestURL().toString()
				: request.getRequestURL().append('?').append(queryString).toString();
	}

	public void siteContext(ShSitesContextURL shSitesContextURL) {

		if (shSitesContextURL.getInfo().isStaticFile()) {
			this.requestStaticFile(shSitesContextURL);
		} else if (shSitesContextURL.getInfo().getObjectId() != null) {
			this.requestPage(shSitesContextURL);
		} else {
			try {
				shSitesContextURL.getResponse().sendError(HttpServletResponse.SC_NOT_FOUND);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

	private void requestPage(ShSitesContextURL shSitesContextURL) {
		ShCachePageBean shCachePageBean = shCachePage.cache(shSitesContextURL);
		if (shCachePageBean != null) {
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
			if (shCachePageBean.getBody() != null)
				try {
					shSitesContextURL.getResponse().getWriter().write(shCachePageBean.getBody());
				} catch (IOException e) {
					logger.error(e);
				}

		}
	}

	private void requestStaticFile(ShSitesContextURL shSitesContextURL) {
		File staticFile;
		ShPostImpl shPost = shPostRepository.findById(shSitesContextURL.getInfo().getObjectId()).orElse(null);
		staticFile = shStaticFileUtils.filePath(shPost);
		if (staticFile != null && staticFile.exists()) {
			byte[] binaryFile;
			try {
				binaryFile = FileUtils.readFileToByteArray(staticFile);

				MimetypesFileTypeMap mimetypesFileTypeMap = new MimetypesFileTypeMap();
				shSitesContextURL.getResponse().setContentType(mimetypesFileTypeMap.getContentType(staticFile));
				shSitesContextURL.getResponse().getOutputStream().write(binaryFile);
			} catch (IOException e) {
				logger.error(e);
			}
		}
	}

}
