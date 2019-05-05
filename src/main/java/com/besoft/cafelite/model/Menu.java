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

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_menu")
public class Menu implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "menu_id")
	private Long menuId;
	
	@Column(name = "menu_name")
	@NotBlank(message = "{menu.menuName.notBlank}")
	private String menuName;
	
	@Column(name = "menu_price")
	private Double menuPrice;
	
	@Column(name = "deleted")
	private boolean deleted;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "login_id")
	private LoginHistory loginHistory;
}
