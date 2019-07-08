package com.besoft.cafelite.restapi;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.cafelite.model.Role;
import com.besoft.cafelite.model.User;
import com.besoft.cafelite.service.UserService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class UserController {
	@Autowired
	private UserService service;
	
	@RequestMapping(value = "/users", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveUser(@RequestBody User user) {
		List<String> result = new ArrayList<>();
		try {
			service.save(user);
			result.add("User is saved successfully!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to save User");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}	
	}

	@RequestMapping(value = "/users/{id}", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> deleteUser(@PathVariable("id") Long id) {
		List<String> result = new ArrayList<>();
		try {
			User user = service.delete(id);
			result.add("User " +user.getUserName()+ " is deleted!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to delete user");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/users", method = RequestMethod.GET)
	public Page<User> selectUser(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = true) int pageNumber){
		try {
//			if(!securityService.isAuthenticatedMenu(Constant.MENU_USER_NAME)) {
//				throw new Exception("Anda tidak memiliki hak dengan menu ini!");
//			}
			Page<User> list = service.selectUser(search, pageNumber, Constant.ROW_PER_PAGE);
			return list;
		} catch(Exception ex) {
			return null;
		}
	}
	
	@RequestMapping(value = "/users/role", method = RequestMethod.GET)
	public Role getRoleUser(@RequestParam(name = "userName", required = true) String userName){
		try {
//			if(!securityService.isAuthenticatedMenu(Constant.MENU_USER_NAME)) {
//				throw new Exception("Anda tidak memiliki hak dengan menu ini!");
//			}
			User user = service.getUser(userName);
			return user.getRole();
		} catch(Exception ex) {
			return null;
		}
	}
	
	@RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable("id") Long id) {
		try {
//			if(!securityService.isAuthenticatedMenu(Constant.MENU_USER_NAME)) {
//				throw new Exception("Anda tidak memiliki hak dengan menu ini!");
//			}
			return service.getUser(id);
		} catch(Exception ex) {
			return null;
		}
	}
	
}
