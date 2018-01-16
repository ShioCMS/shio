package com.viglet.shiohara.onstartup.post;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;

@Component
public class ShPostOnStartup {

	@Autowired
	private ShPostRepository shPostRepository;
	@Autowired
	private ShPostAttrRepository shPostAttrRepository;
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShSiteRepository shSiteRepository;

	public void createDefaultRows() {
		// ShSite shSite = shSiteRepository.findById(1);

		if (shPostRepository.findAll().isEmpty()) {
			// Post Text
			ShPostType shPostType = shPostTypeRepository.findByName("PT-TEXT");

			ShPost shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostType);
			shPost.setSummary("Summary");
			shPost.setTitle("Post01");

			shPostRepository.save(shPost);

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, "title");

			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostType);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Post01");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Text Area
			ShPostType shPostTypeArea = shPostTypeRepository.findByName("PT-TEXT-AREA");

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeArea);
			shPost.setSummary("Summary");
			shPost.setTitle("Post Text Area 01");

			shPostRepository.save(shPost);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArea, "title");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostTypeArea);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Post Text Area 01");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Article

			ShPostType shPostArticle = shPostTypeRepository.findByName("PT-ARTICLE");

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostArticle);
			shPost.setSummary("A short description");
			shPost.setTitle("Post Article Title");

			shPostRepository.save(shPost);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "title");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostArticle);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Post Article");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "Description");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostArticle);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(2);
			shPostAttr.setStrValue("A short description ...");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Page Template

			ShPostType shPostPageTemplate = shPostTypeRepository.findByName("PT-PAGE-TEMPLATE");

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostPageTemplate);
			shPost.setSummary("Home Template");
			shPost.setTitle("Home Page");

			shPostRepository.save(shPost);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostPageTemplate, "title");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostPageTemplate);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Home Page");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostPageTemplate, "Description");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostPageTemplate);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(2);
			shPostAttr.setStrValue("Home Template");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostPageTemplate, "Javascript");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostPageTemplate);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(3);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n"
							+ "var context = {title: \"My New Post\", body: \"This is my first post!\"};\n"
							+ "var htmlTest   = template(context);\n" + "htmlTest");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostPageTemplate, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostPageTemplate);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(4);
			shPostAttr.setStrValue("<div class=\"entry\">\n" + "  <h1>{{title}}</h1>\n" + "  <div class=\"body\">\n"
					+ "    {{body}}\n" + "  </div>\n" + "</div>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
		}

	}
}
