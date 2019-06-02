package com.besoft.cafelite.restapi;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.service.InvoiceService;

@RestController
@RequestMapping("/cafelite")
public class InvoiceController {
	@Autowired
	private InvoiceService service;
	
	@RequestMapping(value = "/invoices", method = RequestMethod.POST)
	public ResponseEntity<String> saveInvoice(@Valid @RequestBody Invoice invoice) {
		if(service.save(invoice)) {
			return new ResponseEntity<String>("Invoice is saved successfully!", HttpStatus.OK);
		} else {
			return new ResponseEntity<String>("Fail to save Invoice", HttpStatus.BAD_REQUEST);
		}		
	}
	
//	@RequestMapping(value = "/invoices", method = RequestMethod.GET)
//	public Page<Invoice> selectInvoice(@RequestParam(name = "search", required = false) String search, @RequestParam(name = "pageNumber", required = true) int pageNumber){
//		Page<Invoice> list = service.selectInvoice(search, pageNumber, Constant.ROW_PER_PAGE);
//		return list;
//	}
	
	@RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
	public Invoice getInvoice(@PathVariable("id") Long id) {
		return service.getInvoice(id);
	}
	
}
