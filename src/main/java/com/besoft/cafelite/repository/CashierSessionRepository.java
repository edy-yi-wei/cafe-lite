package com.besoft.cafelite.repository;

import java.util.Date;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.CashierSession;

@Repository
public interface CashierSessionRepository extends JpaRepository<CashierSession, Long>{
	
	@Query("SELECT c FROM CashierSession c WHERE c.cashier.userName = :userName AND YEAR(c.loginTime) = YEAR(:loginDate) "
			+ " AND MONTH(c.loginTime) = MONTH(:loginDate) AND DATE(c.loginTime) = DATE(:loginDate) AND c.logoutTime IS NULL")
	List<CashierSession> findByCashier(@Param("userName") String userName, @Param("loginDate") Date loginDate);
	
	@Query("SELECT c FROM CashierSession c WHERE c.loginTime >= :startDate AND c.loginTime <= :endDate")
	Page<CashierSession> findByPeriod(@Param("startDate") Date startDate, @Param("endDate") Date endDate, Pageable page);
	
	@Query("SELECT SUM(s.cashAmount) FROM CashierSession s WHERE s.loginTime >= :startDate AND s.loginTime <= :endDate")
	Double calculateTotalSession(@Param("startDate") Date startDate, @Param("endDate") Date endDate);
}
