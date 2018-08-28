package com.viglet.shiohara.api.site;

import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.security.Principal;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.apache.commons.compress.archivers.ArchiveException;
import org.apache.commons.io.FileUtils;
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
import com.viglet.shiohara.api.ShJsonView;
import com.viglet.shiohara.api.folder.ShFolderList;
import com.viglet.shiohara.exchange.ShCloneExchange;
import com.viglet.shiohara.exchange.ShExchange;
import com.viglet.shiohara.exchange.ShSiteExchange;
import com.viglet.shiohara.exchange.site.ShSiteExport;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.url.ShURLFormatter;
import com.viglet.shiohara.utils.ShFolderUtils;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/site")
@Api(tags = "Site", description = "Site API")
public class ShSiteAPI {

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

	@GetMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShSite> shSiteList(final Principal principal) throws Exception {
		return shSiteRepository.findByOwner(principal.getName());
	}

	@GetMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteEdit(@PathVariable String id) throws Exception {
		return shSiteRepository.findById(id).get();
	}

	@PutMapping("/{id}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteUpdate(@PathVariable String id, @RequestBody ShSite shSite) throws Exception {
		ShSite shSiteEdit = shSiteRepository.findById(id).get();
		shSiteEdit.setDate(new Date());
		shSiteEdit.setName(shSite.getName());
		shSiteEdit.setPostTypeLayout(shSite.getPostTypeLayout());
		shSiteEdit.setSearchablePostTypes(shSite.getSearchablePostTypes());
		shSiteEdit.setFurl(shURLFormatter.format(shSite.getName()));
		shSiteRepository.save(shSiteEdit);
		return shSiteEdit;
	}

	@DeleteMapping("/{id}")
	@Transactional
	public boolean shSiteDelete(@PathVariable String id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id).get();

		List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);

		for (ShFolder shFolder : shFolders) {
			shFolderUtils.deleteFolder(shFolder);
		}

		shSiteRepository.delete(id);

		return true;
	}

	public ShExchange importTemplateSite(ShSite shSite) throws IOException, IllegalStateException, ArchiveException {

		URL templateSiteRepository = new URL("https://github.com/openshio/bootstrap-site/archive/master.zip");

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
	public ShSite shSiteAdd(@RequestBody ShSite shSite, final Principal principal) throws Exception {

		shSite.setDate(new Date());
		shSite.setOwner(principal.getName());
		shSite.setFurl(shURLFormatter.format(shSite.getName()));

		ShExchange shExchange = this.importTemplateSite(shSite);
		ShSiteExchange shSiteExchange = shExchange.getSites().get(0);
		shSite.setId(shSiteExchange.getId());
		
		return shSite;
	}

	@GetMapping("/{id}/folder")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShFolderList shSiteRootFolder(@PathVariable String id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id).get();
		List<ShFolder> shFolders = shFolderRepository.findByShSiteAndRootFolder(shSite, (byte) 1);
		ShFolderList shFolderList = new ShFolderList();
		shFolderList.setShFolders(shFolders);
		shFolderList.setShSite(shSite);
		return shFolderList;

	}

	@ResponseBody
	@GetMapping(value = "/{id}/export", produces = "application/zip")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public StreamingResponseBody shSiteExport(@PathVariable String id, HttpServletResponse response) throws Exception {

		return shSiteExport.exportObject(id, response);

	}

	@GetMapping("/model")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteStructure() throws Exception {
		ShSite shSite = new ShSite();
		return shSite;

	}
}
