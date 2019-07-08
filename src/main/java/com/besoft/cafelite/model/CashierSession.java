package com.besoft.cafelite.model;

import java.io.Serializable;
import java.sql.Timestamp;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.annotation.JsonInclude.Include;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_cashier_session")
public class CashierSession implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "session_id")
	private Long sessionId;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User cashier;
	
	@Column(name = "login_time")
	private Timestamp loginTime;
	
	@Column(name = "logout_time")
	private Timestamp logoutTime;
	
	@Column(name = "cash_amount")
	private Double cashAmount;
}
