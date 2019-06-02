package com.besoft.cafelite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.User;
import com.besoft.cafelite.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;
	
	public boolean save(User user) {
		return repo.save(user)!=null;
	}
	
	public User getUser(Long id) {
		return repo.findById(id).get();
	}
	
	public Page<User> selectUser(String search, int pageNumber, int pageSize){
		Pageable page = PageRequest.of(pageNumber-1, pageSize);
		return repo.findByUserNameContainingAllIgnoreCase(search, page);
	}
	
	public Page<User> selectUserByRole(String search, int pageNumber, int pageSize){
		Pageable page = PageRequest.of(pageNumber-1, pageSize);
		return repo.findByRoleName(search, page);
	}
}
