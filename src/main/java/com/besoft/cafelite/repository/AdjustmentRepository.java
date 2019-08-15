package com.besoft.cafelite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Adjustment;

@Repository
public interface AdjustmentRepository extends JpaRepository<Adjustment, Long>{
	
	@Query("SELECT MAX(i.adjustmentNumber) FROM Adjustment i WHERE month(i.adjustmentDate)= :bulan AND year(i.adjustmentDate)= :tahun")
	String getLastAdjustmentNumber(@Param("bulan") int bulan, @Param("tahun") int tahun);
	
	@Query("SELECT i FROM Adjustment i WHERE i.adjustmentDate>= :startDate AND i.adjustmentDate<= :endDate")
	Page<Adjustment> findAdjustmentByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable page);
	
	
	@Query("SELECT i FROM Adjustment i ORDER BY i.adjustmentId DESC")
	List<Adjustment> findLatestAdjustment();
}
