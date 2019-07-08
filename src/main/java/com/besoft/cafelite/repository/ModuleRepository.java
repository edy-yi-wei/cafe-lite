package com.besoft.cafelite.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long>{
	
	@Query("SELECT m FROM Module m WHERE (m.moduleCode LIKE %:moduleCode% OR m.moduleName LIKE %:moduleName%) AND m.deleted = false ")
	Page<Module> findByModuleCodeContainingOrModuleNameContainingAllIgnoreCaseAndDeleted(@Param("moduleCode") String moduleCode, @Param("moduleName") String moduleName, Pageable page);
	
	@Query("SELECT m FROM Module m WHERE m.deleted = false")
	List<Module> findAllModule();
}
