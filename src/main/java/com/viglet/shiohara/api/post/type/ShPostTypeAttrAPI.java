package com.viglet.shiohara.api.post.type;

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
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;

@Component
@Path("/post/type/attr")
public class ShPostTypeAttrAPI {
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPostTypeAttr> list() throws Exception {
		return shPostTypeAttrRepository.findAll();
	}

	@Path("/{postTypeAttrId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr edit(@PathParam("postTypeAttrId") UUID id) throws Exception {
		return shPostTypeAttrRepository.findById(id);
	}

	@Path("/{postTypeAttrId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr update(@PathParam("postTypeAttrId") UUID id, ShPostTypeAttr shPostTypeAttr) throws Exception {
		ShPostTypeAttr shPostTypeAttrEdit = shPostTypeAttrRepository.findById(id);
		shPostTypeAttrEdit.setIsSummary(shPostTypeAttr.getIsSummary());
		shPostTypeAttrEdit.setIsTitle(shPostTypeAttr.getIsTitle());
		shPostTypeAttrEdit.setLabel(shPostTypeAttr.getLabel());
		shPostTypeAttrEdit.setMany(shPostTypeAttr.getMany());
		shPostTypeAttrEdit.setName(shPostTypeAttr.getName());
		shPostTypeAttrEdit.setOrdinal(shPostTypeAttr.getOrdinal());
		shPostTypeAttrEdit.setRequired(shPostTypeAttr.getRequired());
		shPostTypeAttrEdit.setDescription(shPostTypeAttr.getDescription());
		shPostTypeAttrEdit.setShWidget(shPostTypeAttr.getShWidget());
		shPostTypeAttrRepository.save(shPostTypeAttrEdit);
		return shPostTypeAttrEdit;
	}

	@Path("/{postTypeAttrId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postTypeAttrId") UUID id) throws Exception {
		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findById(id);
		for ( ShPostAttr shPostAttr : shPostTypeAttr.getShPostAttrs()) {
			shPostAttrRepository.delete(shPostAttr.getId());
		}
		shPostTypeAttrRepository.delete(id);
		return true;
	}

	@Deprecated
	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr post(ShPostTypeAttr shPostTypeAttr) throws Exception {
		shPostTypeAttrRepository.save(shPostTypeAttr);
		return shPostTypeAttr;

	}
	
	@Path("/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPostTypeAttr postTypeAttrStructure() throws Exception {
		ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
		return shPostTypeAttr;

	}

}
