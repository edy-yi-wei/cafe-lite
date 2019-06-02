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

import com.besoft.cafelite.model.Module;
import com.besoft.cafelite.service.ModuleService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class ModuleController {
	@Autowired
	private ModuleService service;
	
	@RequestMapping(value = "/modules", method = RequestMethod.POST)
	public ResponseEntity<String> saveModule(@Valid @RequestBody Module module) {
		if(service.save(module)) {
			return new ResponseEntity<String>("Module is saved successfully!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail to save Module", HttpStatus.BAD_REQUEST);
		}		
	}
	
	@RequestMapping(value = "/modules", method = RequestMethod.GET)
	public Page<Module> selectModule(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "pageNumber", required = true) int pageNumber){
		Page<Module> list = service.selectModule(search, pageNumber, Constant.ROW_PER_PAGE);
		return list;
	}
	
	@RequestMapping(value = "/modules/{id}", method = RequestMethod.GET)
	public Module getModule(@PathVariable("id") Long id) {
		return service.getModule(id);
	}
	
}
