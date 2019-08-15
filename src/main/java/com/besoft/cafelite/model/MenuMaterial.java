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
@Table(name = "tbl_menu_material")
public class MenuMaterial implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_material_id")
	private Long menuMaterialId;
	
	@ManyToOne
	@JoinColumn(name = "material_id")
	private RawMaterial material;
	
	@Column(name = "quantity")
	@NotNull(message = "quantity tidak boleh kosong")
	private Double quantity;
	
}
