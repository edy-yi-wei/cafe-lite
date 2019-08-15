package com.besoft.cafelite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Purchasing;

@Repository
public interface PurchasingRepository extends JpaRepository<Purchasing, Long>{
	
	@Query("SELECT MAX(i.purchasingNumber) FROM Purchasing i WHERE month(i.purchasingDate)= :bulan AND year(i.purchasingDate)= :tahun")
	String getLastPurchasingNumber(@Param("bulan") int bulan, @Param("tahun") int tahun);
	
	@Query("SELECT i FROM Purchasing i WHERE i.purchasingDate>= :startDate AND i.purchasingDate<= :endDate")
	Page<Purchasing> findPurchasingByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable page);
	
	@Query("SELECT SUM(i.netto) FROM Purchasing i WHERE i.purchasingDate>= :startDate AND i.purchasingDate<= :endDate")
	Double calculateTotalPurchasing(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query("SELECT i FROM Purchasing i ORDER BY i.purchasingId DESC")
	List<Purchasing> findLatestPurchasing();
}
