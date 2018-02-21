package com.viglet.shiohara.api.post;

import java.io.File;
import java.util.Date;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Component
@Path("/post")
public class ShPostAPI {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShPostOutput shPostOutput;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPost> list() throws Exception {
		return shPostRepository.findAll();
	}

	@Path("/{postId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostOutput edit(@PathParam("postId") UUID id) throws Exception {
		return shPostOutput.instance(shPostRepository.findById(id));
	}

	@Path("/{postId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShPost update(@PathParam("postId") UUID id, ShPost shPost) throws Exception {

		ShPost shPostEdit = shPostRepository.findById(id);

		String title = shPostEdit.getTitle();
		String summary = shPostEdit.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {

			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			ShPostAttr shPostAttrEdit = shPostAttrRepository.findById(shPostAttr.getId());

			if (shPost.getShPostType().getName().equals("PT-FILE")
					&& shPostAttrEdit.getShPostTypeAttr().getName().equals("FILE")) {
				File fileFrom = shStaticFileUtils.filePath(shPost.getShChannel(), shPostAttrEdit.getStrValue());
				File fileTo = shStaticFileUtils.filePath(shPost.getShChannel(), shPostAttr.getStrValue());
				if (fileFrom != null && fileTo != null) {
					if (fileFrom.exists()) {
						fileFrom.renameTo(fileTo);
					}
				}
			}

			if (shPostAttrEdit != null) {
				shPostAttrEdit.setDateValue(shPostAttr.getDateValue());
				shPostAttrEdit.setIntValue(shPostAttr.getIntValue());
				shPostAttrEdit.setStrValue(shPostAttr.getStrValue());
				shPostAttrRepository.saveAndFlush(shPostAttrEdit);
			}
		}
		shPostEdit = shPostRepository.findById(id);

		shPostEdit.setDate(new Date());
		shPostEdit.setTitle(title);
		shPostEdit.setSummary(summary);

		shPostRepository.saveAndFlush(shPostEdit);

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPostEdit.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		return shPostEdit;
	}

	@Path("/{postId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postId") UUID id) throws Exception {

		ShPost shPost = shPostRepository.findById(id);

		if (shPost.getShPostType().getName().equals("PT-FILE")) {
			File file = shStaticFileUtils.filePath(shPost.getShChannel(), shPost.getShPostAttrs().get(0).getStrValue());
			if (file != null) {
				if (file.exists()) {
					file.delete();
				}
			}
		}
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostAttrRepository.delete(shPostAttr.getId());
		}

		shPostRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShPost add(ShPost shPost) throws Exception {

		String title = shPost.getTitle();
		String summary = shPost.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);
		}
		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);

		shPostRepository.saveAndFlush(shPost);
		
		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setObjectId(shPost.getId());
		shGlobalId.setType("POST");
		
		shGlobalIdRepository.save(shGlobalId);	

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostAttr.setShPost(shPost);
			shPostAttrRepository.saveAndFlush(shPostAttr);
		}

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		return shPost;

	}

}