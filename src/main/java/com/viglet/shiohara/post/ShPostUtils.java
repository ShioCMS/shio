package com.viglet.shiohara.post;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.channel.ShChannelUtils;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;

@Component
public class ShPostUtils {
	@Autowired
	ShChannelUtils shChannelUtils;

	public JSONObject toJSON(ShPost shPost) {
		JSONObject shPostItemAttrs = new JSONObject();

		JSONObject shPostObject = new JSONObject();
		shPostObject.put("id", shPost.getId());
		shPostObject.put("post-type-id", shPost.getShPostType().getId());
		shPostObject.put("title", shPost.getTitle());
		shPostObject.put("summary", shPost.getSummary());
		shPostObject.put("link",
				shChannelUtils.channelPath(shPost.getShChannel()) + shPost.getTitle().replaceAll(" ", "-"));
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
		for (ShPostAttr shPostAttr : shPostAttrList)
			shPostMap.put(shPostAttr.getShPostTypeAttr().getName(), shPostAttr);

		return shPostMap;

	}
}
