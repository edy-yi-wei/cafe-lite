package com.besoft.cafelite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_invoice")
public class Invoice implements Serializable{
	
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "invoice_id")
	private Long invoiceId;
	
	@Column(name = "invoice_number")
	private String invoiceNumber;
	
	@Column(name = "invoice_date")
	private Date invoiceDate;
	
	@Column(name = "invoice_type")
	@NotBlank(message = "Anda belum memilih jenis pesanan")
	private String invoiceType;
	
	@Column(name = "customer_name")
	private String customerName;
	
	@Column(name = "total")
	private Double total;
	
	@Column(name = "payment")
	private Double payment;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "login_id")
	private LoginHistory loginHistory;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "invoice_id")
	private List<InvoiceDetail> details = new ArrayList<>();
	
	public Double calculateTotal() {
		Double total = 0d;
		for (InvoiceDetail invoiceDetail : details) {
			total+= invoiceDetail.calculateAmount();
		}
		return total;
	}
	
	public Double calculateChange() {
		return payment - calculateTotal();
	}
	
	public boolean isPaymentEnough() {
		return payment>= calculateTotal();
	}
}
