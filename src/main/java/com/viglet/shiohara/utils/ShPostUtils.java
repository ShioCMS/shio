package com.viglet.shiohara.utils;

import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;

@Component
public class ShPostUtils {
	@Autowired
	ShFolderUtils shFolderUtils;
	@Autowired
	ShPostRepository shPostRepository;
	@Autowired
	ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShGlobalIdRepository shGlobalIdRepository;

	public JSONObject toJSON(ShPost shPost) {
		JSONObject shPostItemAttrs = new JSONObject();

		JSONObject shPostObject = new JSONObject();
		shPostObject.put("id", shPost.getId());
		shPostObject.put("post-type-id", shPost.getShPostType().getId());
		shPostObject.put("title", shPost.getTitle());
		shPostObject.put("summary", shPost.getSummary());
		shPostObject.put("link",
				shFolderUtils.folderPath(shPost.getShFolder()) + shPost.getTitle().replaceAll(" ", "-"));
		for (ShPostAttr shPostAttr : shPost.getShPostAttrs()) {
			if (shPostAttr.getShPostTypeAttr().getName() != null) {
				shPostItemAttrs.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr.getStrValue());
			}
		}

		shPostItemAttrs.put("system", shPostObject);

		return shPostItemAttrs;
	}

	public Map<String, ShPostAttr> postToMap(ShPost shPost) {

		List<ShPostAttr> shPostAttrList = shPost.getShPostAttrs();

		Map<String, ShPostAttr> shPostMap = new HashMap<String, ShPostAttr>();
		ShPostAttr shPostAttrId = new ShPostAttr();
		shPostAttrId.setStrValue(shPost.getId().toString());
		shPostMap.put("id", shPostAttrId);
		for (ShPostAttr shPostAttr : shPostAttrList)
			shPostMap.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr);

		return shPostMap;

	}

	public ShPost copy(ShPost shPost, ShFolder shFolderDest) {

		ShPost shPostCopy = new ShPost();
		shPostCopy.setDate(new Date());
		shPostCopy.setShFolder(shFolderDest);
		shPostCopy.setShPostType(shPost.getShPostType());
		shPostCopy.setSummary(shPost.getSummary());
		shPostCopy.setTitle(shPost.getTitle());
		shPostRepository.save(shPostCopy);

		ShGlobalId shGlobalId = new ShGlobalId();
		shGlobalId.setShObject(shPostCopy);
		shGlobalId.setType("POST");

		shGlobalIdRepository.saveAndFlush(shGlobalId);
		List<ShPostAttr> shPostAttrs = shPostAttrRepository.findByShPost(shPost);
		for (ShPostAttr shPostAttr : shPostAttrs) {
			ShPostAttr shPostAttrClone = new ShPostAttr();
			shPostAttrClone.setDateValue(shPostAttr.getDateValue());
			shPostAttrClone.setIntValue(shPostAttr.getIntValue());
			shPostAttrClone.setReferenceObjects(shPostAttr.getReferenceObjects());
			shPostAttrClone.setShPost(shPostCopy);
			shPostAttrClone.setShPostTypeAttr(shPostAttr.getShPostTypeAttr());
			shPostAttrClone.setStrValue(shPostAttr.getStrValue());
			shPostAttrClone.setType(shPostAttr.getType());
			shPostAttrRepository.save(shPostAttrClone);
		}
		
		shPostCopy.setShGlobalId(shGlobalId);
		
		return shPostCopy;
	}

	public String generatePostLink(String postID) {
		ShPost shPost = shPostRepository.findById(UUID.fromString(postID));
		ShFolder shFolder = shPost.getShFolder();
		String link = shFolderUtils.generateFolderLink(shFolder.getId().toString());
		link = link + shPost.getTitle().replaceAll(" ", "-");
		return link;
	}
}
