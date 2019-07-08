package com.besoft.cafelite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.CashierSession;
import com.besoft.cafelite.model.Invoice;

@Repository
public interface ReportRepository extends JpaRepository<Invoice, Long>{
	
	@Query("SELECT i FROM Invoice i WHERE i.invoiceDate>= :startDate AND i.invoiceDate<= :endDate")
	List<Invoice> findInvoiceByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
	@Query("SELECT c FROM CashierSession c WHERE c.loginTime >= :startDate AND c.loginTime <= :endDate")
	List<CashierSession> findCashierSessionByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
