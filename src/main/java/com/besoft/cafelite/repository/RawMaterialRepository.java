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
	
	Page<RawMaterial> findByMaterialNameContainingAllIgnoreCaseAndDeleted(String search, boolean deleted, Pageable page);

	@Query("SELECT s FROM RawMaterial s WHERE s.deleted = false")
	List<RawMaterial> findAllMaterial();
	
	@Query("SELECT s FROM RawMaterial s JOIN s.details d WHERE s.materialId = :material_id AND d.deleted = false")
	List<RawMaterial> findAllChildByParentId(@Param("material_id") Long id);
	
	@Query(value = "SELECT * FROM tbl_material a LEFT JOIN tbl_material_detail b ON a.material_id = b.mat_id WHERE a.deleted = false AND b.mat_id IS NULL", nativeQuery = true)
	List<RawMaterial> findAllParent();
	
	@Query(value = "SELECT * FROM tbl_material a LEFT JOIN tbl_material_detail b ON a.material_id = b.mat_id WHERE a.deleted = false AND b.mat_id IS NULL AND (a.material_code LIKE %:materialCode% OR a.material_name LIKE %:materialName%)", nativeQuery = true)
	Page<RawMaterial> findAllParent(@Param("materialCode") String materialCode, @Param("materialName") String materialName, Pageable page);
	
	
	@Query(value = "SELECT * FROM tbl_material a LEFT JOIN tbl_material_detail b ON a.material_id = b.material_id WHERE a.deleted = false AND b.material_id IS NULL", nativeQuery = true)
	List<RawMaterial> findAllStock();
	
	@Query(value = "SELECT * FROM tbl_material a LEFT JOIN tbl_material_detail b ON a.material_id = b.material_id WHERE a.stockable = :stockable AND a.deleted = false AND b.material_id IS NULL", nativeQuery = true)
	List<RawMaterial> findAllStock(@Param("stockable") boolean stockable);
	
	@Query(value = "SELECT * FROM tbl_material a LEFT JOIN tbl_material_detail b ON a.material_id = b.material_id WHERE a.deleted = false AND b.material_id IS NULL AND (a.material_code LIKE %:materialCode% OR a.material_name LIKE %:materialName%)", nativeQuery = true)
	Page<RawMaterial> findAllStock(@Param("materialCode") String materialCode, @Param("materialName") String materialName, Pageable page);
	
	@Query(value = "SELECT * FROM tbl_material a LEFT JOIN tbl_material_detail b ON a.material_id = b.material_id WHERE a.stockable = :stockable AND a.deleted = false AND b.material_id IS NULL AND (a.material_code LIKE %:materialCode% OR a.material_name LIKE %:materialName%)", nativeQuery = true)
	Page<RawMaterial> findAllStock(@Param("stockable") boolean stockable, @Param("materialCode") String materialCode, @Param("materialName") String materialName, Pageable page);
}
