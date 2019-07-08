package com.besoft.cafelite.service;

import java.sql.Timestamp;
import java.util.Calendar;
import java.util.Date;
import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.CashierSession;
import com.besoft.cafelite.repository.CashierSessionRepository;

@Service
public class CashierSessionService {
	@Autowired
	private CashierSessionRepository repo;

	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public void save(CashierSession session) throws Exception {
		logger.info("CashierSession - save");
		repo.save(session);
		logger.info("New session for "+session.getCashier().getUserName()+" created!");
	}
	
	public CashierSession getCashierSession(String userName) throws Exception{
		logger.info("CashierSessionService - getCashierSession [user name: " +userName+"]");
		
		try {
			List<CashierSession> sessions = repo.findByCashier(userName, new Date());
			if(sessions!=null && !sessions.isEmpty()) {
				return sessions.get(0);
			} else {
				return null;
			}
		} catch(Exception ex) {
			logger.error("CashierSession - getCashierSession "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<CashierSession> selectCashierSession(Date startDate, Date endDate, int pageNumber, int pageSize) throws Exception{
		logger.info("CashierSessionService - selectCashierSession [start date: "+startDate+ ", end date: "+endDate+", page number: "+pageNumber+", page size: "+pageSize+"]");
	
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findByPeriod(startDate, endDate, page);
		} catch(Exception ex) {
			logger.error("CashierSessionService - selectCashierSession "+ex.getMessage());
			throw ex;
		}
	}
	
	public void updateCashierSession(String userName, Double amount) throws Exception{
		logger.info("CashierSessionService - updateCashierSession [user name: "+userName+ " amount: "+amount+"]");
		
		try {
			List<CashierSession> sessions = repo.findByCashier(userName, new Date());		
			if(sessions!=null && !sessions.isEmpty()) {
				CashierSession session =sessions.get(0);
				Double nilai = session.getCashAmount();
				nilai += amount;
				session.setCashAmount(nilai);
				repo.save(session);
			} else {
				throw new Exception("Session untuk kasir "+userName+" sudah expired!.\nSilahkan logout dan login lagi!");
			}
		} catch(Exception ex) {
			logger.error("CashierSessionService - updateCashierSession "+ex.getMessage());
			throw ex;
		}
	}
	
	public CashierSession doClosing(String userName) throws Exception{
		logger.info("CashierSessionService - doClosing");
		
		try {
			CashierSession session = getCashierSession(userName);
			if(session!=null) {
				session.setLogoutTime(new Timestamp(new Date().getTime()));
			}
			return repo.save(session);
		} catch(Exception ex) {
			logger.error("CashierSessionService - doClosing "+ex.getMessage());
			throw ex;
		}
	}
	
	public double calculateTotalSession(Date startDate, Date endDate) throws Exception{
		logger.info("CashierSessionService - calculateTotalSession [start date: "+startDate+", end date: "+endDate+"]");
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			return repo.calculateTotalSession(startDate, endDate);
		} catch(Exception ex) {
			logger.error("CashierSessionService - calculateTotalSession "+ex.getMessage());
			throw ex;
		}
	}
}
