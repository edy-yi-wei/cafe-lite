package com.besoft.cafelite.restapi;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.besoft.cafelite.dto.InvoiceForList;
import com.besoft.cafelite.dto.SoldMenu;
import com.besoft.cafelite.model.Adjustment;
import com.besoft.cafelite.model.CashierSession;
import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.model.Purchasing;
import com.besoft.cafelite.service.CashierSessionService;
import com.besoft.cafelite.service.InvoiceService;
import com.besoft.cafelite.service.ReportService;
import com.besoft.cafelite.utilities.Constant;

@RestController
@RequestMapping("/cafelite")
public class ReportController {
	@Autowired
	private InvoiceService invoiceService;
	@Autowired
	private CashierSessionService cashierSessionService;
	@Autowired
	private ModelMapper modelMapper;
	@Autowired
	private ReportService reportService;
	
	@RequestMapping(value = "/report/invoices", method = RequestMethod.GET)
	public List<Object> selectInvoice(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date endDate, 
			@RequestParam(name = "page", required = true) int pageNumber){
		List<Object> result = new ArrayList<>();
		
		try {
			Page<Invoice> list = invoiceService.selectInvoice(startDate, endDate, pageNumber, Constant.REPORT_ROW_PER_PAGE);
			List<InvoiceForList> invoices = list.getContent().stream().map(inv -> convertToDto(inv)).collect(Collectors.toList());
//			List<InvoiceForList> invoices = null;
			Page<InvoiceForList> hasil = new PageImpl<InvoiceForList>(invoices, list.getPageable(), list.getTotalElements());
			Double total = invoiceService.calculateTotalInvoice(startDate, endDate);
			result.add(hasil);
			result.add(total);
		} catch(Exception ex) {
			result.add(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/report/cashierSessions", method = RequestMethod.GET)
	public List<Object> selectCashierSessions(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date endDate, 
			@RequestParam(name = "page", required = true) int pageNumber){
		List<Object> result = new ArrayList<>();
		
		try {
			Page<CashierSession> list = cashierSessionService.selectCashierSession(startDate, endDate, pageNumber, Constant.REPORT_ROW_PER_PAGE);
			Double total = cashierSessionService.calculateTotalSession(startDate, endDate);
			result.add(list);
			result.add(total);
		} catch(Exception ex) {
			result.add(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/report/soldMenus", method = RequestMethod.GET)
	public List<Object> selectSoldMenus(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date endDate, 
			@RequestParam(name = "page", required = true) int pageNumber){
		List<Object> result = new ArrayList<>();
		
		try {
			List<SoldMenu> list = invoiceService.selectSoldMenu(startDate, endDate, pageNumber, Constant.REPORT_ROW_PER_PAGE);
			result.add(list);
		} catch(Exception ex) {
			result.add(ex.getMessage());
		}
		return result;
	}
	
	@RequestMapping(value = "/report/sendMail", method = RequestMethod.GET)
	public List<Object> sendMail(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date endDate){
		List<Object> result = new ArrayList<>();
		
		try {
			reportService.downloadReports(startDate, endDate);
			result.add("Report berhasil dikirim!");
		} catch(Exception ex) {
			result.add(ex.getMessage());
		}
		return result;
	}
	
	private InvoiceForList convertToDto(Invoice invoice) {
		InvoiceForList inv = modelMapper.map(invoice, InvoiceForList.class);
		return inv;
	}
	
	@RequestMapping(value = "/report/purchasings", method = RequestMethod.GET)
	public Page<Purchasing> selectPurchasing(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date endDate, 
			@RequestParam(name = "page", required = true) int pageNumber){
		Page<Purchasing> list = null;
		
		try {
			list = reportService.getPurchasings(startDate, endDate, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
	
	@RequestMapping(value = "/report/adjustments", method = RequestMethod.GET)
	public Page<Adjustment> selectAdjustment(@RequestParam(name = "startDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date startDate, @RequestParam(name = "endDate", required = true) @DateTimeFormat(pattern = "E MMM dd yyyy") Date endDate, 
			@RequestParam(name = "page", required = true) int pageNumber){
		Page<Adjustment> list = null;
		
		try {
			list = reportService.getAdjustments(startDate, endDate, pageNumber, Constant.ROW_PER_PAGE);
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return list;
	}
}
