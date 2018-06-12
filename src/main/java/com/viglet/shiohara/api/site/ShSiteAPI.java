package com.viglet.shiohara.api.site;

import java.security.Principal;
import java.util.Date;
import java.util.List;

import javax.servlet.http.HttpServletResponse;

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
import com.viglet.shiohara.exchange.site.ShSiteExport;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.post.type.ShSystemPostTypeAttr;
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
	private ShPostRepository shPostRepository;
	@Autowired
	private ShFolderUtils shFolderUtils;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShURLFormatter shURLFormatter;
	@Autowired
	private ShSiteExport shSiteExport;
	
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

	@PostMapping
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public ShSite shSiteAdd(@RequestBody ShSite shSite, final Principal principal) throws Exception {

		shSite.setDate(new Date());
		shSite.setOwner(principal.getName());
		shSite.setFurl(shURLFormatter.format(shSite.getName()));

		shSiteRepository.save(shSite);

		// Home Folder
		ShFolder shFolderHome = new ShFolder();
		shFolderHome.setName("Home");
		shFolderHome.setParentFolder(null);
		shFolderHome.setShSite(shSite);
		shFolderHome.setDate(new Date());
		shFolderHome.setRootFolder((byte) 1);
		shFolderHome.setOwner(principal.getName());
		shFolderHome.setFurl(shURLFormatter.format(shFolderHome.getName()));

		shFolderRepository.save(shFolderHome);

		// Folder Index

		ShPostType shPostFolderIndex = shPostTypeRepository.findByName(ShSystemPostType.FOLDER_INDEX);

		ShPost shPost = new ShPost();
		shPost.setDate(new Date());
		shPost.setShPostType(shPostFolderIndex);
		shPost.setSummary("Folder Index");
		shPost.setTitle("index");
		shPost.setShFolder(shFolderHome);
		shPost.setOwner(principal.getName());
		shPost.setFurl(shURLFormatter.format(shPost.getTitle()));

		shPostRepository.save(shPost);

		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostFolderIndex,
				ShSystemPostTypeAttr.TITLE);

		ShPostAttr shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getTitle());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);

		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostFolderIndex,
				ShSystemPostTypeAttr.DESCRIPTION);

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getSummary());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);

		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostFolderIndex,
				ShSystemPostTypeAttr.PAGE_LAYOUT);

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue("");
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);

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
