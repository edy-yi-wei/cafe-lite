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

import com.besoft.cafelite.model.Supplier;
import com.besoft.cafelite.service.SupplierService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class SupplierController {
	@Autowired
	private SupplierService service;
	
	@RequestMapping(value = "/suppliers", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveSupplier(@Valid @RequestBody Supplier supplier) {
		List<String> result = new ArrayList<>();
		try {
			service.save(supplier);
			result.add("Supplier is saved successfully!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to save Supplier");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}	
	}
	
	@RequestMapping(value = "/suppliers", method = RequestMethod.GET)
	public Page<Supplier> selectSupplier(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "page", required = true) int pageNumber){
		Page<Supplier> list = null;
		try {
			list = service.selectSupplier(search, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/suppliers/{id}", method = RequestMethod.GET)
	public Supplier getSupplier(@PathVariable("id") Long id) {
		try {
			return service.getSupplier(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}

	@RequestMapping(value = "/suppliers/{id}", method = RequestMethod.PUT)
	public ResponseEntity<List<String>> deleteSupplier(@PathVariable("id") Long id) {
		List<String> result = new ArrayList<>();
		try {
			Supplier supplier = service.delete(id);
			result.add("Supplier " +supplier.getSupplierName()+ " is deleted!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			result.add("Fail to delete supplier");
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}
	}
}
