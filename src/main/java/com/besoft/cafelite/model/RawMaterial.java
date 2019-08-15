package com.besoft.cafelite.model;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_material")
public class RawMaterial implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "material_id")
	private Long materialId;
	
	@Column(name = "material_code")
	@NotBlank(message = "kode material tidak boleh kosong")
	private String materialCode;
	
	@Column(name = "material_name")
	@NotBlank(message = "nama material tidak boleh kosong")
	private String materialName;
	
	@Column(name = "uom")
	@NotBlank(message = "uom tidak boleh kosong")
	private String uom;
	
	@Column(name = "quantity")
	private Double quantity;
	
	@Column(name = "stockable", nullable = false)
	private boolean stockable;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
	
	@OneToMany(cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
	@JoinColumn(name = "material_id")
	private List<RawMaterialDetail> details = new ArrayList<>();
	
	
}
