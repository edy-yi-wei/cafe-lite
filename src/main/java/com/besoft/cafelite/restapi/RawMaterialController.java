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

import com.besoft.cafelite.model.RawMaterial;
import com.besoft.cafelite.service.RawMaterialService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class RawMaterialController {
	@Autowired
	private RawMaterialService service;
	
	@RequestMapping(value = "/materials", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveMaterial(@Valid @RequestBody RawMaterial rawMaterial) {
		List<String> result = new ArrayList<>();
		try {
			service.save(rawMaterial);
			result.add("Material is saved successfully!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to save material");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}	
	}
	
	@RequestMapping(value = "/materials", method = RequestMethod.GET)
	public Page<RawMaterial> selectMaterial(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = true) int pageNumber){
		Page<RawMaterial> list = null;
		try {
			list = service.selectMaterial(search, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/materials/parent", method = RequestMethod.GET)
	public List<RawMaterial> selectMaterialParent(){
		List<RawMaterial> list = null;
		try {
			list = service.getMaterialParentOnly();
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/materials/{id}", method = RequestMethod.GET)
	public RawMaterial getMaterial(@PathVariable("id") Long id) {
		try {
			return service.getMaterial(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/materials/{id}", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> deleteSupplier(@PathVariable("id") Long id) {
		List<String> result = new ArrayList<>();
		try {
			RawMaterial rawMaterial = service.delete(id);
			result.add("Material " +rawMaterial.getMaterialName()+ " is deleted!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to delete material");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
}
