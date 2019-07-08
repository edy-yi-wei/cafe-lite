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

import com.besoft.cafelite.model.Role;
import com.besoft.cafelite.repository.RoleRepository;

@Service
public class RoleService {
	@Autowired
	private RoleRepository repo;
	
	private Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Role save(Role role) throws Exception {
		logger.info("RoleService - save");
		try {
			return repo.save(role);
		} catch(Exception ex) {
			logger.info("ERROR RoleService - save " + ex.getMessage());
			throw ex;
		}
	}

	public Role delete(Long id) throws Exception {
		logger.info("RoleService - deleted [id: "+id+"]");
		try {
			Role role = repo.findById(id).get();
			role.setDeleted(true);
			repo.save(role);
			return role;
		} catch(Exception ex) {
			logger.error("RoleService - delete "+ex.getMessage());
			throw ex;
		}
	}
	
	public Role getRole(Long id) throws Exception {
		logger.info("RoleService - getRole [id: "+id+"]");
		
		try {
			return repo.findById(id).get();
		} catch(Exception ex) {
			logger.error("RoleService - getRole "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Role> selectRole(String search, int pageNumber, int pageSize) throws Exception {
		logger.info("RoleService - selectRole [search: "+search+", page: "+pageNumber+", size: "+pageSize+"]");
		
		try {
			if(pageNumber == 0) {
				List<Role> list = repo.findAllRole();
				Page<Role> result = new PageImpl<>(list);
				return result;
			} else {
				Pageable page = PageRequest.of(pageNumber-1, pageSize);
				return repo.findByRoleNameContainingAllIgnoreCaseAndDeleted(search, false, page);
			}
		} catch(Exception ex) {
			logger.error("RoleService - selectRole "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Role> selectRoleByModuleName(String search, int pageNumber, int pageSize) throws Exception {
		logger.info("RoleService - selectRoleByModuleName [search: "+search+", page: "+pageNumber+", size: "+pageSize+"]");
		
		try {
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findByModuleName(search, page);
		} catch(Exception ex) {
			logger.error("RoleService - selectRoleByModuleName "+ex.getMessage());
			throw ex;
		}
	}
}
