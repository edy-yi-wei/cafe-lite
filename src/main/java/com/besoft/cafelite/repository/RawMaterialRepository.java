package com.besoft.cafelite.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.RawMaterial;

@Repository
public interface RawMaterialRepository extends JpaRepository<RawMaterial, Long>{
	
	Page<RawMaterial> findByMaterialCodeContainingAllIgnoreCaseAndDeleted(String roleName, boolean deleted, Pageable page);

	@Query("SELECT s FROM RawMaterial s WHERE s.deleted = false")
	List<RawMaterial> findAllMaterial();
	
	@Query("SELECT s FROM RawMaterial s JOIN s.details d WHERE s.materialId = :material_id AND d.deleted = false")
	List<RawMaterial> findAllChildByParentId(@Param("material_id") Long id);
	
}
