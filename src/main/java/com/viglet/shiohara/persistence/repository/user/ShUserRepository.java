package com.viglet.shiohara.persistence.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import com.viglet.shiohara.persistence.model.user.ShUser;

public interface ShUserRepository extends JpaRepository<ShUser, Integer> {

	List<ShUser> findAll();

	ShUser findById(int id);

	@SuppressWarnings("unchecked")
	ShUser save(ShUser shUser);
	
	ShUser findByUsername(String username);
	
	@Modifying
	@Query("delete from ShUser p where p.id = ?1")
	void delete(int id);
}
