package com.viglet.shiohara.onstartup.post;

import java.util.Date;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.persistence.model.channel.ShChannel;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.channel.ShChannelRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
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
	@Autowired
	private ShChannelRepository shChannelRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;
	
	public void createDefaultRows() {
		ShSite shSite = shSiteRepository.findByName("Sample");

		if (shPostRepository.findAll().isEmpty()) {

			ShChannel shChannelHome = shChannelRepository.findByShSiteAndName(shSite, "Home");
			ShChannel shChannelArticle = shChannelRepository.findByShSiteAndName(shSite, "Article");
			ShChannel shChannelNews = shChannelRepository.findByShSiteAndName(shSite, "News");
			ShChannel shChannelTemplates = shChannelRepository.findByShSiteAndName(shSite, "Templates");
			ShChannel shChannelLayouts = shChannelRepository.findByShSiteAndName(shSite, "Layouts");
			ShChannel shChannelText = shChannelRepository.findByShSiteAndName(shSite, "Text");
			ShChannel shChannelThemes = shChannelRepository.findByShSiteAndName(shSite, "Themes");

			
			ShPostType shPostRegion = shPostTypeRepository.findByName("PT-REGION");
			ShPostType shPostType = shPostTypeRepository.findByName("PT-TEXT");
			ShPostType shPostTypeArea = shPostTypeRepository.findByName("PT-TEXT-AREA");
			ShPostType shPostArticle = shPostTypeRepository.findByName("PT-ARTICLE");
			ShPostType shPostChannelIndex = shPostTypeRepository.findByName("PT-CHANNEL-INDEX");
			
			// Post Text
			
			ShPost shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostType);
			shPost.setSummary(null);
			shPost.setTitle("Post01");
			shPost.setShChannel(shChannelHome);

			shPostRepository.save(shPost);
			
			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostType, "TEXT");

			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Text Area

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeArea);
			shPost.setSummary(null);
			shPost.setTitle("Post Text Area 01");
			shPost.setShChannel(shChannelHome);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArea, "TEXT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Article

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostArticle);
			shPost.setSummary("A short description");
			shPost.setTitle("Post Article Title");
			shPost.setShChannel(shChannelArticle);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "TEXT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Some text...");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Article (Same Name, but different channel)

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostArticle);
			shPost.setSummary("A short description 2");
			shPost.setTitle("Post Article Title");
			shPost.setShChannel(shChannelNews);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostArticle, "TEXT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Some text 2...");
			shPostAttr.setType(1);


			// Sample Theme

			ShPostType shPostTheme = shPostTypeRepository.findByName("PT-THEME");

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTheme);
			shPost.setSummary("Sample Theme");
			shPost.setTitle("Sample Theme");
			shPost.setShChannel(shChannelThemes);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "CSS");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"<link href=\"https://blackrockdigital.github.io/startbootstrap-one-page-wonder/vendor/bootstrap/css/bootstrap.min.css\" rel=\"stylesheet\">\n"
							+ "<link href=\"https://fonts.googleapis.com/css?family=Catamaran:100,200,300,400,500,600,700,800,900\" rel=\"stylesheet\">\n"
							+ "<link href=\"https://fonts.googleapis.com/css?family=Lato:100,100i,300,300i,400,400i,700,700i,900,900i\" rel=\"stylesheet\">\n"
							+ "<link href=\"https://blackrockdigital.github.io/startbootstrap-one-page-wonder/css/one-page-wonder.min.css\" rel=\"stylesheet\">");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTheme, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n"
					+ "<script src=\"https://viglet.ai/ui/thirdparty/jquery/dist/jquery.min.js\"></script>\n"
					+ "<!-- Include all compiled plugins (below), or include individual files as needed -->\n"
					+ "<script src=\"https://viglet.ai/ui/thirdparty/bootstrap/dist/js/bootstrap.min.js\"></script>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
	
			
			// Post Channel Index Home

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostChannelIndex);
			shPost.setSummary("Channel Index");
			shPost.setTitle("index");
			shPost.setShChannel(shChannelHome);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Channel Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);


			// Post Channel Index Article
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostChannelIndex);
			shPost.setSummary("Channel Index");
			shPost.setTitle("index");
			shPost.setShChannel(shChannelArticle);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Channel Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			// Post Channel Index News
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostChannelIndex);
			shPost.setSummary("Channel Index");
			shPost.setTitle("index");
			shPost.setShChannel(shChannelNews);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Channel Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			// Post Channel Index Text
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostChannelIndex);
			shPost.setSummary("Channel Index");
			shPost.setTitle("index");
			shPost.setShChannel(shChannelText);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostChannelIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Channel Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			// Post Page Layout

			ShPostType shPostTypePageLayout = shPostTypeRepository.findByName("PT-PAGE-LAYOUT");
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypePageLayout);
			shPost.setSummary("Post Page Layout");
			shPost.setTitle("Post Page Layout");
			shPost.setShChannel(shChannelLayouts);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "THEME");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Sample Theme");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!DOCTYPE html>\n" + "<html>\n" + "    <head>\n" + "        <title>\n"
					+ "            Sample Site\n" + "        </title>\n" + "        {{{theme.css}}}\n" + "    </head>\n"
					+ "    <body>\n" + "        <div sh-region=\"Navigation\">\n"
					+ "            Navigation Placeholder\n" + "        </div>\n"
					+ "        <div sh-region=\"Content\">\n" + "            Content Placeholder\n" + "        </div>\n"
					+ "    </body>\n" + "    {{{theme.javascript}}}\n" + "</html>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Page Template Navigation

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostRegion);
			shPost.setSummary("Navigation Template");
			shPost.setTitle("Navigation");
			shPost.setShChannel(shChannelTemplates);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!-- Navigation -->\n"
					+ "    <nav class=\"navbar navbar-expand-lg navbar-dark navbar-custom fixed-top\">\n"
					+ "      <div class=\"container\">\n"
					+ "        <a class=\"navbar-brand\" href=\"#\">Viglet Shiohara</a>\n"
					+ "        <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarResponsive\" aria-controls=\"navbarResponsive\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n"
					+ "          <span class=\"navbar-toggler-icon\"></span>\n" + "        </button>\n"
					+ "        <div class=\"collapse navbar-collapse\" id=\"navbarResponsive\">\n"
					+ "          <ul class=\"navbar-nav ml-auto\">\n" + "            <li class=\"nav-item\">\n"
					+ "              <a class=\"nav-link\" href=\"/#!/content/post/type/{{system.post-type-id}}/post/{{system.id}}\">Come back to Content</a>\n"
					+ "            </li>\n" + "          </ul>\n" + "        </div>\n" + "      </div>\n"
					+ "    </nav>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Page Template Content

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostRegion);
			shPost.setSummary("Content Template");
			shPost.setTitle("Content");
			shPost.setShChannel(shChannelTemplates);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<header class=\"masthead text-center text-white\">\n"
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
					+ "        <p class=\"m-0 text-center text-white small\">Copyright &copy; Viglet 2018</p>\n"
					+ "      </div>\n" + "      <!-- /.container -->\n" + "    </footer>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Channel Page Layout

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypePageLayout);
			shPost.setSummary("Channel Page Layout");
			shPost.setTitle("Channel Page Layout");
			shPost.setShChannel(shChannelLayouts);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "THEME");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Sample Theme");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			
			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!DOCTYPE html>\n" + "<html>\n" + "    <head>\n" + "        <title>\n"
					+ "            Sample Site | Viglet Shiohara\n" + "        </title>\n" + "        {{{theme.css}}}\n"
					+ "    </head>\n" + "    <body>\n" + "        <div sh-region=\"NavigationChannel\">\n"
					+ "            Navigation Placeholder\n" + "        </div>\n"
					+ "        <div sh-region=\"ContentChannel\">\n" + "            Content Placeholder\n"
					+ "        </div>\n" + "    </body>\n" + "    {{{theme.javascript}}}\n" + "</html>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			// Page Template NavigationChannel

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostRegion);
			shPost.setSummary("Navigation Channel Template");
			shPost.setTitle("NavigationChannel");
			shPost.setShChannel(shChannelTemplates);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!-- Navigation -->\n"
					+ "    <nav class=\"navbar navbar-expand-lg navbar-dark navbar-custom fixed-top\">\n"
					+ "      <div class=\"container\">\n"
					+ "        <a class=\"navbar-brand\" href=\"#\">Viglet Shiohara</a>\n"
					+ "        <button class=\"navbar-toggler\" type=\"button\" data-toggle=\"collapse\" data-target=\"#navbarResponsive\" aria-controls=\"navbarResponsive\" aria-expanded=\"false\" aria-label=\"Toggle navigation\">\n"
					+ "          <span class=\"navbar-toggler-icon\"></span>\n" + "        </button>\n"
					+ "        <div class=\"collapse navbar-collapse\" id=\"navbarResponsive\">\n"
					+ "          <ul class=\"navbar-nav ml-auto\">\n" + "                {{#each channels}}\n"
					+ "                    <li class=\"nav-item\">\n"
					+ "                    <a class=\"nav-link\" href=\"{{../site.system.link}}{{this.system.link}}\">{{this.system.title}}</a>\n"
					+ "                    </li>\n" + "                {{/each}}\n"
					+ "            <li class=\"nav-item\">\n"
					+ "              <a class=\"nav-link\" href=\"/#!/content/list/channel/{{system.id}}\">Come back to Channel</a>\n"
					+ "            </li>\n" + "          </ul>\n" + "        </div>\n" + "      </div>\n"
					+ "    </nav>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Page Template ContentChannel

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostRegion);
			shPost.setSummary("Content Channel Template");
			shPost.setTitle("ContentChannel");
			shPost.setShChannel(shChannelTemplates);

			shPostRepository.save(shPost);
			
			shGlobalId = new ShGlobalId();
			shGlobalId.setObjectId(shPost.getId());
			shGlobalId.setType("POST");
			
			shGlobalIdRepository.save(shGlobalId);			

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n" + "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostRegion, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(" <header class=\"masthead text-center text-white\">\n"
					+ "      <div class=\"masthead-content\">\n" + "        <div class=\"container\">\n"
					+ "          <h1 class=\"masthead-heading mb-0\">{{system.title}}</h1>\n"
					+ "          <h2 class=\"masthead-subheading mb-0\">{{system.summary}}</h2>\n"
					+ "            {{#each posts}}\n"
					+ "            <a href=\"{{../site.system.link}}{{this.system.link}}\" class=\"btn btn-primary btn-xl rounded-pill mt-5\">{{this.system.title}}</a>\n"
					+ "            {{/each}}\n" + "        </div>\n" + "      </div>\n"
					+ "      <div class=\"bg-circle-1 bg-circle\"></div>\n"
					+ "      <div class=\"bg-circle-2 bg-circle\"></div>\n"
					+ "      <div class=\"bg-circle-3 bg-circle\"></div>\n"
					+ "      <div class=\"bg-circle-4 bg-circle\"></div>\n" + "    </header>\n" + "\n" + "    \n"
					+ "    <!-- Footer -->\n" + "    <footer class=\"py-5 bg-black\">\n"
					+ "      <div class=\"container\">\n"
					+ "        <p class=\"m-0 text-center text-white small\">Copyright &copy; Viglet 2018</p>\n"
					+ "      </div>\n" + "      <!-- /.container -->\n" + "    </footer>\n");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
		}

	}
}
