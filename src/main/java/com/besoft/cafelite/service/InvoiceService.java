package com.besoft.cafelite.service;

import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.List;
import java.util.Locale;

import javax.transaction.Transactional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.dto.SoldMenu;
import com.besoft.cafelite.model.CashierSession;
import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.model.InvoiceDetail;
import com.besoft.cafelite.model.Menu;
import com.besoft.cafelite.model.MenuMaterial;
import com.besoft.cafelite.model.RawMaterial;
import com.besoft.cafelite.model.RawMaterialDetail;
import com.besoft.cafelite.repository.InvoiceRepository;
import com.besoft.cafelite.utilities.PrintElement;
import com.besoft.cafelite.utilities.Printer;
import com.besoft.cafelite.utilities.SoldMenuSort;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceRepository repo;
	@Autowired
	private CashierSessionService cashierSessionService;
	
	@Autowired
	private MenuService menuService;
	
	@Autowired
	private RawMaterialService materialService;
	
	@Autowired
	private Printer printer;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@Transactional(rollbackOn = Exception.class)
	public void save(Invoice invoice, String cashierName) throws Exception {
		logger.info("InvoiceService - save");
		try {
			invoice.setInvoiceDate(new Date());
			invoice.setInvoiceNumber(generateInvoiceNumber());
			invoice.setTotal(invoice.calculateTotal());
			
			//adjust stock here
			if(invoice.getDetails() != null && invoice.getDetails().size() > 0) {
				for(InvoiceDetail details : invoice.getDetails()) {
					Double soldQty = details.getQuantity();
					Menu menu = menuService.getMenu(details.getMenu().getMenuId());
					details.setMenu(menu);
					if(details.getMenu().getMaterials() != null && details.getMenu().getMaterials().size() > 0) {
						for(MenuMaterial menuMaterial : details.getMenu().getMaterials()) {
							Double qty = soldQty * menuMaterial.getQuantity();
							
							RawMaterial material = materialService.getMaterial(menuMaterial.getMaterial().getMaterialId());
							if(material.isStockable()) {
								material.setQuantity(material.getQuantity() - qty);
								materialService.updateStock(material);
							}
						}
					}
				}
			}
			
			Invoice inv = repo.save(invoice);
			cashierSessionService.updateCashierSession(cashierName, invoice.calculateTotal());
			printInvoice(inv, cashierName);
		} catch (Exception ex) {
			throw ex;
		}
	}
	
	private String generateInvoiceNumber() throws Exception{
		logger.info("InvoiceService - generateInvoiceNumber");
		
		try {
			String invoiceNumber = "";
			Date tanggal = new Date();
			int bulan = tanggal.getMonth()+1;
			int tahun = tanggal.getYear()+1900;
			String result = repo.getLastInvoiceNumber(bulan, tahun);
			Long nomor = result==null?1:Long.parseLong(result.substring(8, 13)) + 1;
			invoiceNumber = "PS" + String.valueOf(tahun) + (bulan>9?bulan:"0"+bulan) + String.format("%05d", nomor);
			return invoiceNumber;
		} catch(Exception ex) {
			logger.error("InvoiceService - generateInvoiceNumber "+ex.getMessage());
			throw ex;
		}
	}
	
	public Invoice getInvoice(Long id) throws Exception {
		logger.info("InvoiceService - getInvoice [id: "+id+"]");
		
		try {
			Invoice inv = repo.findById(id).get();
			inv.getLoginHistory();
			return inv;
		} catch(Exception ex) {
			logger.error("InvoiceService - getInvoice "+ex.getMessage());
			throw ex;
		}
	}
	
	public Page<Invoice> selectInvoice(Date startDate, Date endDate, int pageNumber, int pageSize) throws Exception {
		logger.info("Invoice Service - selectInvoice[start date: "+startDate+ ", end date: "+endDate+", page number: "+pageNumber+", page size: "+pageSize+"]");
	
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			return repo.findInvoiceByPeriod(startDate, endDate, page);
		} catch(Exception ex) {
			logger.error("InvoiceService - selectInvoice "+ex.getMessage());
			throw ex;
		}
	}
		
	public double calculateTotalInvoice(Date startDate, Date endDate) throws Exception {
		logger.info("InvoiceService - calculateTotalInvoice");
		
		try {
			Calendar cal = Calendar.getInstance();
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			return repo.calculateTotalInvoice(startDate, endDate);
		} catch(Exception ex) {
			logger.error("InvoiceService - calculateTotalInvoice "+ex.getMessage());
			throw ex;
		}
	}
	
	@Transactional(rollbackOn = Exception.class)
	public void doClosing(String userName) throws Exception {
		logger.info("InvoiceService - doClosing [user name: "+userName+"]");
		
		try {
			CashierSession session = cashierSessionService.doClosing(userName);
			printClosing(session);
		} catch(Exception ex) {
			logger.error("InvoiceService - doClosing "+ex.getMessage());
			throw ex;
		}
	}
	
	public List<SoldMenu> selectSoldMenu(Date startDate, Date endDate, int pageNumber, int pageSize) throws Exception {
		logger.info("Invoice Service - selectSoldMenu [start date: "+startDate+ ", end date: "+endDate+", page number: "+pageNumber+", page size: "+pageSize+"]");
	
		try {
			List<SoldMenu> result = new ArrayList<>();
			
			Calendar cal = Calendar.getInstance();
			Pageable page = PageRequest.of(pageNumber-1, pageSize);
			System.out.println(startDate);
			System.out.println(endDate);
			cal.setTime(endDate);
			cal.add(Calendar.DATE, 1);
			cal.add(Calendar.SECOND, -1);
			endDate = cal.getTime();
			System.out.println(endDate);
	//		Page<SoldMenu> list = repo.calculateSoldMenu(startDate, endDate, page);
	//		System.out.println("kembali dari repo");
	//		return list;
			Page<Invoice> invoices = repo.findInvoiceByPeriod(startDate, endDate, page);
			if(invoices.hasContent()) {
				List<Invoice> list = invoices.getContent();
				for(Invoice i: list) {
					mergeToList(i, result);
				}
			}
			Collections.sort(result, new SoldMenuSort());
			return result;
		} catch(Exception ex) {
			logger.error("InvoiceService - selectSoldMenu "+ex.getMessage());
			throw ex;
		}
	}
	
	private void mergeToList(Invoice i, List<SoldMenu> list) throws Exception{
		logger.info("InvoiceService - mergeToList");
		
		try {
			boolean found = false;
			for(InvoiceDetail d: i.getDetails()) {
				for(SoldMenu s: list) {
					if(d.getMenu().getMenuId().longValue()==s.getMenu().getMenuId().longValue()) {
						Double nilai = s.getQuantity() + d.getQuantity();
						s.setQuantity(nilai);
						found=true;
						break;
					}
				}
				if(!found) {
					SoldMenu m = new SoldMenu();
					m.setMenu(d.getMenu());
					m.setQuantity(d.getQuantity());
					list.add(m);
				}
			}
		} catch(Exception ex) {
			logger.error("InvoiceService - mergeToList "+ex.getMessage());
			throw ex;
		}
	}
	
	public void rePrintInvoice(String userName) throws Exception {
		logger.info("InvoiceService - rePrintInvoice [user name: "+userName+"]");
		
		try {
			List<Invoice> invoices = repo.findLastestInvoice();
			if(invoices!=null && !invoices.isEmpty()) {
				Invoice invoice = invoices.get(0);
				printInvoice(invoice, userName);
			}
		} catch(Exception ex) {
			logger.error("InvoiceService - rePrintInvoice "+ex.getMessage());
			throw ex;
		}
	}
	
	private void printInvoice(Invoice invoice, String cashierName) {
		logger.info("InvoiceService - printInvoice");
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Locale locale = new Locale("id", "ID");
		int x = 0;
		int y = 10;
		
		List<PrintElement> list = new ArrayList<>();
		PrintElement el = new PrintElement();
		
		el.setContent("CINDY'S Fried Chicken");
		el.setX(x+50);
		el.setY(y);
		list.add(el);
		y+=10;
		el = new PrintElement();
		el.setContent("Jl. 28 Oktober No. C04");
		el.setX(x+42);
		el.setY(y);
		list.add(el);
		y+=10;
		el = new PrintElement();
		el.setContent("Pontianak");
		el.setX(x+70);
		el.setY(y);
		list.add(el);
		y+=20;
		el = new PrintElement();
		el.setContent(invoice.getInvoiceNumber());
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(fmt.format(invoice.getInvoiceDate()));
		el.setX(x+100);
		el.setY(y);		
		list.add(el);
		y+=10;
		el = new PrintElement();
//		el.setContent(invoice.getLoginHistory().getLoginUser().getUserName());
		el.setContent(cashierName);
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(invoice.getInvoiceType().toUpperCase());
		el.setX(x+100);
		el.setY(y);
		list.add(el);
		y+=10;
		el = new PrintElement();
		el.setContent(invoice.getCustomerName());
		el.setX(x);
		el.setY(y);
		list.add(el);
		y+=10;
		el = new PrintElement();
		el.setContent("-");
		el.setX(x);
		el.setY(y);
		list.add(el);
		
		y+=20;
		for(InvoiceDetail detail: invoice.getDetails()) {
			x=0;
			el = new PrintElement();
			el.setContent(String.format("%3d", detail.getQuantity().intValue()));
			el.setX(x);
			el.setY(y);
			list.add(el);
			x+= 20;
			el = new PrintElement();
			el.setContent(detail.getMenu().getPrintoutName()==null?detail.getMenu().getMenuName():detail.getMenu().getPrintoutName());
			el.setX(x);
			el.setY(y);
			list.add(el);
			x+= 110;
			el = new PrintElement();
			el.setContent(String.format("%10s", NumberFormat.getNumberInstance(locale).format(detail.calculateAmount())));
			el.setX(x);
			el.setY(y);
			list.add(el);
			y+=15;
		}
		x=55;
//		y+=10;
		el = new PrintElement();
		el.setContent("-");
		el.setX(x);
		el.setY(y);
		list.add(el);
		y+=20;
		el = new PrintElement();
		el.setContent("Total");
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(String.format("%12s", NumberFormat.getNumberInstance(locale).format(invoice.calculateTotal())));
		el.setX(x+66);
		el.setY(y);
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent("Bayar");
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(String.format("%12s", NumberFormat.getNumberInstance(locale).format(invoice.getPayment())));
		el.setX(x+66);
		el.setY(y);
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent("Kembali");
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(String.format("%12s", NumberFormat.getNumberInstance(locale).format(invoice.calculateChange())));
		el.setX(x+66);
		el.setY(y);
		list.add(el);
		y+=20;
		el = new PrintElement();
		el.setContent("Terima Kasih");
		el.setX(x+10);
		el.setY(y);
		list.add(el);
		printer.print(list);
	}
	
	private void printClosing(CashierSession session) {
		logger.info("InvoiceService - printClosing");
		
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy HH:mm");
		Locale locale = new Locale("id", "ID");
		int x = 0;
		int y = 10;
		
		List<PrintElement> list = new ArrayList<>();
		PrintElement el = new PrintElement();
		el.setContent("CLOSING PENJUALAN");
		el.setX(x);
		el.setY(y);
		list.add(el);
		y+=10;
		el = new PrintElement();
		el.setContent("-");
		el.setX(x);
		el.setY(y);
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent(String.format("%1$-11s", "Kasir ") + session.getCashier().getUserName());
//		el.setContent("kasir");
		el.setX(x);
		el.setY(y);
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent(String.format("%1$-11s", "Login ")  + fmt.format(session.getLoginTime()));
		el.setX(x);
		el.setY(y);		
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent(String.format("%1$-11s", "Logout ")  + fmt.format(session.getLogoutTime()));
		el.setX(x);
		el.setY(y);		
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent(String.format("%1$-11s", "Nilai Cash ")  + NumberFormat.getNumberInstance(locale).format(session.getCashAmount()));
		el.setX(x);
		el.setY(y);		
		list.add(el);
		y+=10;
		el = new PrintElement();
		el.setContent("-");
		el.setX(x);
		el.setY(y);
		list.add(el);
				
		printer.print(list);
	}
}
