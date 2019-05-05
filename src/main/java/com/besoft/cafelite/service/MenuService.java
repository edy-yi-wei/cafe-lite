package com.besoft.cafelite.service;

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
	
	public boolean save(Menu menu) {
		return repo.save(menu)!=null;
	}
	
	public Menu getMenu(Long id) {
		return repo.getOne(id);
	}
	
	public Page<Menu> selectMenu(String search, int pageNumber, int pageSize){
		Pageable page = PageRequest.of(pageNumber-1, pageSize);
		return repo.findByMenuNameContainingAllIgnoreCase(search, page);
	}
}
