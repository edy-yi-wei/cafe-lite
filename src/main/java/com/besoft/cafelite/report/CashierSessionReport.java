package com.besoft.cafelite.report;

import java.sql.Timestamp;

import lombok.Data;

@Data
public class CashierSessionReport {
	private Long sessionId;
	private String cashierName;
	private Timestamp loginTime;
	private Timestamp logoutTime;
	private Double cashAmount;
	
}
