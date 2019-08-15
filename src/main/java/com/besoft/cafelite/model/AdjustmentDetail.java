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
@Table(name = "tbl_adjusment_detail")
public class AdjustmentDetail implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "adjustment_detail_id")
	private Long adjustmentDetailId;
	
	@ManyToOne
	@JoinColumn(name = "material_id")
	private RawMaterial material;
	
	@Column(name = "quantity")
	@NotNull(message = "quantity tidak boleh kosong")	
	private Double quantity;
	
	@Column(name = "notes")
	private String notes;
	
}
