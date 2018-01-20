package com.viglet.shiohara.api.channel;

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
		ShChannelList shChannelList = new ShChannelList();
		shChannelList.setShChannels(shChannelRepository.findByParentChannel(parentChannel));
		shChannelList.setShPosts(shPostRepository.findByShChannel(parentChannel));
		return shChannelList;
	}

}