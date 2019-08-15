package com.besoft.cafelite.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_purchasing_detail")
public class PurchasingDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "purchasing_detail_id")
	private Long purchasingDetailId;
	
	@ManyToOne
	@JoinColumn(name = "material_id")
	private RawMaterial material;
	
	@Column(name = "quantity")
	@NotNull(message = "quantity tidak boleh kosong")
	private Double quantity;
	
	@Column(name = "price")
	@NotNull(message = "harga tidak boleh kosong")
	private Double price;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
	
	public Double calculateAmount() {
		return quantity*price;
	}
}
