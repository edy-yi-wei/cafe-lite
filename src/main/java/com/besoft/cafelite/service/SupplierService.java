package com.besoft.cafelite.service;

import java.util.List;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Supplier;
import com.besoft.cafelite.repository.SupplierRepository;

@Service
public class SupplierService {
	@Autowired
	private SupplierRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String className = getClass().getName();
	
	@Transactional(rollbackOn = Exception.class)
	public Supplier save(Supplier supplier) {
		logger.info(String.format("%s - save", className));
		try {
			System.out.println("id: "+supplier.getSupplierId());
			return repo.save(supplier);
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - save %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
		
	}

	@Transactional(rollbackOn = Exception.class)
	public Supplier delete(Long id) throws Exception {
		logger.info(String.format("%s - delete [id: %s]", new Object[] {className, id}));
		try {
			Supplier supplier = repo.findById(id).get();
			supplier.setDeleted(true);
			repo.save(supplier);
			return supplier;
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - delete [id: %i] %s", new Object[] {className, id, ex.getMessage()}));
			throw ex;
		}
	}
	
	public Supplier getSupplier(Long id) throws Exception {
		logger.info(String.format("%s - getSupplier [id: %s]", new Object[] {className, id}));
		try {
			return repo.findById(id).get();
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - getSupplier [id: %s] %s", new Object[] {className, id, ex.getMessage()}));
			throw ex;
		}
	}
	
	public Page<Supplier> selectSupplier(String search, int pageNumber, int pageSize) throws Exception {
		logger.info(String.format("%s - selectSupplier [[search: %s, page: %s, size: %s]", new Object[] {className, search, pageNumber, pageSize}));
		
		try {
			if(pageNumber == 0) {
				List<Supplier> list = repo.findAllSupplier();
				Page<Supplier> result = new PageImpl<>(list);
				return result;
			} else {
				Pageable page = PageRequest.of(pageNumber-1, pageSize);
				return repo.findBySupplierNameContainingAllIgnoreCaseAndDeleted(search, false, page);
			}
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - selectSupplier %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
	}
	
}
