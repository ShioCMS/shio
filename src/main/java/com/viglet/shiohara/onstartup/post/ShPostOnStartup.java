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

			// Theme Template

			ShPostType shPostTheme = shPostTypeRepository.findByName("PT-THEME");

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTheme);
			shPost.setSummary("Home Theme");
			shPost.setTitle("Home Theme");

			shPostRepository.save(shPost);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "title");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostTheme);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(1);
			shPostAttr.setStrValue("Home Theme");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "Description");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostTheme);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(2);
			shPostAttr.setStrValue("Home Theme");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "CSS");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostTheme);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(3);
			shPostAttr.setStrValue(
					"<link href=\"https://blackrockdigital.github.io/startbootstrap-one-page-wonder/vendor/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n"
							+ "<link href=\"https://fonts.googleapis.com/css?family=Catamaran:100,200,300,400,500,600,700,800,900\" rel=\"stylesheet\">\n"
							+ "<link href=\"https://fonts.googleapis.com/css?family=Lato:100,100i,300,300i,400,400i,700,700i,900,900i\" rel=\"stylesheet\">\n"
							+ "<link href=\"https://blackrockdigital.github.io/startbootstrap-one-page-wonder/css/one-page-wonder.min.css\" rel=\"stylesheet\">");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "Javascript");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostTheme);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(4);
			shPostAttr.setStrValue("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n"
					+ "<script src=\"https://viglet.ai/ui/thirdparty/jquery/dist/jquery.min.js\"></script>\n"
					+ "<!-- Include all compiled plugins (below), or include individual files as needed -->\n"
					+ "<script src=\"https://viglet.ai/ui/thirdparty/bootstrap/dist/js/bootstrap.min.js\"></script>");
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
							+ "var template = Handlebars.compile(html);\n" + "var html = template(post);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostPageTemplate, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostType(shPostPageTemplate);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setShPostTypeAttrId(4);
			shPostAttr.setStrValue("<!DOCTYPE html>\n" + "<html>\n" + "    <head>\n"
					+ "    <title>Sample Site | Viglet Shiohara</title>\n" + "    {{{theme.css}}}\n" + "    </head>\n"
					+ "    <body>\n" + "\n" + "    <!-- Navigation -->\n"
					+ "    <nav class=\"navbar navbar-expand-lg navbar-dark navbar-custom fixed-top\">\n"
					+ "      <div class=\"container\">\n"
					+ "        <a class=\"navbar-brand\" href=\"#\">Viglet Shiohara</a>\n"
					+ "        <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarResponsive\" aria-controls=\"navbarResponsive\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n"
					+ "          <span class=\"navbar-toggler-icon\"></span>\n" + "        </button>\n"
					+ "        <div class=\"collapse navbar-collapse\" id=\"navbarResponsive\">\n"
					+ "          <ul class=\"navbar-nav ml-auto\">\n" + "            <li class=\"nav-item\">\n"
					+ "              <a class=\"nav-link\" href=\"#\">Sign Up</a>\n" + "            </li>\n"
					+ "            <li class=\"nav-item\">\n"
					+ "              <a class=\"nav-link\" href=\"#\">Log In</a>\n" + "            </li>\n"
					+ "          </ul>\n" + "        </div>\n" + "      </div>\n" + "    </nav>\n" + "\n"
					+ "    <header class=\"masthead text-center text-white\">\n"
					+ "      <div class=\"masthead-content\">\n" + "        <div class=\"container\">\n"
					+ "          <h1 class=\"masthead-heading mb-0\">{{system.title}}</h1>\n"
					+ "          <h2 class=\"masthead-subheading mb-0\">{{system.summary}}</h2>\n"
					+ "          <a href=\"#\" class=\"btn btn-primary btn-xl rounded-pill mt-5\">Learn More</a>\n"
					+ "        </div>\n" + "      </div>\n" + "      <div class=\"bg-circle-1 bg-circle\"></div>\n"
					+ "      <div class=\"bg-circle-2 bg-circle\"></div>\n"
					+ "      <div class=\"bg-circle-3 bg-circle\"></div>\n"
					+ "      <div class=\"bg-circle-4 bg-circle\"></div>\n" + "    </header>\n" + "\n" + "    \n"
					+ "    <!-- Footer -->\n" + "    <footer class=\"py-5 bg-black\">\n"
					+ "      <div class=\"container\">\n"
					+ "        <p class=\"m-0 text-center text-white small\">Copyright &copy; Viglet Shiohara 2018</p>\n"
					+ "      </div>\n" + "      <!-- /.container -->\n" + "    </footer>\n" + "\n"
					+ "{{{theme.javascript}}}\n" + "  </body>\n" + "</html>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
		}

	}
}
