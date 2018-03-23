package com.viglet.shiohara.api.object;

import java.net.URI;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShChannelUtils;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
@Path("/object")
public class ShObjectAPI {

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
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	@Path("/{globalId}/preview")
	@GET
	public Response edit(@PathParam("globalId") UUID id) throws Exception {
		String redirect = null;
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(id);
		if (shGlobalId.getType().equals("POST")) {
			ShPost shPost = shPostRepository.findById(shGlobalId.getShObject().getId());
			redirect = shPostUtils.generatePostLink(shPost.getId().toString());
		} else if (shGlobalId.getType().equals("CHANNEL")) {
			ShChannel shChannel = shChannelRepository.findById(shGlobalId.getShObject().getId());
			redirect = shChannelUtils.generateChannelLink(shChannel.getId().toString());
		}
		  URI targetURIForRedirection = new URI(redirect);
		
		return Response.temporaryRedirect(targetURIForRedirection).build();
	}

}