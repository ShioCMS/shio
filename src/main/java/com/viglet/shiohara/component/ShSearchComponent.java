package com.viglet.shiohara.component;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.utils.ShPostUtils;

@Component
public class ShSearchComponent {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostUtils shPostUtils;
	
	public List<Map<String, ShPostAttr>> search(String q) {
		List<Map<String, ShPostAttr>> shPosts = new ArrayList<Map<String, ShPostAttr>>();
		for (ShPost shPost : shPostRepository.fuzzySearch(q)) {
			Map<String, ShPostAttr> shPostObject = shPostUtils.postToMap(shPost);
			shPosts.add(shPostObject);
		}
		return shPosts;
	}
}
