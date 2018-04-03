package com.viglet.shiohara.api.object;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.fasterxml.jackson.annotation.JsonView;
import com.viglet.shiohara.api.ShJsonView;
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

@RestController
@RequestMapping("/api/v2/object")
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

	@RequestMapping(method = RequestMethod.GET, value = "/{id}/preview")
	public void shObjectPreview(@PathVariable UUID id, HttpServletResponse response) throws Exception {
		String redirect = null;
		ShGlobalId shGlobalId = shGlobalIdRepository.findById(id);
		if (shGlobalId.getType().equals("POST")) {
			ShPost shPost = shPostRepository.findById(shGlobalId.getShObject().getId());
			redirect = shPostUtils.generatePostLink(shPost.getId().toString());
		} else if (shGlobalId.getType().equals("FOLDER")) {
			ShFolder shFolder = shFolderRepository.findById(shGlobalId.getShObject().getId());
			redirect = shFolderUtils.generateFolderLink(shFolder.getId().toString());
		}
		response.sendRedirect(redirect);
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/moveto/{channeGloballId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectMoveTo(@PathVariable UUID channeGloballId, @RequestBody List<UUID> globalIds)
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

	@RequestMapping(method = RequestMethod.PUT, value = "/copyto/{channeGloballId}")
	@JsonView({ ShJsonView.ShJsonViewObject.class })
	public List<ShObject> shObjectCopyTo(@PathVariable UUID channeGloballId, @RequestBody List<UUID> globalIds)
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
