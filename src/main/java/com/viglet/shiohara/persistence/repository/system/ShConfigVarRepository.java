package com.viglet.shiohara.persistence.repository.system;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShConfigVarRepository extends JpaRepository<ShConfigVar, String> {

	List<ShConfigVar> findAll();

	ShConfigVar findById(String id);

	ShConfigVar save(ShConfigVar turConfigVar);

	void delete(ShConfigVar turConfigVar);
}
