package com.viglet.shiohara.onstartup.post.type;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.model.ShPostTypeAttr;
import com.viglet.shiohara.persistence.model.ShWidget;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeAttrRepository;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

@Component
public class ShPostTypeOnStartup {

	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	@Autowired
	private ShPostTypeAttrRepository shPostTypeAttrRepository;
	@Autowired
	ShWidgetRepository shWidgetRepository;

	public void createDefaultRows() {

		if (shPostTypeRepository.findAll().isEmpty()) {
			// Text
			ShWidget shWidgetText = shWidgetRepository.findById(1);

			ShPostType shPostType = new ShPostType();
			shPostType.setName("PT-TEXT");
			shPostType.setTitle("Text");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Text Post Type");

			shPostTypeRepository.save(shPostType);

			ShPostTypeAttr shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("title");
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

			// Text Area
			ShWidget shWidgetTextArea = shWidgetRepository.findById(2);

			shPostType = new ShPostType();
			shPostType.setName("PT-TEXT-AREA");
			shPostType.setTitle("Text Area");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Text Area Post Type");

			shPostTypeRepository.save(shPostType);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("title");
			shPostTypeAttr.setLabel("Title");
			shPostTypeAttr.setDescription("Title");
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
			shPostType.setName("PT-ARTICLE");
			shPostType.setTitle("Article");
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Article Post Type");

			shPostTypeRepository.save(shPostType);

			shPostTypeAttr = new ShPostTypeAttr();
			shPostTypeAttr.setName("title");
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
			shPostTypeAttr.setName("Description");
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
		}

	}
}
