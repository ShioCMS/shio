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
package com.viglet.shio.api.site;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
import org.apache.http.client.ClientProtocolException;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
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
import com.viglet.shio.api.ShJsonView;
import com.viglet.shio.api.folder.ShFolderList;
import com.viglet.shio.bean.ShFolderTinyBean;
import com.viglet.shio.exchange.ShCloneExchange;
import com.viglet.shio.exchange.ShExchange;
import com.viglet.shio.exchange.ShSiteExchange;
import com.viglet.shio.exchange.site.ShSiteExport;
import com.viglet.shio.persistence.model.folder.ShFolder;
import com.viglet.shio.persistence.model.site.ShSite;
import com.viglet.shio.persistence.repository.folder.ShFolderRepository;
import com.viglet.shio.persistence.repository.site.ShSiteRepository;
import com.viglet.shio.url.ShURLFormatter;
import com.viglet.shio.utils.ShFolderUtils;
import com.viglet.shio.utils.ShHistoryUtils;

import io.swagger.annotations.Api;

/**
 * @author Alexandre Oliveira
 */
@RestController
@RequestMapping("/api/v2/site")
@Api(tags = "Site", description = "Site API")
public class ShSiteAPI {
	static final Logger logger = LogManager.getLogger(ShSiteAPI.class);
	
	@Autowired
	private ShSiteRepository shSiteRepository;
	@Autowired
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShSiteExport shSiteExport;
	@Autowired
	private ShCloneExchange shCloneExchange;
	@Autowired
	private ShHistoryUtils shHistoryUtils;
	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShSite> shSiteList(final Principal principal) throws Exception {
		if (principal != null) {
			return shSiteRepository.findByOwnerOrOwnerIsNull(principal.getName());
		} else {
			return shSiteRepository.findAll();
		}
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteEdit(@PathVariable String id) throws Exception {
		return shSiteRepository.findById(id).orElse(null);
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteUpdate(@PathVariable String id, @RequestBody ShSite shSite, Principal principal) {
		Optional<ShSite> shSiteOptional = shSiteRepository.findById(id);
		if (shSiteOptional.isPresent()) {
			ShSite shSiteEdit = shSiteOptional.get();
			shSiteEdit.setDate(new Date());
			shSiteEdit.setName(shSite.getName());
			shSiteEdit.setPostTypeLayout(shSite.getPostTypeLayout());
			shSiteEdit.setSearchablePostTypes(shSite.getSearchablePostTypes());
			shSiteEdit.setFormSuccess(shSite.getFormSuccess());
			shSiteEdit.setFurl(shURLFormatter.format(shSite.getName()));
			shSiteRepository.save(shSiteEdit);
			
			shHistoryUtils.commit(shSite, principal, ShHistoryUtils.UPDATE);
			
			return shSiteEdit;
		}

		return null;

	}

	@DeleteMapping("/{id}")
	@Transactional
	public boolean shSiteDelete(@PathVariable String id, Principal principal) {
		ShSite shSite = shSiteRepository.findById(id).orElse(null);

		Set<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);

		for (ShFolder shFolder : shFolders) {
			try {
				shFolderUtils.deleteFolder(shFolder);
			} catch (ClientProtocolException e) {
				logger.error("shSiteDelete ClientProtocolException: ", e);
			} catch (IOException e) {
				logger.error("shSiteDelete IOException: ", e);			
			}
		}
		
		shSiteRepository.delete(id);

		shHistoryUtils.commit(shSite, principal, ShHistoryUtils.DELETE);
		
		return true;
	}

	public ShExchange importTemplateSite(ShSite shSite) throws IOException, IllegalStateException, ArchiveException {

		URL templateSiteRepository = new URL("https://github.com/ShioCMS/bootstrap-site/archive/0.3.5.zip");

		File userDir = new File(System.getProperty("user.dir"));
		if (userDir.exists() && userDir.isDirectory()) {
			File tmpDir = new File(userDir.getAbsolutePath().concat(File.separator + "store" + File.separator + "tmp"));
			if (!tmpDir.exists()) {
				tmpDir.mkdirs();
			}

			File templateSiteFile = new File(
					tmpDir.getAbsolutePath().concat(File.separator + "template-site-" + UUID.randomUUID() + ".zip"));

			FileUtils.copyURLToFile(templateSiteRepository, templateSiteFile);

			ShExchange shExchange = shCloneExchange.cloneFromFile(templateSiteFile, "admin", shSite);

			FileUtils.deleteQuietly(templateSiteFile);

			return shExchange;
		} else {
			return null;
		}

	}

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteAdd(@RequestBody ShSite shSite, final Principal principal) {

		shSite.setDate(new Date());
		shSite.setOwner(principal.getName());
		shSite.setFurl(shURLFormatter.format(shSite.getName()));

		ShExchange shExchange;
		try {
			shExchange = this.importTemplateSite(shSite);
			ShSiteExchange shSiteExchange = shExchange.getSites().get(0);
			shSite.setId(shSiteExchange.getId());
		} catch (IllegalStateException e) {
			logger.error("shSiteAdd IllegalStateException: ", e);
		} catch (IOException e) {
			logger.error("shSiteAdd IOException: ", e);
		} catch (ArchiveException e) {
			logger.error("shSiteAdd ArchiveException: ", e);		
		}
		
		shHistoryUtils.commit(shSite, principal, ShHistoryUtils.CREATE);
		
		return shSite;
	}

	@GetMapping("/{id}/folder")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shSiteRootFolder(@PathVariable String id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id).orElse(null);
		Set<ShFolderTinyBean> shFolders = shFolderRepository.findByShSiteAndRootFolderTiny(shSite, (byte) 1);
		ShFolderList shFolderList = new ShFolderList();
		shFolderList.setShFolders(shFolders);
		shFolderList.setShSite(shSite);
		return shFolderList;

	}

	@ResponseBody
	@GetMapping(value = "/{id}/export", produces = "application/zip")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public StreamingResponseBody shSiteExport(@PathVariable String id, HttpServletResponse response) {

		return shSiteExport.exportObject(id, response);

	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteStructure() throws Exception {
		ShSite shSite = new ShSite();
		return shSite;

	}
}
