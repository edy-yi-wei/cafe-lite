package com.besoft.cafelite.model;

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
@Table(name = "tbl_role_module")
public class RoleModule {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "role_module_id")
	private Long roleModuleId;
	
	@ManyToOne
	@JoinColumn(name = "module_id")
	private Module module;
	
	@Column(name = "read_only")
	private boolean readOnly;
	
	@Column(name = "active")
	private boolean active;
	
}
