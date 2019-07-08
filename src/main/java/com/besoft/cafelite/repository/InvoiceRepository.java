package com.besoft.cafelite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Invoice;

@Repository
public interface InvoiceRepository extends JpaRepository<Invoice, Long>{
	
	@Query("SELECT MAX(i.invoiceNumber) FROM Invoice i WHERE month(i.invoiceDate)= :bulan AND year(i.invoiceDate)= :tahun")
	String getLastInvoiceNumber(@Param("bulan") int bulan, @Param("tahun") int tahun);
	
	@Query("SELECT i FROM Invoice i WHERE i.invoiceDate>= :startDate AND i.invoiceDate<= :endDate")
	Page<Invoice> findInvoiceByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable page);
	
	@Query("SELECT SUM(i.total) FROM Invoice i WHERE i.invoiceDate>= :startDate AND i.invoiceDate<= :endDate")
	Double calculateTotalInvoice(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
	
//	@Query("SELECT d.menu, d.quantity FROM Invoice i JOIN i.details d WHERE i.invoiceDate>= :startDate AND i.invoiceDate<= :endDate")
//	Page<SoldMenu> calculateSoldMenu(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable page);
	
	@Query("SELECT i FROM Invoice i ORDER BY i.invoiceId DESC")
	List<Invoice> findLastestInvoice();
}
