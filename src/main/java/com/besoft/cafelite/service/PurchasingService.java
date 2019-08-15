package com.besoft.cafelite.service;

import java.util.Calendar;
import java.util.Date;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Purchasing;
import com.besoft.cafelite.repository.PurchasingRepository;


@Service
public class PurchasingService {
	@Autowired
	private PurchasingRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional(rollbackOn = Exception.class)
	public void save(Purchasing purchasing) throws Exception {
		logger.info("PurchasingService - save");
		try {
			purchasing.setPurchasingDate(new Date());
			purchasing.setPurchasingNumber(generateAutoNumber());
			purchasing.setAmount(purchasing.calculateTotal());
			Double discount = purchasing.getAmount() * (purchasing.getDiscount() / 100);
			purchasing.setNetto(purchasing.getAmount() - discount);
			repo.save(purchasing);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	private String generateAutoNumber() throws Exception{
		logger.info("PurchasingService - generateAutoNumber");
		
		try {
			String number = "";
			Date tanggal = new Date();
			int bulan = tanggal.getMonth()+1;
			int tahun = tanggal.getYear()+1900;
			String result = repo.getLastPurchasingNumber(bulan, tahun);
			Long nomor = result==null?1:Long.parseLong(result.substring(8, 13)) + 1;
			number = "PO" + String.valueOf(tahun) + (bulan>9?bulan:"0"+bulan) + String.format("%05d", nomor);
			return number;
		} catch(Exception ex) {
			logger.error("PurchasingService - generateAutoNumber "+ex.getMessage());
			throw ex;
		}
	}
	
	public Purchasing getPurchasing(Long id) throws Exception {
		logger.info("PurchasingService - getPurchasing [id: "+id+"]");
		
		try {
			Purchasing purchasing = repo.findById(id).get();
			return purchasing;
		} catch(Exception ex) {
			logger.error("PurchasingService - getPurchasing "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Purchasing> selectPurchasing(Date startDate, Date endDate, int pageNumber, int pageSize) throws Exception {
		logger.info("PurchasingService - selectPurchasing[start date: "+startDate+ ", end date: "+endDate+", page number: "+pageNumber+", page size: "+pageSize+"]");
	
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findPurchasingByPeriod(startDate, endDate, page);
		} catch(Exception ex) {
			logger.error("PurchasingService - selectPurchasing "+ex.getMessage());
			throw ex;
		}
	}
		
	public double calculateTotalPurchasing(Date startDate, Date endDate) throws Exception {
		logger.info("PurchasingService - calculateTotalPurchasing");
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			return repo.calculateTotalPurchasing(startDate, endDate);
		} catch(Exception ex) {
			logger.error("PurchasingService - calculateTotalPurchasing "+ex.getMessage());
			throw ex;
		}
	}
	
}
