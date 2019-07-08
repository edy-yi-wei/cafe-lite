package com.besoft.cafelite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Menu;
import com.besoft.cafelite.repository.MenuRepository;

@Service
public class MenuService {
	@Autowired
	private MenuRepository repo;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public Menu save(Menu menu) throws Exception {
		logger.info("MenuService - save");
		
		try {
			Menu entity = repo.save(menu);
			return entity;
		} catch(Exception ex) {
			logger.error("MenuService - save "+ex.getMessage());
			throw ex;
		}
	}
	
	public Menu delete(Long menuId) throws Exception {
		logger.info("MenuService - delete [id: "+menuId+"]");
		
		try {
			Menu menu = repo.findById(menuId).get();
			menu.setDeleted(true);
			repo.save(menu);
			return menu;
		} catch(Exception ex) {
			logger.error("MenuService - delete "+ex.getMessage());
			throw ex;
		}
	}
	
	public Menu getMenu(Long id) throws Exception{
		logger.info("MenuService - getMenu [id: "+id+"]");
		
		try {
			return repo.findById(id).get();
		} catch(Exception ex) {
			logger.error("MenuService - getMenu "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Menu> selectMenu(String search, int pageNumber, int pageSize) throws Exception{
		logger.info("MenuService - selectMenu [search: "+search+", page: "+pageNumber+", size: "+pageSize+"]");
		
		try {
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findByMenuNameContainingAllIgnoreCaseAndDeleted(search, false, page);
		} catch(Exception ex) {
			logger.error("MenuService - selectMenu "+ex.getMessage());
			throw ex;
		}
	}
}
