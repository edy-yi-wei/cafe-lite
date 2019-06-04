package com.besoft.cafelite.service;

import java.text.DateFormat;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.model.InvoiceDetail;
import com.besoft.cafelite.repository.InvoiceRepository;
import com.besoft.cafelite.utilities.PrintElement;
import com.besoft.cafelite.utilities.Printer;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceRepository repo;
	@Autowired
	private Printer printer;
	
	public boolean save(Invoice invoice) {		
		try {
			repo.save(invoice);
//			printInvoice();
			return true;
		} catch(Exception ex) {
			return false;
		}		
	}
	
	public Invoice getInvoice(Long id) {
		Invoice inv = repo.findById(id).get();
		inv.getLoginHistory();
		printInvoice(inv);
		return null;
	}
	
	private void printInvoice(Invoice invoice) {
		SimpleDateFormat fmt = new SimpleDateFormat("dd-MM-yyyy");
		Locale locale = new Locale("id", "ID");
		int x = 0;
		int y = 10;
		
		List<PrintElement> list = new ArrayList<>();
		PrintElement el = new PrintElement();
		el.setContent(invoice.getInvoiceNumber());
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(fmt.format(invoice.getInvoiceDate()));
		el.setX(x+135);
		el.setY(y);		
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent(invoice.getLoginHistory().getLoginUser().getUserName());
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
			el.setContent(detail.getMenu().getPrintoutName());
			el.setX(x);
			el.setY(y);
			list.add(el);
			x+= 120;
			el = new PrintElement();
			el.setContent(String.format("%10s", NumberFormat.getNumberInstance(locale).format(detail.calculateAmount())));
			el.setX(x);
			el.setY(y);
			list.add(el);
			y+=15;
		}
		x=85;
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
		el.setX(x+49);
		el.setY(y);
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent("Cash");
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(String.format("%12s", NumberFormat.getNumberInstance(locale).format(invoice.getPayment())));
		el.setX(x+49);
		el.setY(y);
		list.add(el);
		y+=15;
		el = new PrintElement();
		el.setContent("Change");
		el.setX(x);
		el.setY(y);
		list.add(el);
		el = new PrintElement();
		el.setContent(String.format("%12s", NumberFormat.getNumberInstance(locale).format(invoice.calculateChange())));
		el.setX(x+49);
		el.setY(y);
		list.add(el);
		printer.print(list);
	}
}
