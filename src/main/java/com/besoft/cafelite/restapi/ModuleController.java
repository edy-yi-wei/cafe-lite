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

import com.besoft.cafelite.model.Module;
import com.besoft.cafelite.service.ModuleService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class ModuleController {
	@Autowired
	private ModuleService service;
	
	@RequestMapping(value = "/modules", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveModule(@Valid @RequestBody Module module) {
		List<String> result = new ArrayList<>();
		try {
			service.save(module);
			result.add("Module is saved successfully!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to save Module");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}		
	}
	
	@RequestMapping(value = "/modules", method = RequestMethod.GET)
	public Page<Module> selectModule(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = true) int pageNumber){
		Page<Module> list = null;
		try {
			list = service.selectModule(search, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/modules/{id}", method = RequestMethod.GET)
	public Module getModule(@PathVariable("id") Long id) {
		try {
			return service.getModule(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/modules/{id}", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> deleteModule(@PathVariable("id") Long id) {
		List<String> result = new ArrayList<>();
		try {
			Module module = service.delete(id);
			result.add("Module " +module.getModuleName()+ " is deleted!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to delete module");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
}
