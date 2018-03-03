package com.viglet.shiohara.api.post;

import java.io.File;
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
import com.viglet.shiohara.persistence.model.reference.ShReferenceId;
import com.viglet.shiohara.persistence.model.user.ShUser;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.object.ShObject;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.reference.ShReferenceRepository;
import com.viglet.shiohara.persistence.repository.user.ShUserRepository;
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

	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public List<ShPost> list() throws Exception {
		return shPostRepository.findAll();
	}

	@Path("/{postId}")
	@GET
	@Produces(MediaType.APPLICATION_JSON)
	public ShPost edit(@PathParam("postId") UUID id) throws Exception {
		return shPostRepository.findById(id);
	}

	@Path("/{postId}")
	@PUT
	@Produces(MediaType.APPLICATION_JSON)
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

						// TODO Need remove old reference
						ShReferenceId shReferenceId = new ShReferenceId();
						shReferenceId.setFromId(shPost.getShGlobalId().getId());
						shReferenceId.setToId(shPostFile.getShGlobalId().getId());
						ShReference shReference = new ShReference();
						shReference.setId(shReferenceId);

						shReferenceRepository.saveAndFlush(shReference);
						Set<ShObject> referenceObjects = new HashSet<ShObject>();
						referenceObjects.add(shPostFile);
						shPostAttr.setReferenceObjects(referenceObjects);
					}
				}

			}

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

		shPostRepository.delete(id);
		return true;
	}

	@POST
	@Consumes(MediaType.APPLICATION_JSON)
	public ShPost add(ShPost shPost) throws Exception {

		String title = shPost.getTitle();
		String summary = shPost.getSummary();

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getIsTitle() == 1)
				title = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);

			if (shPostAttr.getShPostTypeAttr().getIsSummary() == 1)
				summary = StringUtils.abbreviate(shPostAttr.getStrValue(), 255);
		}
		shPost.setDate(new Date());
		shPost.setTitle(title);
		shPost.setSummary(summary);

		shPostRepository.saveAndFlush(shPost);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPost);
		shGlobalId.setType("POST");

		shGlobalIdRepository.saveAndFlush(shGlobalId);

		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			shPostAttr.setShPost(shPost);
			if (shPostAttr.getShPostTypeAttr().getShWidget().getName().equals("File")
					&& !shPost.getShPostType().getName().equals("PT-FILE")) {
				ShPost shPostFile = shPostRepository.findById(UUID.fromString(shPostAttr.getStrValue()));
				ShReferenceId shReferenceId = new ShReferenceId();
				shReferenceId.setFromId(shGlobalId.getId());
				shReferenceId.setToId(shPostFile.getShGlobalId().getId());
				ShReference shReference = new ShReference();
				shReference.setId(shReferenceId);

				shReferenceRepository.saveAndFlush(shReference);
				Set<ShObject> referenceObjects = new HashSet<ShObject>();
				referenceObjects.add(shPostFile);
				shPostAttr.setReferenceObjects(referenceObjects);

			}
			shPostAttrRepository.saveAndFlush(shPostAttr);
		}

		ShUser shUser = shUserRepository.findById(1);
		shUser.setLastPostType(String.valueOf(shPost.getShPostType().getId()));
		shUserRepository.saveAndFlush(shUser);

		return shPost;

	}

}