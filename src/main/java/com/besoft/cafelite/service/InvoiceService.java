package com.besoft.cafelite.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.besoft.cafelite.model.Invoice;
import com.besoft.cafelite.repository.InvoiceRepository;

@Service
public class InvoiceService {
	@Autowired
	private InvoiceRepository repo;
	
	public boolean save(Invoice invoice) {
		return repo.save(invoice)!=null;
	}
	
	public Invoice getInvoice(Long id) {
		return repo.findById(id).get();
	}
}
