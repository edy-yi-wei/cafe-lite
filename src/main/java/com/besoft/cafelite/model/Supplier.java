package com.besoft.cafelite.model;

import java.io.Serializable;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_supplier")
public class Supplier implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "supplier_id")
	private Long supplierId;
	
	@Column(name = "supplier_code")
	@NotBlank(message = "kode supplier {notBlank}")
//	@Size(max = 15, message = "{user.userName.size}")
	private String supplierCode;
	
	@Column(name = "supplier_name")
	@NotBlank(message = "nama supplier {notBlank}")
	@Size(max = 100, message = "maksimal 100 karakter.")
	private String supplierName;
	
	@Column(name = "address")
	@NotBlank(message = "alamat {notBlank}")
	@Size(max = 250, message = "maksimal 250 karakter.")
	private String address;
	
	@Column(name = "phone")
	@NotBlank(message = "nomor telpon {notBlank}")
	@Size(max = 20, message = "maksimal 20 karakter.")
	private String phone;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
}
