package com.viglet.shiohara.persistence.repository.user;

import com.viglet.shiohara.persistence.model.ShUser;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;

public interface ShUserRepository extends JpaRepository<ShUser, Integer> {

	List<ShUser> findAll();

	ShUser findById(int id);

	ShUser save(ShUser shUser);

	void delete(ShUser shUser);
}
