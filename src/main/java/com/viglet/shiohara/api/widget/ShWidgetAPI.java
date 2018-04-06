package com.viglet.shiohara.api.widget;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/widget")
@Api(tags="Widget", description="Widget API")
public class ShWidgetAPI {
	
	@Autowired
	ShWidgetRepository shWidgetRepository;

	@RequestMapping(method = RequestMethod.GET)
	public List<ShWidget> shWidgetList() throws Exception {
		return shWidgetRepository.findAll();
	}
	
	@RequestMapping(method = RequestMethod.GET, value = "/{id}")
	public ShWidget shWidgetEdit(@PathVariable UUID id) throws Exception {
		return shWidgetRepository.findById(id).get();
	}

	@RequestMapping(method = RequestMethod.PUT, value = "/{id}")
	public ShWidget shWidgetUpdate(@PathVariable UUID id, @RequestBody ShWidget shWidget) throws Exception {
		ShWidget shWidgetEdit = shWidgetRepository.findById(id).get();
		shWidgetEdit.setName(shWidget.getName());
		shWidgetEdit.setType(shWidget.getType());
		shWidgetEdit.setClassName(shWidget.getClassName());
		shWidgetEdit.setDescription(shWidget.getDescription());
		shWidgetEdit.setImplementationCode(shWidget.getImplementationCode());
		shWidgetRepository.save(shWidgetEdit);
		return shWidgetEdit;
	}

	@RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
	public boolean shWidgetDelete(@PathVariable UUID id) throws Exception {
		shWidgetRepository.delete(id);
		return true;
	}

	@RequestMapping(method = RequestMethod.POST)
	public ShWidget shWidgetAdd(@RequestBody ShWidget shWidget) throws Exception {
		shWidgetRepository.save(shWidget);
		return shWidget;

	}

}
