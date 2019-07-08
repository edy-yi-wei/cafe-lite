package com.besoft.cafelite.dto;

import java.util.Date;

import lombok.Data;

@Data
public class InvoiceForList {
	private Long invoiceId;
	private String invoiceNumber;
	private Date invoiceDate;
	private String invoiceType;
	private String customerName;
	private Double total;
}
