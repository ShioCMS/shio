package com.viglet.shiohara.listener.onstartup;

import java.util.Calendar;

import com.viglet.shiohara.persistence.model.ShPostType;
import com.viglet.shiohara.persistence.service.ShPostTypeService;


public class ShPostTypeOnStartup {
	public static void createDefaultRows() {
		ShPostTypeService shPostTypeService = new ShPostTypeService();
		
		if (shPostTypeService.listAll().isEmpty()) {

			ShPostType shPostType = new ShPostType();
			shPostType.setName("PT-TEXT");
			shPostType.setTitle("Text");			
			shPostType.setDate(Calendar.getInstance().getTime());
			shPostType.setDescription("Text Post Type");
			
		}

	}
}
