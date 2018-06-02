package com.viglet.shiohara.persistence.repository.widget;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.widget.ShWidget;

public interface ShWidgetRepository extends JpaRepository<ShWidget, String> {

	List<ShWidget> findAll();

	Optional<ShWidget> findById(String id);
	
	ShWidget findByName(String name);

	@SuppressWarnings("unchecked")
	ShWidget save(ShWidget shWidget);

	@Modifying
	@Query("delete from ShWidget p where p.id = ?1")
	void delete(String id);
}
