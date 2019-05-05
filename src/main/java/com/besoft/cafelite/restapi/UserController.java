package com.besoft.cafelite.restapi;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.cafelite.model.User;
import com.besoft.cafelite.service.UserService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class UserController {
	@Autowired
	private UserService service;
	
	@RequestMapping(value = "/user", method = RequestMethod.POST)
	public HttpStatus saveUser(@RequestBody User user) {
		return service.save(user)? HttpStatus.CREATED: HttpStatus.BAD_REQUEST;
	}
	
	@RequestMapping(value = "/user", method = RequestMethod.GET)
	public Page<User> selectUser(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "pageNumber", required = true) int pageNumber){
		Page<User> list = service.selectUser(search, pageNumber, Constant.ROW_PER_PAGE);
		return list;
	}
	
	@RequestMapping(value = "/user/{id}", method = RequestMethod.GET)
	public User getUser(@PathVariable("id") Long id) {
		return service.getUser(id);
	}
	
}
