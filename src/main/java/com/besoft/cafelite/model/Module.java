package com.besoft.cafelite.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotBlank;

import lombok.Data;

@Data
@Entity
@Table(name = "tbl_module")
public class Module {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "module_id")
	private Long moduleId;
	
//	@Column(name = "order_number")
//	private Integer orderNumber;
	
	@Column(name = "module_code")
	private String moduleCode;
	
	@Column(name = "module_name")
	@NotBlank(message = "{module.moduleName.notBlank}")
	private String moduleName;
	
	@Column(name = "parent_name")
	private String parentName;
	
	@Column(name = "moduleUrl")
	private String moduleUrl;
	
	@Column(name = "deleted")
	private boolean deleted;
}
