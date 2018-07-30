package com.viglet.shiohara.persistence.repository.ecommerce;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.ecommerce.ShEcomOrder;

public interface ShEcomOrderRepository extends JpaRepository<ShEcomOrder, String> {

	List<ShEcomOrder> findAll();
	
	Optional<ShEcomOrder> findById(String id);

	@SuppressWarnings("unchecked")
	ShEcomOrder save(ShEcomOrder shEcomOrder);

	@Modifying
	@Query("delete from ShEcomOrder eo where eo.id = ?1")
	void delete(String shEcomOrderId);
}
