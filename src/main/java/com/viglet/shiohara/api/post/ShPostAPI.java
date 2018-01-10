package com.viglet.shiohara.api.post;

import java.util.Date;
import java.util.List;

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

import com.viglet.shiohara.persistence.model.ShPost;
import com.viglet.shiohara.persistence.model.ShPostAttr;
import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;

@Component
@Path("/post")
public class ShPostAPI {

	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPost> list() throws Exception {
		return shPostRepository.findAll();
	}

	@Path("/{postId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPost edit(@PathParam("postId") int id) throws Exception {
		return shPostRepository.findById(id);
	}

	@Path("/{postId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShPost update(@PathParam("postId") int id, ShPost shPost) throws Exception {

		ShPost shPostEdit = shPostRepository.findById(id);

		String title = shPostEdit.getTitle();
		String summary = shPostEdit.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = shPostAttr.getStrValue();

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = shPostAttr.getStrValue();

			System.out.println("Attr id: " + shPostAttr.getId());
			ShPostAttr shPostAttrEdit = shPostAttrRepository.findOne(shPostAttr.getId());

			if (shPostAttrEdit != null) {
				System.out.println("Atributo encontrado");
				shPostAttrEdit.setDateValue(shPostAttr.getDateValue());
				shPostAttrEdit.setIntValue(shPostAttr.getIntValue());
				shPostAttrEdit.setStrValue(shPostAttr.getStrValue());

				shPostAttrRepository.saveAndFlush(shPostAttrEdit);
			} else {
				System.out.println("Atributo nao encontrado");
			}
		}
		shPostEdit = shPostRepository.findById(id);
		
		shPostEdit.setDate(new Date());
		shPostEdit.setTitle(title);
		shPostEdit.setSummary(summary);

		shPostRepository.saveAndFlush(shPostEdit);

		return shPostEdit;
	}

	@Path("/{postId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postId") int id) throws Exception {
		shPostRepository.delete(id);
		return true;
	}

	@Path("/{postTypeId}/")
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShPost add(@PathParam("postTypeId") int postTypeId, ShPost shPost) throws Exception {

		ShPostType shPostType = shPostTypeRepository.findById(postTypeId);
		shPost.setShPostType(shPostType);
		shPostRepository.save(shPost);

		return shPost;

	}

}