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
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_purchasing")
public class Purchasing implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchasing_id")
	private Long purchasingId;
	
	@Column(name = "purchasing_number")
	private String purchasingNumber;
	
	@Column(name = "purchasing_date")
	private Date purchasingDate;
	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "user_id")
	private User user;

	
	@ManyToOne(fetch = FetchType.EAGER)
	@JoinColumn(name = "supplier_id")
	private Supplier supplier;
	
	
	@Column(name = "amount")
	private Double amount;
	
	@Column(name = "discount")
	private Double discount;
	
	@Column(name = "netto")
	private Double netto;

	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "purchasing_id")
	private List<PurchasingDetail> details = new ArrayList<>();
	
	
	public Double calculateTotal() {
		Double total = 0d;
		for (PurchasingDetail purchasingDetail : details) {
			total+= purchasingDetail.calculateAmount();
		}
		return total;
	}
}
