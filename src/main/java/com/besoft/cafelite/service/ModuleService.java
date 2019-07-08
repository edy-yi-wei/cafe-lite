package com.besoft.cafelite.service;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Module;
import com.besoft.cafelite.repository.ModuleRepository;

@Service
public class ModuleService {
	@Autowired
	private ModuleRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Module save(Module module) throws Exception {
		logger.info("ModuleService - save");
		try {
			return repo.save(module);
		} catch(Exception ex) {
			logger.info("ERROR ModuleService - save "+ex.getMessage());
			throw ex;
		}
	}

	public Module delete(Long id) throws Exception {
		logger.info("ModuleService - delete [id: "+id+"]");
		try {
			Module module = repo.findById(id).get();
			module.setDeleted(true);
			repo.save(module);
			return module;
		} catch(Exception ex) {
			logger.error("ModuleService - delete "+ex.getMessage());
			throw ex;
		}
	}
	
	public Module getModule(Long id) throws Exception {
		logger.info("ModuleService - getModule [id: "+id+"]");
		
		try {
			return repo.findById(id).get();
		} catch(Exception ex) {
			logger.error("ModuleService - getModule "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Module> selectModule(String search, int pageNumber, int pageSize) throws Exception {
		logger.info("ModuleService - selectModule [search: "+search+", page: "+pageNumber+", size: "+pageSize+"]");
		
		try {
			if(pageNumber == 0) {
				List<Module> list = repo.findAllModule();
				Page<Module> result = new PageImpl<>(list);
				return result;
			} else {
				Pageable page = PageRequest.of(pageNumber-1, pageSize);
				return repo.findByModuleCodeContainingOrModuleNameContainingAllIgnoreCaseAndDeleted(search, search, page);
			}
		} catch(Exception ex) {
			logger.error("ModuleService - selectModule "+ex.getMessage());
			throw ex;
		}
	}
}
