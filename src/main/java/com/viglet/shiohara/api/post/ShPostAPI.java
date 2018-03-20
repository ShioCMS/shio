package com.viglet.shiohara.api.post;

import java.io.File;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;
import java.util.Set;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.reference.ShReference;
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.SystemObjectView;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
import com.viglet.shiohara.utils.ShPostUtils;
import com.viglet.shiohara.utils.ShStaticFileUtils;

@Component
@Path("/post")
public class ShPostAPI {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShUserRepository shUserRepository;
	@Autowired
	private ShStaticFileUtils shStaticFileUtils;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	@Autowired
	private ShReferenceRepository shReferenceRepository;
	@Autowired
	private ShPostUtils shPostUtils;

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShPost> list() throws Exception {
		return shPostRepository.findAll();
	}

	@Path("/{postId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public ShPost edit(@PathParam("postId") UUID id) throws Exception {
		return shPostRepository.findById(id);
	}

	@Path("/moveto/{channeGloballId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShPost> moveTo(@PathParam("channeGloballId") UUID channeGloballId, List<UUID> postGlobalIds)
			throws Exception {
		List<ShPost> shPosts = new ArrayList<ShPost>();
		for (UUID postGlobalId : postGlobalIds) {
			ShGlobalId shPostGlobalId = shGlobalIdRepository.findById(postGlobalId);
			ShGlobalId shChannelGlobalId = shGlobalIdRepository.findById(channeGloballId);
			ShPost shPost = (ShPost) shPostGlobalId.getShObject();
			ShChannel shChannel = (ShChannel) shChannelGlobalId.getShObject();
			shPost.setShChannel(shChannel);
			shPostRepository.save(shPost);
			shPosts.add(shPost);
		}
		return shPosts;
	}

	@Path("/copyto/{channeGloballId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public List<ShPost> copyTo(@PathParam("channeGloballId") UUID channeGloballId, List<UUID> postGlobalIds)
			throws Exception {
		List<ShPost> shPosts = new ArrayList<ShPost>();
		for (UUID postGlobalId : postGlobalIds) {
			ShGlobalId shPostGlobalId = shGlobalIdRepository.findById(postGlobalId);
			ShGlobalId shChannelGlobalId = shGlobalIdRepository.findById(channeGloballId);
			ShPost shPost = (ShPost) shPostGlobalId.getShObject();
			ShChannel shChannel = (ShChannel) shChannelGlobalId.getShObject();			
			shPosts.add(shPostUtils.clone(shPost, shChannel));
		}
		return shPosts;
	}
	
	@Path("/{postId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public ShPost update(@PathParam("postId") UUID id, ShPost shPost) throws Exception {

		ShPost shPostEdit = shPostRepository.findById(id);

		String title = shPostEdit.getTitle();
		String summary = shPostEdit.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {

			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			ShPostAttr shPostAttrEdit = shPostAttrRepository.findById(shPostAttr.getId());
			this.referencedFile(shPostAttrEdit, shPostAttr, shPost);

			if (shPostAttrEdit != null) {
				shPostAttrEdit.setDateValue(shPostAttr.getDateValue());
				shPostAttrEdit.setIntValue(shPostAttr.getIntValue());
				shPostAttrEdit.setStrValue(shPostAttr.getStrValue());
				shPostAttrEdit.setReferenceObjects(shPostAttr.getReferenceObjects());
				shPostAttrRepository.saveAndFlush(shPostAttrEdit);
			}
		}
		shPostEdit = shPostRepository.findById(id);

		shPostEdit.setDate(new Date());
		shPostEdit.setTitle(title);
		shPostEdit.setSummary(summary);

		shPostRepository.saveAndFlush(shPostEdit);

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPostEdit.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		return shPostEdit;
	}

	@Path("/{postId}")
	@DELETE
	@Produces(MediaType.APPLICATION_JSON)
	public boolean delete(@PathParam("postId") UUID id) throws Exception {

		ShPost shPost = shPostRepository.findById(id);

		if (shPost.getShPostType().getName().equals("PT-FILE") && shPost.getShPostAttrs().size() > 0) {
			File file = shStaticFileUtils.filePath(shPost.getShChannel(), shPost.getShPostAttrs().get(0).getStrValue());
			if (file != null) {
				if (file.exists()) {
					file.delete();
				}
			}
		}
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostAttrRepository.delete(shPostAttr.getId());
		}

		for (ShReference shReference : shReferenceRepository.findByShGlobalFromId(shPost.getShGlobalId())) {
			shReferenceRepository.delete(shReference.getId());
		}
		for (ShReference shReference : shReferenceRepository.findByShGlobalToId(shPost.getShGlobalId())) {
			shReferenceRepository.delete(shReference.getId());
		}

		shGlobalIdRepository.delete(shPost.getShGlobalId().getId());

		shPostRepository.delete(id);

		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	@JsonView({ SystemObjectView.ShObject.class })
	public ShPost add(ShPost shPost) throws Exception {

		String title = shPost.getTitle();
		String summary = shPost.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr != null) {
				shPostAttr.setReferenceObjects(null);

			}
		}
		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);

