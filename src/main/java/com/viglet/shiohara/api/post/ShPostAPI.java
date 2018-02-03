package com.viglet.shiohara.api.post;

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

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;

@Component
@Path("/post")
public class ShPostAPI {

	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShUserRepository shUserRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPost> list() throws Exception {
		return shPostRepository.findAll();
	}

	@Path("/{postId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPost edit(@PathParam("postId") UUID id) throws Exception {
		return shPostRepository.findById(id);
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
				title = shPostAttr.getStrValue();

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = shPostAttr.getStrValue();

			ShPostAttr shPostAttrEdit = shPostAttrRepository.findById(shPostAttr.getId());

			if (shPostAttrEdit != null) {
				shPostAttrEdit.setDateValue(shPostAttr.getDateValue());
				shPostAttrEdit.setIntValue(shPostAttr.getIntValue());
				shPostAttrEdit.setStrValue(shPostAttr.getStrValue());
				shPostAttrRepository.saveAndFlush(shPostAttrEdit);
			} else {
				//
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
				title = shPostAttr.getStrValue();

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = shPostAttr.getStrValue();
		}
		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);

		shPostRepository.saveAndFlush(shPost);

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