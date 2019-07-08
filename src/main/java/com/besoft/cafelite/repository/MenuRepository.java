package com.besoft.cafelite.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import com.besoft.cafelite.model.Menu;

@Repository
public interface MenuRepository extends JpaRepository<Menu, Long>{
	
	Page<Menu> findByMenuNameContainingAllIgnoreCaseAndDeleted(String menuName, boolean deleted, Pageable page);
	
}
