package com.viglet.shiohara.api.object;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.ws.rs.GET;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.SystemObjectView;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.utils.ShFolderUtils;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
@Path("/object")
public class ShObjectAPI {

	@Autowired
	ShFolderRepository shFolderRepository;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShFolderUtils shFolderUtils;
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
		} else if (shGlobalId.getType().equals("FOLDER")) {
			ShFolder shFolder = shFolderRepository.findById(shGlobalId.getShObject().getId());
			redirect = shFolderUtils.generateFolderLink(shFolder.getId().toString());
		}
		URI targetURIForRedirection = new URI(redirect);

		return Response.temporaryRedirect(targetURIForRedirection).build();
	}

	@Path("/moveto/{channeGloballId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShObject> moveTo(@PathParam("channeGloballId") UUID channeGloballId, List<UUID> globalIds)
			throws Exception {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		for (UUID globalId : globalIds) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(globalId);
			ShGlobalId shFolderGlobalId = shGlobalIdRepository.findById(channeGloballId);
			ShFolder shFolderDest = (ShFolder) shFolderGlobalId.getShObject();
			if (shGlobalId.getType().equals("POST")) {
				ShPost shPost = (ShPost) shGlobalId.getShObject();
				shPost.setShFolder(shFolderDest);
				shPostRepository.save(shPost);
				shObjects.add(shPost);
			} else if (shGlobalId.getType().equals("FOLDER")) {
				ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
				shFolder.setParentFolder(shFolderDest);
				shFolderRepository.save(shFolder);
				shObjects.add(shFolder);
			}
		}
		return shObjects;
	}

	@Path("/copyto/{channeGloballId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShObject> copyTo(@PathParam("channeGloballId") UUID channeGloballId, List<UUID> globalIds)
			throws Exception {
		List<ShObject> shObjects = new ArrayList<ShObject>();
		for (UUID globalId : globalIds) {
			ShGlobalId shGlobalId = shGlobalIdRepository.findById(globalId);
			ShGlobalId shFolderGlobalId = shGlobalIdRepository.findById(channeGloballId);
			ShFolder shFolderDest = (ShFolder) shFolderGlobalId.getShObject();
			if (shGlobalId.getType().equals("POST")) {
				ShPost shPost = (ShPost) shGlobalId.getShObject();
				shObjects.add(shPostUtils.copy(shPost, shFolderDest));
			} else if (shGlobalId.getType().equals("FOLDER")) {
				ShFolder shFolder = (ShFolder) shGlobalId.getShObject();
				shObjects.add(shFolderUtils.copy(shFolder, shFolderDest));
			}
		}
		return shObjects;
	}

}
