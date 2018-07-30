package com.viglet.shiohara.persistence.repository.ecommerce;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.ecommerce.ShEcomPaymentType;

public interface ShEcomPaymentTypeRepository extends JpaRepository<ShEcomPaymentType, String> {

	List<ShEcomPaymentType> findAll();
	
	Optional<ShEcomPaymentType> findById(String id);

	@SuppressWarnings("unchecked")
	ShEcomPaymentType save(ShEcomPaymentType shEcomPaymentType);

	@Modifying
	@Query("delete from ShEcomPaymentType ept where ept.id = ?1")
	void delete(String shEcomPaymentTypeId);
}
