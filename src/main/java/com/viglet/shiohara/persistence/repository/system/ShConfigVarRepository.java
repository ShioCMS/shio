package com.viglet.shiohara.persistence.repository.system;

import com.viglet.shiohara.persistence.model.system.ShConfigVar;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShConfigVarRepository extends JpaRepository<ShConfigVar, String> {

	List<ShConfigVar> findAll();

	Optional<ShConfigVar> findById(String id);

	ShConfigVar save(ShConfigVar turConfigVar);

	void delete(ShConfigVar turConfigVar);
}
