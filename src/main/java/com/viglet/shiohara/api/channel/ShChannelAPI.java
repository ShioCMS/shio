package com.viglet.shiohara.api.channel;

import java.util.ArrayList;
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

import com.viglet.shiohara.channel.ShChannelUtils;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@Component
@Path("/channel")
public class ShChannelAPI {

	@Autowired
	ShChannelRepository shChannelRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShChannelUtils shChannelUtils;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShChannel> list() throws Exception {
		return shChannelRepository.findAll();
	}

	@Path("/{channelId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannel edit(@PathParam("channelId") int id) throws Exception {
		return shChannelRepository.findById(id);
	}

	@Path("/{channelId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannel update(@PathParam("channelId") int id, ShChannel shChannel) throws Exception {

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
	public boolean delete(@PathParam("channelId") int id) throws Exception {
		shChannelRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShChannel add(ShChannel shChannel) throws Exception {
		shChannelRepository.save(shChannel);
		return shChannel;

	}

	@Path("/{channelId}/list")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannelList list(@PathParam("channelId") int id) throws Exception {
		ShChannel parentChannel = shChannelRepository.findById(id);

		String channelPath = shChannelUtils.channelPath(parentChannel);
		ArrayList<ShChannel> breadcrumb = shChannelUtils.breadcrumb(parentChannel);
		
		ShChannelList shChannelList = new ShChannelList();
		shChannelList.setShChannels(shChannelRepository.findByParentChannel(parentChannel));
		shChannelList.setShPosts(shPostRepository.findByShChannel(parentChannel));
		shChannelList.setChannelPath(channelPath);
		shChannelList.setBreadcrumb(breadcrumb);
		
		return shChannelList;
	}

	@Path("/{channelId}/path")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShChannelPath path(@PathParam("channelId") int id) throws Exception {
		ShChannel shChannel = shChannelRepository.findById(id);
		ShChannelPath shChannelPath = new ShChannelPath();
		String channelPath = shChannelUtils.channelPath(shChannel);
		ArrayList<ShChannel> breadcrumb = shChannelUtils.breadcrumb(shChannel);
		shChannelPath.setChannelPath(channelPath);
		shChannelPath.setCurrentChannel(shChannelUtils.channelFromPath(shChannel.getShSite(), channelPath));
		shChannelPath.setBreadcrumb(breadcrumb);
		return shChannelPath;
	}

}