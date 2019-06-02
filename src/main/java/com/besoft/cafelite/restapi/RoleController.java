package com.besoft.cafelite.restapi;

import javax.validation.Valid;

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
import com.besoft.cafelite.service.RoleService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class RoleController {
	@Autowired
	private RoleService service;
	
	@RequestMapping(value = "/roles", method = RequestMethod.POST)
	public ResponseEntity<String> saveRole(@Valid @RequestBody Role role) {
		if(service.save(role)) {
			return new ResponseEntity<String>("Role is saved successfully!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail to save Role", HttpStatus.BAD_REQUEST);
		}		
	}
	
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public Page<Role> selectRole(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "pageNumber", required = true) int pageNumber){
		Page<Role> list = service.selectRole(search, pageNumber, Constant.ROW_PER_PAGE);
		return list;
	}
	
	@RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
	public Role getRole(@PathVariable("id") Long id) {
		return service.getRole(id);
	}
	
}
