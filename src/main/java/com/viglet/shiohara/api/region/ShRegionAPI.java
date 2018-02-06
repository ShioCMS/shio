package com.viglet.shiohara.api.region;

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

import com.viglet.shiohara.persistence.model.region.ShRegion;
import com.viglet.shiohara.persistence.repository.region.ShRegionRepository;

@Component
@Path("/region")
public class ShRegionAPI {
	
	@Autowired
	ShRegionRepository shRegionRepository;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShRegion> list() throws Exception {
		return shRegionRepository.findAll();
	}

	@Path("/{regionId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShRegion edit(@PathParam("regionId") UUID id) throws Exception {
		return shRegionRepository.findById(id);
	}

	@Path("/{regionId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShRegion update(@PathParam("regionId") UUID id, ShRegion shRegion) throws Exception {
		ShRegion shRegionEdit = shRegionRepository.findById(id);
		shRegionEdit.setName(shRegion.getName());
		shRegionEdit.setShPost(shRegion.getShPost());
		shRegionEdit.setShpostType(shRegion.getShPostType());		
		shRegionRepository.save(shRegionEdit);
		return shRegionEdit;
	}

	@Path("/{regionId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("regionId") UUID id) throws Exception {
		shRegionRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShRegion add(ShRegion shRegion) throws Exception {
		shRegionRepository.save(shRegion);
		return shRegion;

	}

}