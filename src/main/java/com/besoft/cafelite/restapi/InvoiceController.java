package com.besoft.cafelite.restapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.cafelite.auth.SecurityService;
import com.besoft.cafelite.dto.InvoiceForList;
import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.service.InvoiceService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class InvoiceController {
	@Autowired
	private InvoiceService service;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private SecurityService securityService;
	
	@RequestMapping(value = "/invoices", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveInvoice(@Valid @RequestBody Invoice invoice) {
		List<String> result = new ArrayList<>();
		try {
			String loginUser = securityService.getLoginUser();
			service.save(invoice, loginUser);
			result.add("Tekan Close untuk transaksi baru!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			ex.printStackTrace();
			result.add(ex.getMessage());
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}		
	}
	
	@RequestMapping(value = "/invoices", method = RequestMethod.GET)
	public List<Object> selectInvoice(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy HH:mm:ss z") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy HH:mm:ss z") Date endDate, 
			@RequestParam(name = "page", required = true) int pageNumber){
		List<Object> result = new ArrayList<>();
		
		try {
			Page<Invoice> list = service.selectInvoice(startDate, endDate, pageNumber, Constant.REPORT_ROW_PER_PAGE);
			List<InvoiceForList> invoices = list.getContent().stream().map(inv -> convertToDto(inv)).collect(Collectors.toList());
//			List<InvoiceForList> invoices = null;
			Page<InvoiceForList> hasil = new PageImpl<InvoiceForList>(invoices, list.getPageable(), list.getTotalElements());
			Double total = service.calculateTotalInvoice(startDate, endDate);
			result.add(hasil);
			result.add(total);
		} catch(Exception ex) {
			result.add(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/invoices/{id}", method = RequestMethod.GET)
	public Invoice getInvoice(@PathVariable("id") Long id) {
		try {
			return service.getInvoice(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	@RequestMapping(value = "/invoices/closing", method = RequestMethod.POST)
	public ResponseEntity<List<String>> doClosing(@RequestParam(name = "userName", required = true) String userName, @RequestParam(name = "userPassword", required = true) String userPassword) {
		List<String> result = new ArrayList<>();
		try {
			UserDetails userDetails = securityService.isAuthenticated(userName, userPassword);
			String loginUser = securityService.getLoginUser();
			if(userDetails!=null && loginUser.equalsIgnoreCase(userName)) {
				service.doClosing(loginUser);
				result.add("Closing berhasil!");
				return new ResponseEntity<List<String>>(result, HttpStatus.OK);
			} else {
				result.add("User name atau password Anda salah!");
				return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
			}
			
		} catch(Exception ex) {
			ex.printStackTrace();
			result.add(ex.getMessage());
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}		
	}
	
	private InvoiceForList convertToDto(Invoice invoice) {
		InvoiceForList inv = modelMapper.map(invoice, InvoiceForList.class);
		return inv;
	}
	
	@RequestMapping(value = "/invoices/reprint", method = RequestMethod.GET)
	public void reprintInvoice(@RequestParam(name = "userName") String userName) {
		try {
			service.rePrintInvoice(userName);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
	}
}
