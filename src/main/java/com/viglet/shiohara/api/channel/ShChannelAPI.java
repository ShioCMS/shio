package com.viglet.shiohara.api.channel;

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

@Component
@Path("/channel")
public class ShChannelAPI {

	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShChannelUtils shChannelUtils;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	ShPostTypeAttrRepository shPostTypeAttrRepository;
	
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShChannel> list() throws Exception {
		return shChannelRepository.findAll();
	}

	@Path("/{channelId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannel edit(@PathParam("channelId") UUID id) throws Exception {
		return shChannelRepository.findById(id);
	}

	@Path("/{channelId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannel update(@PathParam("channelId") UUID id, ShChannel shChannel) throws Exception {

		ShChannel shChannelEdit = shChannelRepository.findById(id);

		shChannelEdit.setDate(new Date());
		shChannelEdit.setName(shChannel.getName());
		shChannelEdit.setSummary(shChannel.getName());
		shChannelEdit.setParentChannel(shChannel.getParentChannel());
		shChannelEdit.setShSite(shChannel.getShSite());

		shChannelRepository.saveAndFlush(shChannelEdit);

		return shChannelEdit;
	}

	@Path("/{channelId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("channelId") UUID id) throws Exception {
		ShChannel shChannel = shChannelRepository.findById(id);		
		return shChannelUtils.deleteChannel(shChannel);
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShChannel add(ShChannel shChannel) throws Exception {
		shChannel.setDate(new Date());
		shChannelRepository.save(shChannel);
		
		
		// Channel Index
		
		ShPostType shPostChannelIndex = shPostTypeRepository.findByName("PT-CHANNEL-INDEX");

		ShPost shPost = new ShPost();
		shPost.setDate(new Date());
		shPost.setShPostType(shPostChannelIndex);
		shPost.setSummary("Channel Index");
		shPost.setTitle("index");
		shPost.setShChannel(shChannel);

		shPostRepository.save(shPost);

		ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "TITLE");

		ShPostAttr shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostType(shPostChannelIndex);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getTitle());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);

		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "DESCRIPTION");

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostType(shPostChannelIndex);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue(shPost.getSummary());
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);
		
		shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "PAGE-LAYOUT");

		shPostAttr = new ShPostAttr();
		shPostAttr.setShPost(shPost);
		shPostAttr.setShPostType(shPostChannelIndex);
		shPostAttr.setShPostTypeAttr(shPostTypeAttr);
		shPostAttr.setStrValue("");
		shPostAttr.setType(1);

		shPostAttrRepository.save(shPostAttr);
		return shChannel;

	}

	@Path("/{channelId}/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannelList list(@PathParam("channelId") UUID id) throws Exception {
		ShChannel shChannel = shChannelRepository.findById(id);

		String channelPath = shChannelUtils.channelPath(shChannel);
		ArrayList<ShChannel> breadcrumb = shChannelUtils.breadcrumb(shChannel);
		ShSite shSite = breadcrumb.get(0).getShSite();
		ShChannelList shChannelList = new ShChannelList();
		shChannelList.setShChannels(shChannelRepository.findByParentChannel(shChannel));
		shChannelList.setShPosts(shPostRepository.findByShChannel(shChannel));
		shChannelList.setChannelPath(channelPath);
		shChannelList.setBreadcrumb(breadcrumb);
		shChannelList.setShSite(shSite);
		return shChannelList;
	}

	@Path("/{channelId}/path")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannelPath path(@PathParam("channelId") UUID id) throws Exception {
		ShChannel shChannel = shChannelRepository.findById(id);
		if (shChannel != null) {
			ShChannelPath shChannelPath = new ShChannelPath();
			String channelPath = shChannelUtils.channelPath(shChannel);
			ArrayList<ShChannel> breadcrumb = shChannelUtils.breadcrumb(shChannel);
			ShSite shSite = breadcrumb.get(0).getShSite();
			shChannelPath.setChannelPath(channelPath);
			shChannelPath.setCurrentChannel(shChannelUtils.channelFromPath(shSite, channelPath));
			shChannelPath.setBreadcrumb(breadcrumb);
			shChannelPath.setShSite(shSite);
			return shChannelPath;
		} else {
			return null;
		}
	}
	
	@Path("/model")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannel channelStructure() throws Exception {
		ShChannel shChannel = new ShChannel();
		return shChannel;

	}

}