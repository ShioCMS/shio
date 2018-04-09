package com.viglet.shiohara.onstartup.post;

import java.util.Date;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.post.ShPostAttr;
import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.folder.ShFolder;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.ShPost;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.site.ShSite;
import com.viglet.shiohara.persistence.repository.folder.ShFolderRepository;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostAttrRepository;
import com.viglet.shiohara.persistence.repository.post.ShPostRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.site.ShSiteRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;

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
	private ShFolderRepository shFolderRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

	public void createDefaultRows() {
		ShSite shSite = shSiteRepository.findByName("Sample");

		if (shPostRepository.findAll().isEmpty()) {

			ShFolder shFolderHome = shFolderRepository.findByShSiteAndName(shSite, "Home");
			ShFolder shFolderArticle = shFolderRepository.findByShSiteAndName(shSite, "Article");
			ShFolder shFolderNews = shFolderRepository.findByShSiteAndName(shSite, "News");
			ShFolder shFolderTemplates = shFolderRepository.findByShSiteAndName(shSite, "Templates");
			ShFolder shFolderLayouts = shFolderRepository.findByShSiteAndName(shSite, "Layouts");
			ShFolder shFolderText = shFolderRepository.findByShSiteAndName(shSite, "Text");
			ShFolder shFolderThemes = shFolderRepository.findByShSiteAndName(shSite, "Themes");

			ShPostType shPostTypeRegion = shPostTypeRepository.findByName(ShSystemPostType.REGION.toString());
			ShPostType shPostTypeText = shPostTypeRepository.findByName(ShSystemPostType.TEXT.toString());
			ShPostType shPostTypeTextArea = shPostTypeRepository.findByName(ShSystemPostType.TEXT_AREA.toString());
			ShPostType shPostTypeArticle = shPostTypeRepository.findByName(ShSystemPostType.ARTICLE.toString());
			ShPostType shPostTypeFolderIndex = shPostTypeRepository.findByName(ShSystemPostType.FOLDER_INDEX.toString());
			ShPostType shPostTypeTheme = shPostTypeRepository.findByName(ShSystemPostType.THEME.toString());
			
			// Post Text

			ShPost shPost = new ShPost();
			shPost.setId(UUID.fromString("990e5c4b-3369-4718-b8dc-5efde982c4b7"));
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeText);
			shPost.setSummary(null);
			shPost.setTitle("Post01");
			shPost.setShFolder(shFolderHome);
			shPostRepository.save(shPost);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			ShPostTypeAttr shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeText, "TEXT");

			ShPostAttr shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Text Area

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeTextArea);
			shPost.setSummary(null);
			shPost.setTitle("Post Text Area 01");
			shPost.setShFolder(shFolderHome);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeTextArea, "TEXT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Article

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeArticle);
			shPost.setSummary("A short description");
			shPost.setTitle("Post Article Title");
			shPost.setShFolder(shFolderArticle);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArticle, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArticle, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArticle, "TEXT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Some text...");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Article (Same Name, but different folder)

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeArticle);
			shPost.setSummary("A short description 2");
			shPost.setTitle("Post Article Title");
			shPost.setShFolder(shFolderNews);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArticle, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArticle, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeArticle, "TEXT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Some text 2...");
			shPostAttr.setType(1);

			// Sample Theme

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeTheme);
			shPost.setSummary("Sample Theme");
			shPost.setTitle("Sample Theme");
			shPost.setShFolder(shFolderThemes);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeTheme, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeTheme, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeTheme, "CSS");

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

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeTheme, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!-- jQuery (necessary for Bootstrap's JavaScript plugins) -->\n"
					+ "<script src=\"https://viglet.ai/ui/thirdparty/jquery/dist/jquery.min.js\"></script>\n"
					+ "<!-- Include all compiled plugins (below), or include individual files as needed -->\n"
					+ "<script src=\"https://viglet.ai/ui/thirdparty/bootstrap/dist/js/bootstrap.min.js\"></script>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Folder Index Home

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeFolderIndex);
			shPost.setSummary("Folder Index");
			shPost.setTitle("index");
			shPost.setShFolder(shFolderHome);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Folder Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Folder Index Article
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeFolderIndex);
			shPost.setSummary("Folder Index");
			shPost.setTitle("index");
			shPost.setShFolder(shFolderArticle);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Folder Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Folder Index News
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeFolderIndex);
			shPost.setSummary("Folder Index");
			shPost.setTitle("index");
			shPost.setShFolder(shFolderNews);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Folder Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Folder Index Text
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeFolderIndex);
			shPost.setSummary("Folder Index");
			shPost.setTitle("index");
			shPost.setShFolder(shFolderText);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeFolderIndex, "PAGE-LAYOUT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("Folder Page Layout");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Post Page Layout

			ShPostType shPostTypePageLayout = shPostTypeRepository.findByName("PT-PAGE-LAYOUT");
			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypePageLayout);
			shPost.setSummary("Post Page Layout");
			shPost.setTitle("Post Page Layout");
			shPost.setShFolder(shFolderLayouts);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

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

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n"
							+ "html;");
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
			shPost.setShPostType(shPostTypeRegion);
			shPost.setSummary("Navigation Template");
			shPost.setTitle("Navigation");
			shPost.setShFolder(shFolderTemplates);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n"
							+ "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "HTML");

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
			shPost.setShPostType(shPostTypeRegion);
			shPost.setSummary("Content Template");
			shPost.setTitle("Content");
			shPost.setShFolder(shFolderTemplates);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n"
							+ "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "HTML");

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

			// Folder Page Layout

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypePageLayout);
			shPost.setSummary("Folder Page Layout");
			shPost.setTitle("Folder Page Layout");
			shPost.setShFolder(shFolderLayouts);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

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

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n"
							+ "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypePageLayout, "HTML");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue("<!DOCTYPE html>\n" + "<html>\n" + "    <head>\n" + "        <title>\n"
					+ "            Sample Site | Viglet Shiohara\n" + "        </title>\n" + "        {{{theme.css}}}\n"
					+ "    </head>\n" + "    <body>\n" + "        <div sh-region=\"NavigationFolder\">\n"
					+ "            Navigation Placeholder\n" + "        </div>\n"
					+ "        <div sh-region=\"ContentFolder\">\n" + "            Content Placeholder\n"
					+ "        </div>\n" + "    </body>\n" + "    {{{theme.javascript}}}\n" + "</html>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);
			// Page Template NavigationFolder

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeRegion);
			shPost.setSummary("Navigation Folder Template");
			shPost.setTitle("NavigationFolder");
			shPost.setShFolder(shFolderTemplates);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n"
							+ "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "HTML");

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
					+ "          <ul class=\"navbar-nav ml-auto\">\n" + "                {{#each folders}}\n"
					+ "                    <li class=\"nav-item\">\n"
					+ "                    <a class=\"nav-link\" href=\"{{../site.system.link}}{{this.system.link}}\">{{this.system.title}}</a>\n"
					+ "                    </li>\n" + "                {{/each}}\n"
					+ "            <li class=\"nav-item\">\n"
					+ "              <a class=\"nav-link\" href=\"/#!/content/list/folder/{{system.id}}\">Come back to Folder</a>\n"
					+ "            </li>\n" + "          </ul>\n" + "        </div>\n" + "      </div>\n"
					+ "    </nav>");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			// Page Template ContentFolder

			shPost = new ShPost();
			shPost.setDate(new Date());
			shPost.setShPostType(shPostTypeRegion);
			shPost.setSummary("Content Folder Template");
			shPost.setTitle("ContentFolder");
			shPost.setShFolder(shFolderTemplates);

			shPostRepository.save(shPost);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPost);
			shGlobalId.setType(ShObjectType.POST);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "TITLE");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getTitle());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "DESCRIPTION");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(shPost.getSummary());
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "JAVASCRIPT");

			shPostAttr = new ShPostAttr();
			shPostAttr.setShPost(shPost);
			shPostAttr.setShPostTypeAttr(shPostTypeAttr);
			shPostAttr.setStrValue(
					"load('https://cdnjs.cloudflare.com/ajax/libs/handlebars.js/4.0.11/handlebars.min.js');\n"
							+ "var template = Handlebars.compile(html);\n" + "var html = template(shContent);\n"
							+ "html;");
			shPostAttr.setType(1);

			shPostAttrRepository.save(shPostAttr);

			shPostTypeAttr = shPostTypeAttrRepository.findByShPostTypeAndName(shPostTypeRegion, "HTML");

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
