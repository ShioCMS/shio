package com.viglet.shiohara.onstartup.post;

import java.util.Calendar;
import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.ShPost;
import com.viglet.shiohara.persistence.model.ShPostAttr;
import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.model.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.ShWidget;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

@Component
public class ShPostOnStartup {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;

	public void createDefaultRows() {

		if (shPostRepository.findAll().isEmpty()) {
			// Post Text
			ShPostType shPostType = shPostTypeRepository.findById(1);

			ShPost shPost = new ShPost();
			shPost.setDate(new Date());			
			shPost.setShPostType(shPostType);
			shPost.setSummary("Summary");
			shPost.setTitle("Post01");
			

			shPostRepository.save(shPost);

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findById(1);
			
			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostType);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Post01");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			// Post Text Area
			ShPostType shPostTypeArea = shPostTypeRepository.findById(2);

			shPost = new ShPost();
			shPost.setDate(new Date());			
			shPost.setShPostType(shPostTypeArea);
			shPost.setSummary("Summary");
			shPost.setTitle("Post Text Area 01");
			

			shPostRepository.save(shPost);

			shPostTypeAttr = shPostTypeAttrRepository.findById(2);
			
			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostTypeArea);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Post Text Area 01");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Article
			
			ShPostType shPostArticle = shPostTypeRepository.findById(3);

			shPost = new ShPost();
			shPost.setDate(new Date());			
			shPost.setShPostType(shPostArticle);
			shPost.setSummary("A short description");
			shPost.setTitle("Post Article Title");
			

			shPostRepository.save(shPost);

			shPostTypeAttr = shPostTypeAttrRepository.findById(3);
			
			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostArticle);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Post Article");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			shPostTypeAttr = shPostTypeAttrRepository.findById(3);
			
			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostArticle);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(2);
			shPostAttr.setStrValue("A short description ...");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
		}

	}
}
