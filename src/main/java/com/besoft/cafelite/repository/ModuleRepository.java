package com.besoft.cafelite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Module;

@Repository
public interface ModuleRepository extends JpaRepository<Module, Long>{
	
	Page<Module> findByModuleCodeContainingOrModuleNameContainingAllIgnoreCase(String moduleCode, String moduleName, Pageable page);
}
