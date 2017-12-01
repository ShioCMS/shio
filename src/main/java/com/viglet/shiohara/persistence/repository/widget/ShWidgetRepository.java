package com.viglet.shiohara.persistence.repository.widget;

import com.viglet.shiohara.persistence.model.ShWidget;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShWidgetRepository extends JpaRepository<ShWidget, Integer> {

	List<ShWidget> findAll();

	ShWidget findById(int id);

	ShWidget save(ShWidget shWidget);

	void delete(ShWidget shWidget);
}
