package com.besoft.cafelite.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.User;
import com.besoft.cafelite.repository.UserRepository;

@Service
public class UserService {
	@Autowired
	private UserRepository repo;
	@Autowired
	private PasswordEncoder passwordEncoder;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	public User save(User user) {
		logger.info("UserService - save");
		try {
			System.out.println("id: "+user.getUserId());
			String pass = passwordEncoder.encode(user.getUserPassword());
			user.setUserPassword(pass);
			return repo.save(user);
		} catch(Exception ex) {
			logger.info("ERROR UserService - save " + ex.getMessage());
			throw ex;
		}
		
	}

	public User delete(Long id) throws Exception {
		logger.info("UserService - delete [id: "+id+"]");
		try {
			User user = repo.findById(id).get();
			user.setDeleted(true);
			repo.save(user);
			return user;
		} catch(Exception ex) {
			throw ex;
		}
	}
	
	public User getUser(Long id) throws Exception {
		logger.info("UserService - getUser [id: "+id+"]");
		
		try {
			User user = repo.findById(id).get();
			user.getRole();
			return user;
		} catch(Exception ex) {
			logger.error("UserService - getUser "+ex.getMessage());
			throw ex;
		}
	}
	
	public User getUser(String userName) throws Exception {
		logger.info("UserService - getUser [user name: "+userName+"]");
		
		try {
			User user = repo.findByUserNameAndDeleted(userName, false);
			user.getRole();
			return user;
		} catch(Exception ex) {
			logger.error("UserService - getUser "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<User> selectUser(String search, int pageNumber, int pageSize) throws Exception {
		logger.info("UserService - selectUser [search: "+search+", page: "+pageNumber+", size: "+pageSize+"]");
		
		try {
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findByUserNameContainingAllIgnoreCaseAndDeleted(search, false, page);
		} catch(Exception ex) {
			logger.error("UserService - selectUser "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<User> selectUserByRole(String search, int pageNumber, int pageSize) throws Exception {
		logger.info("UserService - selectUser [search: "+search+", page: "+pageNumber+", size: "+pageSize+"]");
		
		try {
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findByRoleName(search, page);
		} catch(Exception ex) {
			logger.error("UserService - selectUserByRole " + ex.getMessage());
			throw ex;
		}
	}
	
}
