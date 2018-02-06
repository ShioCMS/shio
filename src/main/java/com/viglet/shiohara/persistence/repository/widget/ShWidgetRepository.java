package com.viglet.shiohara.persistence.repository.widget;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viglet.shiohara.persistence.model.widget.ShWidget;

public interface ShWidgetRepository extends JpaRepository<ShWidget, UUID> {

	List<ShWidget> findAll();

	ShWidget findById(UUID id);
	
	ShWidget findByName(String name);

	ShWidget save(ShWidget shWidget);

	void delete(ShWidget shWidget);
}
