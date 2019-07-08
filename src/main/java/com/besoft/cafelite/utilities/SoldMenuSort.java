package com.besoft.cafelite.utilities;

import java.util.Comparator;

import com.besoft.cafelite.dto.SoldMenu;

public class SoldMenuSort implements Comparator<SoldMenu>{
	
	public int compare(SoldMenu a, SoldMenu b) {
		return (int)(b.getQuantity().doubleValue() - a.getQuantity().doubleValue());
	}
}
