package com.viglet.shiohara.api.site;

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

import com.viglet.shiohara.api.channel.ShChannelList;
import com.viglet.shiohara.channel.ShChannelUtils;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
@Path("/site")
public class ShSiteAPI {

	@Autowired
	ShSiteRepository shSiteRepository;
	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShChannelUtils shChannelUtils;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShSite> list() throws Exception {
		return shSiteRepository.findAll();
	}

	@Path("/{siteId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite edit(@PathParam("siteId") int id) throws Exception {
		return shSiteRepository.findById(id);
	}

	@Path("/{siteId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite update(@PathParam("siteId") int id, ShSite shSite) throws Exception {
		ShSite shSiteEdit = shSiteRepository.findById(id);
		shSiteEdit.setName(shSite.getName());
		shSiteRepository.save(shSiteEdit);
		return shSiteEdit;
	}

	@Path("/{siteId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("siteId") int id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id);

		for (ShChannel shChannel : shSite.getShChannels()) {
			shChannelUtils.deleteChannel(shChannel);
		}

		shSiteRepository.delete(id);

		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShSite add(ShSite shSite) throws Exception {
		shSiteRepository.save(shSite);

		// Home Channel
		ShChannel shChannelHome = new ShChannel();
		shChannelHome.setName("Home");
		shChannelHome.setSummary("Home Channel");
		shChannelHome.setParentChannel(null);
		shChannelHome.setShSite(shSite);
		shChannelHome.setDate(new Date());
		shChannelHome.setRootChannel((byte) 1);

		shChannelRepository.save(shChannelHome);

		// Channel Index

		ShPostType shPostChannelIndex = shPostTypeRepository.findByName("PT-CHANNEL-INDEX");

		ShPost shPost = new ShPost();
		shPost.setDate(new Date());
		shPost.setShPostType(shPostChannelIndex);
		shPost.setSummary("Channel Index");
		shPost.setTitle("index");
		shPost.setShChannel(shChannelHome);

		shPostRepository.save(shPost);

		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "title");

		ShPostAttr shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostType(shPostChannelIndex);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setShPostTypeAttrId(1);
		shPostAttr.setStrValue(shPost.getTitle());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);

		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "Description");

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostType(shPostChannelIndex);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setShPostTypeAttrId(2);
		shPostAttr.setStrValue(shPost.getSummary());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);
		return shSite;

	}

	@Path("/{siteId}/channel")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannelList rootChannel(@PathParam("siteId") int id) throws Exception {
		ShSite shSite = shSiteRepository.findById(id);

		ShChannelList shChannelList = new ShChannelList();
		shChannelList.setShChannels(shChannelRepository.findByShSiteAndRootChannel(shSite, (byte) 1));
		shChannelList.setShPosts(shPostRepository.findByShChannel(null));
		shChannelList.setShSite(shSite);
		return shChannelList;

	}

	@Path("/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShSite siteStructure() throws Exception {
		ShSite shSite = new ShSite();
		return shSite;

	}
}