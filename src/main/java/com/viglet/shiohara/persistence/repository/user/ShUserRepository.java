package com.viglet.shiohara.persistence.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.user.ShUser;

public interface ShUserRepository extends JpaRepository<ShUser, String> {

	List<ShUser> findAll();

	ShUser findByUsername(String username);

	@SuppressWarnings("unchecked")
	ShUser save(ShUser shUser);

	@Modifying
	@Query("delete from ShUser p where p.username = ?1")
	void delete(String id);
}
