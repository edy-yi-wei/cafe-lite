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

import com.besoft.cafelite.model.Menu;
import com.besoft.cafelite.service.MenuService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class MenuController {
	@Autowired
	private MenuService service;
	
	@RequestMapping(value = "/menus", method = RequestMethod.POST)
	public ResponseEntity<String> saveMenu(@Valid @RequestBody Menu menu) {
		if(service.save(menu)) {
			return new ResponseEntity<String>("Menu is saved successfully!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail to save Menu", HttpStatus.BAD_REQUEST);
		}		
	}
	
	@RequestMapping(value = "/menus", method = RequestMethod.GET)
	public Page<Menu> selectMenu(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "pageNumber", required = true) int pageNumber){
		Page<Menu> list = service.selectMenu(search, pageNumber, Constant.ROW_PER_PAGE);
		return list;
	}
	
	@RequestMapping(value = "/menus/{id}", method = RequestMethod.GET)
	public Menu getMenu(@PathVariable("id") Long id) {
		return service.getMenu(id);
	}
	
}
