package com.besoft.cafelite.report;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceReport {
	private Long invoiceId;
	private String invoiceNumber;
	private Date invoiceDate;
	private String invoiceType;
	private String customerName;
	private Double total;
	
}
