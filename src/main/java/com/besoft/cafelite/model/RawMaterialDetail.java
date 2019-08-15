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
@Table(name = "tbl_material_detail")
public class RawMaterialDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "material_detail_id")
	private Long materialDetailId;
	
	@ManyToOne
	@JoinColumn(name = "mat_id")
	private RawMaterial material;
	
	@Column(name = "conversion_quantity")
	@NotNull(message = "conversion quantity tidak boleh kosong")
	private Double conversionQuantity;
	
	@Column(name = "deleted", nullable = false)
	private boolean deleted;
}
