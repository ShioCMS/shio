package com.viglet.shiohara.api.widget;

import java.util.List;
import java.util.UUID;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.viglet.shiohara.persistence.model.widget.ShWidget;
import com.viglet.shiohara.persistence.repository.widget.ShWidgetRepository;

import io.swagger.annotations.Api;

@RestController
@RequestMapping("/api/v2/widget")
@Api(tags="Widget", description="Widget API")
public class ShWidgetAPI {
	
	@Autowired
	private ShWidgetRepository shWidgetRepository;

	@GetMapping
	public List<ShWidget> shWidgetList() throws Exception {
		return shWidgetRepository.findAll();
	}
	
	@GetMapping("/{id}")
	public ShWidget shWidgetEdit(@PathVariable UUID id) throws Exception {
		return shWidgetRepository.findById(id).get();
	}

	@PutMapping("/{id}")
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

	@DeleteMapping("/{id}")
	public boolean shWidgetDelete(@PathVariable UUID id) throws Exception {
		shWidgetRepository.delete(id);
		return true;
	}

	@PostMapping
	public ShWidget shWidgetAdd(@RequestBody ShWidget shWidget) throws Exception {
		shWidgetRepository.save(shWidget);
		return shWidget;

	}

}
