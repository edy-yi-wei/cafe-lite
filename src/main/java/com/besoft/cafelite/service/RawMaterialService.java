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

import com.besoft.cafelite.model.RawMaterial;
import com.besoft.cafelite.model.RawMaterialDetail;
import com.besoft.cafelite.repository.RawMaterialRepository;

@Service
public class RawMaterialService {
	@Autowired
	private RawMaterialRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	private String className = getClass().getName();
	
	@Transactional(rollbackOn = Exception.class)
	public RawMaterial save(RawMaterial rawMaterial) throws Exception {
		logger.info(String.format("%s - save", className));
		try {
			System.out.println("id: "+ rawMaterial.getMaterialId());
			rawMaterial.setQuantity((double) 0);
			if(rawMaterial.getMaterialId() != null && rawMaterial.getMaterialId() > 0) {
				RawMaterial material = getMaterial(rawMaterial.getMaterialId());
				if(material != null) {
					rawMaterial.setQuantity(material.getQuantity());
				}
			}
			
			return repo.save(rawMaterial);
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - save %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
		
	}
	
	//@Transactional(rollbackOn = Exception.class)
	public Boolean insertStock(RawMaterial rawMaterial, Double qty) throws Exception {
		Boolean status = false;
		logger.info(String.format("%s - insertStock", className));
		try {
			System.out.println("id: "+ rawMaterial.getMaterialId());
			if(rawMaterial.getDetails() != null && rawMaterial.getDetails().size() > 0) {
				//insert to detail quantity, multiply with conversion quantity
				for(RawMaterialDetail matDetail : rawMaterial.getDetails()) {
					RawMaterial materialDetail = getMaterial(matDetail.getMaterial().getMaterialId());
					Double quantity = materialDetail.getQuantity() + (matDetail.getConversionQuantity() * qty);
					materialDetail.setQuantity(quantity);
					repo.save(materialDetail);
				}
			}else {
				//insert to quantity
				Double quantity = rawMaterial.getQuantity() + qty;
				rawMaterial.setQuantity(quantity);
				repo.save(rawMaterial);
			}
			status = true;
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - insertStock %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
		
		return status;
		
	}
	
	
	//@Transactional(rollbackOn = Exception.class)
	public Boolean adjustStock(RawMaterial rawMaterial) throws Exception {
		Boolean status = false;
		logger.info(String.format("%s - adjustStock", className));
		try {
			System.out.println("id: "+ rawMaterial.getMaterialId());
			Double quantity = rawMaterial.getQuantity();
			rawMaterial.setQuantity(quantity);
			repo.save(rawMaterial);
			status = true;
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - adjustStock %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
		
		return status;
		
	}
	
	

	@Transactional(rollbackOn = Exception.class)
	public RawMaterial delete(Long id) throws Exception {
		logger.info(String.format("%s - delete [id: %s]", new Object[] {className, id}));
		try {
			RawMaterial rawMaterial = repo.findById(id).get();
			rawMaterial.setDeleted(true);
			repo.save(rawMaterial);
			return rawMaterial;
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - delete [id: %i] %s", new Object[] {className, id, ex.getMessage()}));
			throw ex;
		}
	}
	
	public RawMaterial getMaterial(Long id) throws Exception {
		logger.info(String.format("%s - getMaterial [id: %s]", new Object[] {className, id}));
		try {
			return repo.findById(id).get();
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - getMaterial [id: %s] %s", new Object[] {className, id, ex.getMessage()}));
			throw ex;
		}
	}
	
	public List<RawMaterial> getMaterialDetails(Long id) throws Exception {
		logger.info(String.format("%s - getMaterialDetails [id: %s]", new Object[] {className, id}));
		try {
			return repo.findAllChildByParentId(id);
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - getMaterialDetails [id: %s] %s", new Object[] {className, id, ex.getMessage()}));
			throw ex;
		}
	}
	
	public List<RawMaterial> getMaterialParentOnly() throws Exception {
		logger.info(String.format("%s - getMaterialParentOnly", new Object[] {className}));
		try {
			return repo.findAllParent();
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - getMaterialParentOnly %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
	}
	
	public Page<RawMaterial> selectMaterial(String search, int pageNumber, int pageSize) throws Exception {
		logger.info(String.format("%s - selectMaterial [[search: %s, page: %s, size: %s]", new Object[] {className, search, pageNumber, pageSize}));
		
		try {
			if(pageNumber == 0) {
				List<RawMaterial> list = repo.findAllMaterial();
				Page<RawMaterial> result = new PageImpl<>(list);
				return result;
			} else {
				Pageable page = PageRequest.of(pageNumber-1, pageSize);
				return repo.findByMaterialCodeContainingAllIgnoreCaseAndDeleted(search, false, page);
			}
		} catch(Exception ex) {
			logger.info(String.format("ERROR %s - selectMaterial %s", new Object[] {className, ex.getMessage()}));
			throw ex;
		}
	}
	
}
