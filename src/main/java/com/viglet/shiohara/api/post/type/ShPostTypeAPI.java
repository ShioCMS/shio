package com.viglet.shiohara.api.post.type;

import java.util.ArrayList;
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
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;

@Component
@Path("/post/type")
public class ShPostTypeAPI {
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShPostRepository shPostRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPostType> list() throws Exception {
		return shPostTypeRepository.findAll();
	}

	@Path("/{postTypeId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostType edit(@PathParam("postTypeId") UUID id) throws Exception {
		return shPostTypeRepository.findById(id);
	}

	@Path("/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostType postTypeStructure() throws Exception {
		ShPostType shPostType = new ShPostType();
		return shPostType;

	}

	@Path("/{postTypeId}/post/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPost postStructure(@PathParam("postTypeId") UUID id) throws Exception {
		ShPost shPost = new ShPost();
		shPost.setShPostType(shPostTypeRepository.findById(id));
		List<ShPostAttr> shPostAttrs = new ArrayList<ShPostAttr>();
		for (ShPostTypeAttr shPostTypeAttr : shPost.getShPostType().getShPostTypeAttrs()) {
			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(shPostTypeAttr.getId());
			shPostAttrs.add(shPostAttr);
		}
		shPost.setShPostAttrs(shPostAttrs);
		return shPost;

	}

	@Path("/{postTypeId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostType update(@PathParam("postTypeId") UUID id, ShPostType shPostType) throws Exception {
		ShPostType shPostTypeEdit = shPostTypeRepository.findById(id);

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {

			ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrRepository.findById(shPostTypeAttr.getId());

			if (shPostTypeAttrEdit != null) {
				shPostTypeAttrEdit.setDescription(shPostTypeAttr.getDescription());
				shPostTypeAttrEdit.setIsSummary(shPostTypeAttr.getIsSummary());
				shPostTypeAttrEdit.setIsTitle(shPostTypeAttr.getIsTitle());
				shPostTypeAttrEdit.setLabel(shPostTypeAttr.getLabel());
				shPostTypeAttrEdit.setMany(shPostTypeAttr.getMany());
				shPostTypeAttrEdit.setName(shPostTypeAttr.getName());
				shPostTypeAttrEdit.setOrdinal(shPostTypeAttr.getOrdinal());
				shPostTypeAttrEdit.setRequired(shPostTypeAttr.getRequired());
				shPostTypeAttrEdit.setShWidget(shPostTypeAttr.getShWidget());

				shPostTypeAttrRepository.saveAndFlush(shPostTypeAttrEdit);
			} else {
				if (shPostTypeAttr.getId() == null) {
					shPostTypeAttr.setShPostType(shPostType);
					shPostTypeAttrRepository.saveAndFlush(shPostTypeAttr);
				}
			}
		}

		shPostTypeEdit = shPostTypeRepository.findById(id);
		shPostTypeEdit.setDate(shPostType.getDate());
		shPostTypeEdit.setTitle(shPostType.getTitle());
		shPostTypeEdit.setDescription(shPostType.getDescription());
		shPostTypeEdit.setName(shPostType.getName());

		shPostTypeRepository.saveAndFlush(shPostTypeEdit);
		return shPostTypeEdit;
	}

	@Path("/{postTypeId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postTypeId") UUID id) throws Exception {
		ShPostType shPostType = shPostTypeRepository.findById(id);

		for (ShPostTypeAttr shPostTypeAttr : shPostType.getShPostTypeAttrs()) {
			for (ShPostAttr shPostAttr : shPostTypeAttr.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}
			shPostTypeAttrRepository.delete(shPostTypeAttr.getId());
		}

		for (ShPost shPost : shPostType.getShPosts()) {
			for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
				shPostAttrRepository.delete(shPostAttr.getId());
			}

			shPostRepository.delete(shPost.getId());
		}

		shPostTypeRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShPostType add(ShPostType shPostType) throws Exception {
		shPostTypeRepository.save(shPostType);
		shPostType.setDate(new Date());
		return shPostType;

	}

	@POST
	@Path("/{postTypeId}/attr")
	@Consumes(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr add(@PathParam("postTypeId") UUID id, ShPostTypeAttr shPostTypeAttr) throws Exception {
		ShPostType shPostType = shPostTypeRepository.findById(id);
		if (shPostType != null) {
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttrRepository.save(shPostTypeAttr);
			return shPostTypeAttr;
		} else {
			return null;
		}

	}

}