package com.viglet.shiohara.persistence.repository.ecommerce;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentTypeDefinition;

public interface ShEcomPaymentTypeDefinitionRepository extends JpaRepository<ShEcomPaymentTypeDefinition, String> {

	List<ShEcomPaymentTypeDefinition> findAll();

	Optional<ShEcomPaymentTypeDefinition> findById(String id);

	ShEcomPaymentTypeDefinition findByName(String name);

	@SuppressWarnings("unchecked")
	ShEcomPaymentTypeDefinition save(ShEcomPaymentTypeDefinition shEcomPaymentType);

	@Modifying
	@Query("delete from ShEcomPaymentTypeDefinition eptd where eptd.id = ?1")
	void delete(String shEcomPaymentTypeDefinitionId);
}
