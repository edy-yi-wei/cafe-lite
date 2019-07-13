package com.besoft.cafelite.repository;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Supplier;;

@Repository
public interface SupplierRepository extends JpaRepository<Supplier, Long>{
	
	Page<Supplier> findBySupplierCodeContainingAllIgnoreCaseAndDeleted(String roleName, boolean deleted, Pageable page);

	@Query("SELECT s FROM Supplier s WHERE s.deleted = false")
	List<Supplier> findAllSupplier();
}
