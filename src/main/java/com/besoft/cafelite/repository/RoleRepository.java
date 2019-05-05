package com.besoft.cafelite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Role;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long>{
	
	Page<Role> findByRoleNameContainingAllIgnoreCase(String roleName, Pageable page);
	
	@Query("SELECT r FROM Role r JOIN r.moduleList m WHERE m.module.moduleName LIKE %:moduleName%")
	Page<Role> findByModuleName(@Param("moduleName") String moduleName, Pageable page);
}
