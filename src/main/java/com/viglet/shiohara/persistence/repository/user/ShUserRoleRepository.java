package com.viglet.shiohara.persistence.repository.user;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.viglet.shiohara.persistence.model.user.ShUserRole;

@Repository
public interface ShUserRoleRepository extends JpaRepository<ShUserRole, Long> {
	
	@Query("select a.role from ShUserRole a, ShUser b where b.username=?1 and a.userid=b.id")
    public List<String> findRoleByUsername(String username);
	
}