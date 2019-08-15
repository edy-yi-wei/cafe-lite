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

import com.besoft.cafelite.model.Adjustment;
import com.besoft.cafelite.repository.AdjustmentRepository;



@Service
public class AdjustmentService {
	@Autowired
	private AdjustmentRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional(rollbackOn = Exception.class)
	public void save(Adjustment adjustment) throws Exception {
		logger.info("AdjustmentService - save");
		try {
			adjustment.setAdjustmentDate(new Date());
			adjustment.setAdjustmentNumber(generateAutoNumber());
			repo.save(adjustment);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	private String generateAutoNumber() throws Exception{
		logger.info("AdjustmentService - generateAutoNumber");
		
		try {
			String number = "";
			Date tanggal = new Date();
			int bulan = tanggal.getMonth()+1;
			int tahun = tanggal.getYear()+1900;
			String result = repo.getLastAdjustmentNumber(bulan, tahun);
			Long nomor = result==null?1:Long.parseLong(result.substring(8, 13)) + 1;
			number = "AJ" + String.valueOf(tahun) + (bulan>9?bulan:"0"+bulan) + String.format("%05d", nomor);
			return number;
		} catch(Exception ex) {
			logger.error("AdjustmentService - generateAutoNumber "+ex.getMessage());
			throw ex;
		}
	}
	
	public Adjustment getAdjustment(Long id) throws Exception {
		logger.info("AdjustmentService - getAdjustment [id: "+id+"]");
		
		try {
			Adjustment adjustment = repo.findById(id).get();
			return adjustment;
		} catch(Exception ex) {
			logger.error("AdjustmentService - getAdjustment "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Adjustment> selectAdjustment(Date startDate, Date endDate, int pageNumber, int pageSize) throws Exception {
		logger.info("AdjustmentService - selectAdjustment[start date: "+startDate+ ", end date: "+endDate+", page number: "+pageNumber+", page size: "+pageSize+"]");
	
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findAdjustmentByPeriod(startDate, endDate, page);
		} catch(Exception ex) {
			logger.error("AdjustmentService - selectAdjustment "+ex.getMessage());
			throw ex;
		}
	}
	
}
