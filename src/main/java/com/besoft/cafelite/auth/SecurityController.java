package com.besoft.cafelite.auth;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.cafelite.model.User;

@RestController
@RequestMapping("/cafelite")
public class SecurityController {
	@Autowired
	private SecurityService service;
//	@Autowired
//	private UserService userService;
//	@Autowired
//	private CashierSessionService sessionService;
	
	@RequestMapping(value = "/security/login", method = RequestMethod.POST)
	public ResponseEntity<List<String>> login(@RequestBody User user) {
		List<String> result = new ArrayList<>();
		try {
			String token = service.login(user.getUserName(), user.getUserPassword());
			if(token!=null) {				
				result.add("User "+user.getUserName()+ " berhasil login!");
				return new ResponseEntity<List<String>>(result, HttpStatus.OK);
			} else {
				result.add("User name atau password Anda tidak cocok!");
				return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.add(ex.getMessage());
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/security/logout", method = RequestMethod.POST)
	public ResponseEntity<List<String>> logout() {
		List<String> result = new ArrayList<>();
		try {
			service.logout();
			result.add("berhasil logout!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			ex.printStackTrace();
			result.add(ex.getMessage());
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
}
