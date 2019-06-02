package com.besoft.cafelite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Role;
import com.besoft.cafelite.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository repo;
	
	public boolean save(Role role) {
		return repo.save(role)!=null;
	}
	
	public Role getRole(Long id) {
		return repo.findById(id).get();
	}
	
	public Page<Role> selectRole(String search, int pageNumber, int pageSize){
		Pageable page = PageRequest.of(pageNumber-1, pageSize);
		return repo.findByModuleName(search, page);
	}
	
	public Page<Role> selectRoleByModuleName(String search, int pageNumber, int pageSize){
		Pageable page = PageRequest.of(pageNumber-1, pageSize);
		return repo.findByModuleName(search, page);
	}
}