		shPostRepository.saveAndFlush(shPost);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPost);
		shGlobalId.setType("POST");

		shGlobalIdRepository.saveAndFlush(shGlobalId);

		ShPost shPostWithGlobalId = shPostRepository.findById(shPost.getId());

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostAttr.setShPost(shPost);
			this.referencedFile(shPostAttr, shPostAttr, shPostWithGlobalId);
			shPostAttrRepository.saveAndFlush(shPostAttr);
		}

		shPostRepository.saveAndFlush(shPost);

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		return shPost;

	}

	public void referencedFile(ShPostAttr shPostAttrEdit, ShPostAttr shPostAttr, ShPost shPost) {
		if (shPostAttrEdit.getShPostTypeAttr().getName().equals("FILE")) {

			if (shPost.getShPostType().getName().equals("PT-FILE")) {
				File fileFrom = shStaticFileUtils.filePath(shPost.getShChannel(), shPostAttrEdit.getStrValue());
				File fileTo = shStaticFileUtils.filePath(shPost.getShChannel(), shPostAttr.getStrValue());
				if (fileFrom != null && fileTo != null) {
					if (fileFrom.exists()) {
						fileFrom.renameTo(fileTo);
					}
				}

			} else {
				if (shPostAttr.getStrValue() == null) {
					shPostAttr.setReferenceObjects(null);
				} else {
					ShPost shPostFile = shPostRepository.findById(UUID.fromString(shPostAttr.getStrValue()));
					// TODO Two or more attributes with FILE Widget and same file, it cannot remove
					// a valid reference
					// Remove old references
					List<ShReference> shOldReferences = shReferenceRepository
							.findByShGlobalFromId(shPost.getShGlobalId());
					if (shOldReferences.size() > 0) {
						// System.out.println("Removing old references");
						for (ShReference shOldReference : shOldReferences) {
							// System.out.println("Old Reference: " +
							// shOldReference.getShGlobalFromId().getId() + ", "
							// + shOldReference.getShGlobalToId().getId());
							if (shPostAttrEdit.getReferenceObjects() != null) {
								for (ShObject shObject : shPostAttrEdit.getReferenceObjects()) {
									// System.out.println("shObject: " +
									// shObject.getShGlobalId().getId().toString());
									if (shOldReference.getShGlobalToId().getId().toString()
											.equals(shObject.getShGlobalId().getId().toString())) {
										shReferenceRepository.delete(shOldReference);
										// System.out.println("Reference removed");
										break;
									}
								}
							}
						}
					}

					// Create new reference
					ShReference shReference = new ShReference();
					shReference.setShGlobalFromId(shPost.getShGlobalId());
					shReference.setShGlobalToId(shPostFile.getShGlobalId());
					shReferenceRepository.saveAndFlush(shReference);

					Set<ShObject> referenceObjects = new HashSet<ShObject>();
					referenceObjects.add(shPostFile);
					shPostAttr.setReferenceObjects(referenceObjects);
				}
			}

		}
	}
}