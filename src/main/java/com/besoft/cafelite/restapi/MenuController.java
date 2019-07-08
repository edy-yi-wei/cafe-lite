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

import com.besoft.cafelite.model.Menu;
import com.besoft.cafelite.service.MenuService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class MenuController {
	@Autowired
	private MenuService service;
	
	@RequestMapping(value = "/menus", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveMenu(@Valid @RequestBody Menu menu) {
		List<String> result = new ArrayList<>();
		
		try {			
			Menu entity = service.save(menu);
			if(entity!=null) {
				result.add("Menu "+menu.getMenuName()+" is saved successfully!");
				return new ResponseEntity<List<String>>(result, HttpStatus.OK);
			} else {
				result.add("Fail to save Menu "+menu.getMenuName());
				return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
			}
		} catch(Exception ex) {
			ex.printStackTrace();
			result.add("ERROR - "+ex.getMessage());
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
	
	@RequestMapping(value = "/menus", method = RequestMethod.GET)
	public Page<Menu> selectMenu(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = true) int pageNumber){
		Page<Menu> list = null;
		try {
			list = service.selectMenu(search, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/menus/{id}", method = RequestMethod.GET)
	public Menu getMenu(@PathVariable("id") Long id) {
		try {
			return service.getMenu(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/menus/{id}", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> deleteMenu(@PathVariable("id") Long id) {
		List<String> result = new ArrayList<>();
		try {
			Menu menu = service.delete(id);
			result.add("Menu " +menu.getMenuName()+ " is deleted!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to delete menu");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
}
