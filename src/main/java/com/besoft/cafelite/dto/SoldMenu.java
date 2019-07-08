package com.besoft.cafelite.dto;

import java.io.Serializable;

import com.besoft.cafelite.model.Menu;

import lombok.Data;

@Data
public class SoldMenu implements Serializable {
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private Menu menu;
	private Double quantity;
}
