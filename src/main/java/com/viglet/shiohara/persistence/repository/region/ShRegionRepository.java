package com.viglet.shiohara.persistence.repository.region;

import java.util.List;
import java.util.UUID;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viglet.shiohara.persistence.model.region.ShRegion;

public interface ShRegionRepository extends JpaRepository<ShRegion, UUID> {

	List<ShRegion> findAll();

	ShRegion findById(UUID id);

	ShRegion save(ShRegion shRegion);

	void delete(ShRegion shRegion);
}
