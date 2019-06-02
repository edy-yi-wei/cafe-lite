package com.besoft.cafelite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Module;
import com.besoft.cafelite.repository.ModuleRepository;

@Service
public class ModuleService {
	@Autowired
	private ModuleRepository repo;
	
	public boolean save(Module module) {
		return repo.save(module)!=null;
	}
	
	public Module getModule(Long id) {
		return repo.findById(id).get();
	}
	
	public Page<Module> selectModule(String search, int pageNumber, int pageSize){
		Pageable page = PageRequest.of(pageNumber-1, pageSize);
		return repo.findByModuleCodeContainingOrModuleNameContainingAllIgnoreCase(search, search, page);
	}
}
