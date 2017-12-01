package com.viglet.shiohara.onstartup.post.type;

import java.util.Calendar;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.repository.post.type.ShPostTypeRepository;

@Component
public class ShPostTypeOnStartup {
	
	@Autowired
	private ShPostTypeRepository shPostTypeRepository;
	
	public void createDefaultRows() {

		if (shPostTypeRepository.findAll().isEmpty()) {

			ShPostType shPostType = new ShPostType();
			shPostType.setName("PT-TEXT");
			shPostType.setTitle("Text");			
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Text Post Type");
			
			shPostTypeRepository.save(shPostType);
		}

	}
}
