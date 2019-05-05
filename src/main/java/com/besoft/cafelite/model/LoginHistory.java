package com.besoft.cafelite.model;

import java.io.Serializable;
import java.util.Date;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_login_history")
public class LoginHistory implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "login_id")
	private Long loginId;
	
	@Column(name = "login_time")
	private Date loginTime;
	
	@Column(name = "logout_time")
	private Date logoutTime;
	
	@ManyToOne
	@JoinColumn(name = "user_id")
	private User loginUser;

}
