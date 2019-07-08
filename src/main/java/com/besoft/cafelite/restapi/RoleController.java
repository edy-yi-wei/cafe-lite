package com.besoft.cafelite.restapi;

import java.util.ArrayList;
import java.util.List;

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
	public ResponseEntity<List<String>> saveRole(@Valid @RequestBody Role role) {
		List<String> result = new ArrayList<>();
		try {
			service.save(role);
			result.add("Role is saved successfully!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to save Role");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}	
	}
	
	@RequestMapping(value = "/roles", method = RequestMethod.GET)
	public Page<Role> selectRole(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = true) int pageNumber){
		Page<Role> list = null;
		try {
			list = service.selectRole(search, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/roles/{id}", method = RequestMethod.GET)
	public Role getRole(@PathVariable("id") Long id) {
		try {
			return service.getRole(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/roles/{id}", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> deleteRole(@PathVariable("id") Long id) {
		List<String> result = new ArrayList<>();
		try {
			Role role = service.delete(id);
			result.add("Role " +role.getRoleName()+ " is deleted!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to delete role");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
}
