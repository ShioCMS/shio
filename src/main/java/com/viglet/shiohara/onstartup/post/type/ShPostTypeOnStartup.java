package com.viglet.shiohara.onstartup.post.type;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.object.ShObjectType;
import com.viglet.shiohara.persistence.model.globalid.ShGlobalId;
import com.viglet.shiohara.persistence.model.post.type.ShPostType;
import com.viglet.shiohara.persistence.model.post.type.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.globalid.ShGlobalIdRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;
import com.viglet.shiohara.post.type.ShSystemPostType;
import com.viglet.shiohara.widget.ShSystemWidget;

@Component
public class ShPostTypeOnStartup {

	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	private ShWidgetRepository shWidgetRepository;
	@Autowired
	private ShGlobalIdRepository shGlobalIdRepository;

	public void createDefaultRows() {

		if (shPostTypeRepository.findAll().isEmpty()) {

			ShWidget shWidgetText = shWidgetRepository.findByName(ShSystemWidget.TEXT.toString());
			ShWidget shWidgetTextArea = shWidgetRepository.findByName(ShSystemWidget.TEXT_AREA.toString());
			ShWidget shWidgetFile = shWidgetRepository.findByName(ShSystemWidget.FILE.toString());
			ShWidget shWidgetAceEditorJS = shWidgetRepository.findByName(ShSystemWidget.ACE_JS.toString());
			ShWidget shWidgetAceEditorHTML = shWidgetRepository.findByName(ShSystemWidget.ACE_HTML.toString());
			ShWidget shWidgetHTMLEditor = shWidgetRepository.findByName(ShSystemWidget.HTML_EDITOR.toString());
			// Text

			ShPostType shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.TEXT);
			shPostType.setTitle("Text");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Text Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			ShGlobalId shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TEXT");
			shPostTypeAttr.setLabel("Text");
			shPostTypeAttr.setDescription("Text");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Photo

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.PHOTO);
			shPostType.setTitle("Photo");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Photo Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("PHOTO");
			shPostTypeAttr.setLabel("Photo Path");
			shPostTypeAttr.setDescription("Photo Path");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Video

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.VIDEO);
			shPostType.setTitle("Video");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Video Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("VIDEO");
			shPostTypeAttr.setLabel("Video Path");
			shPostTypeAttr.setDescription("Video Path");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Quote

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.QUOTE);
			shPostType.setTitle("Quote");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Quote Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("QUOTE");
			shPostTypeAttr.setLabel("Quote");
			shPostTypeAttr.setDescription("Quote Text");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Link

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.LINK);
			shPostType.setTitle("Link");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Link Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("LINK");
			shPostTypeAttr.setLabel("Link");
			shPostTypeAttr.setDescription("Link");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// File

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.FILE);
			shPostType.setTitle("File");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("File Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("FILE");
			shPostTypeAttr.setLabel("File Path");
			shPostTypeAttr.setDescription("File Path");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetFile);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Text Area

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.TEXT_AREA);
			shPostType.setTitle("Text Area");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Text Area Post Type");
			shPostType.setSystem((byte) 0);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TEXT");
			shPostTypeAttr.setLabel("Text");
			shPostTypeAttr.setDescription("Text");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetTextArea);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Article

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.ARTICLE);
			shPostType.setTitle("Article");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Article Post Type");
			shPostType.setSystem((byte) 0);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TITLE");
			shPostTypeAttr.setLabel("Title");
			shPostTypeAttr.setDescription("Title");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("DESCRIPTION");
			shPostTypeAttr.setLabel("Description");
			shPostTypeAttr.setDescription("Description");
			shPostTypeAttr.setIsSummary((byte) 1);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(2);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetTextArea);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("FILE");
			shPostTypeAttr.setLabel("File Path");
			shPostTypeAttr.setDescription("File Path");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(3);
			shPostTypeAttr.setRequired((byte) 0);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetFile);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TEXT");
			shPostTypeAttr.setLabel("Text");
			shPostTypeAttr.setDescription("Text");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(4);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetHTMLEditor);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Region

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.REGION);
			shPostType.setTitle("Region");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Region Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TITLE");
			shPostTypeAttr.setLabel("Title");
			shPostTypeAttr.setDescription("Title");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("DESCRIPTION");
			shPostTypeAttr.setLabel("Description");
			shPostTypeAttr.setDescription("Description");
			shPostTypeAttr.setIsSummary((byte) 1);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(2);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetTextArea);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("JAVASCRIPT");
			shPostTypeAttr.setLabel("Javascript");
			shPostTypeAttr.setDescription("Javascript");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(3);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetAceEditorJS);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("HTML");
			shPostTypeAttr.setLabel("HTML");
			shPostTypeAttr.setDescription("HTML");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(4);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetAceEditorHTML);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Theme

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.THEME);
			shPostType.setTitle("Theme");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Theme Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TITLE");
			shPostTypeAttr.setLabel("Title");
			shPostTypeAttr.setDescription("Title");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("DESCRIPTION");
			shPostTypeAttr.setLabel("Description");
			shPostTypeAttr.setDescription("Description");
			shPostTypeAttr.setIsSummary((byte) 1);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(2);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetTextArea);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("CSS");
			shPostTypeAttr.setLabel("CSS");
			shPostTypeAttr.setDescription("CSS");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(3);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetAceEditorHTML);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("JAVASCRIPT");
			shPostTypeAttr.setLabel("Javascript");
			shPostTypeAttr.setDescription("Javascript");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(4);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetAceEditorHTML);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Page Layout

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.PAGE_LAYOUT);
			shPostType.setTitle("Page Layout");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Page Layout Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TITLE");
			shPostTypeAttr.setLabel("Title");
			shPostTypeAttr.setDescription("Title");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("DESCRIPTION");
			shPostTypeAttr.setLabel("Description");
			shPostTypeAttr.setDescription("Description");
			shPostTypeAttr.setIsSummary((byte) 1);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(2);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("THEME");
			shPostTypeAttr.setLabel("Theme");
			shPostTypeAttr.setDescription("Theme");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(3);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("JAVASCRIPT");
			shPostTypeAttr.setLabel("Javascript");
			shPostTypeAttr.setDescription("Javascript");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(4);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetAceEditorJS);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("HTML");
			shPostTypeAttr.setLabel("HTML");
			shPostTypeAttr.setDescription("HTML");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(5);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetAceEditorHTML);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			// Folder Index

			shPostType = new ShPostType();
			shPostType.setName(ShSystemPostType.FOLDER_INDEX);
			shPostType.setTitle("Folder Index");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Folder Index Post Type");
			shPostType.setSystem((byte) 1);

			shPostTypeRepository.save(shPostType);

			shGlobalId = new ShGlobalId();
			shGlobalId.setShObject(shPostType);
			shGlobalId.setType(ShObjectType.POST_TYPE);

			shGlobalIdRepository.save(shGlobalId);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("TITLE");
			shPostTypeAttr.setLabel("Title");
			shPostTypeAttr.setDescription("Title");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 1);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(1);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("DESCRIPTION");
			shPostTypeAttr.setLabel("Description");
			shPostTypeAttr.setDescription("Description");
			shPostTypeAttr.setIsSummary((byte) 1);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(2);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetTextArea);

			shPostTypeAttrRepository.save(shPostTypeAttr);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("PAGE-LAYOUT");
			shPostTypeAttr.setLabel("Page Layout");
			shPostTypeAttr.setDescription("Page Layout");
			shPostTypeAttr.setIsSummary((byte) 0);
			shPostTypeAttr.setIsTitle((byte) 0);
			shPostTypeAttr.setMany((byte) 0);
			shPostTypeAttr.setOrdinal(2);
			shPostTypeAttr.setRequired((byte) 1);
			shPostTypeAttr.setShPostType(shPostType);
			shPostTypeAttr.setShWidget(shWidgetText);

			shPostTypeAttrRepository.save(shPostTypeAttr);

		}

	}
}
