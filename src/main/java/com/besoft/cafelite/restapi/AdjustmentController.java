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
import com.besoft.cafelite.model.Adjustment;
import com.besoft.cafelite.model.AdjustmentDetail;
import com.besoft.cafelite.model.RawMaterial;
import com.besoft.cafelite.model.RawMaterialDetail;
import com.besoft.cafelite.model.User;
import com.besoft.cafelite.service.AdjustmentService;
import com.besoft.cafelite.service.RawMaterialService;
import com.besoft.cafelite.service.UserService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class AdjustmentController {
	@Autowired
	private AdjustmentService service;
	@Autowired
	private RawMaterialService materialService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private SecurityService securityService;
	@Autowired
	private UserService userService;
	
	@RequestMapping(value = "/adjustments", method = RequestMethod.POST)
	public ResponseEntity<List<String>> saveAdjustment(@Valid @RequestBody Adjustment adjustment) {
		List<String> result = new ArrayList<>();
		try {
			String loginUser = securityService.getLoginUser();
			
			User user = userService.getUser(loginUser);
			adjustment.setUser(user);
			service.save(adjustment);

			result.add("Adjustment is saved successfully!");
			return new ResponseEntity<List<String>>(result, HttpStatus.OK);
		} catch(Exception ex) {
			ex.printStackTrace();
			result.add(ex.getMessage());
			return new ResponseEntity<List<String>>(result, HttpStatus.BAD_REQUEST);
		}		
	}
	
//	@RequestMapping(value = "/adjustments", method = RequestMethod.GET)
//	public List<Object> selectInvoice(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy HH:mm:ss z") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy HH:mm:ss z") Date endDate, 
//			@RequestParam(name = "page", required = true) int pageNumber){
//		List<Object> result = new ArrayList<>();
//		
//		try {
//			Page<Adjustment> list = service.selectAdjustment(startDate, endDate, pageNumber, Constant.REPORT_ROW_PER_PAGE);
//			List<InvoiceForList> invoices = list.getContent().stream().map(inv -> convertToDto(inv)).collect(Collectors.toList());
////		
//		} catch(Exception ex) {
//			result.add(ex.getMessage());
//		}
//		return result;
//	}
	
	@RequestMapping(value = "/adjustments/{id}", method = RequestMethod.GET)
	public Adjustment getAdjustment(@PathVariable("id") Long id) {
		try {
			return service.getAdjustment(id);
		} catch (Exception ex) {
			ex.printStackTrace();
			return null;
		}
	}
	
	
}
