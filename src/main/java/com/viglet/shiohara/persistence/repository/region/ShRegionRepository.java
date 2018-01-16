package com.viglet.shiohara.persistence.repository.region;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

import com.viglet.shiohara.persistence.model.region.ShRegion;

public interface ShRegionRepository extends JpaRepository<ShRegion, Integer> {

	List<ShRegion> findAll();

	ShRegion findById(int id);

	ShRegion save(ShRegion shRegion);

	void delete(ShRegion shRegion);
}
