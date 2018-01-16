package com.viglet.shiohara.persistence.repository.widget;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viglet.shiohara.persistence.model.widget.ShWidget;

public interface ShWidgetRepository extends JpaRepository<ShWidget, Integer> {

	List<ShWidget> findAll();

	ShWidget findById(int id);

	ShWidget save(ShWidget shWidget);

	void delete(ShWidget shWidget);
}
