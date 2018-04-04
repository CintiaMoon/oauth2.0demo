package com.hxgis.authserver.model;

import javax.persistence.*;

/**
 * SysRole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_ROLE", schema = "HXLC")
public class SysRole implements java.io.Serializable {

	// Fields
	@Id
	@GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "menuSeq2")
	@SequenceGenerator(name = "menuSeq2", initialValue = 1, allocationSize = 1, sequenceName = "MENU_SEQUENCE2")
	private Long roleid;
	private Long ordernum;
	private String roledesc;
	private String rolename;

	// Constructors

	/** default constructor */
	public SysRole() {
	}

	/** minimal constructor */
	public SysRole(Long roleid) {
		this.roleid = roleid;
	}

	/** full constructor */
	public SysRole(Long roleid, Long ordernum, String roledesc,
                   String rolename) {
		this.roleid = roleid;
		this.ordernum = ordernum;
		this.roledesc = roledesc;
		this.rolename = rolename;
	}

	// Property accessors
	@Column(name = "ROLEID", unique = true, nullable = false, precision = 10, scale = 0)
	public Long getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Long roleid) {
		this.roleid = roleid;
	}

	@Column(name = "ORDERNUM", precision = 10, scale = 0)
	public Long getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(Long ordernum) {
		this.ordernum = ordernum;
	}

	@Column(name = "ROLEDESC")
	public String getRoledesc() {
		return this.roledesc;
	}

	public void setRoledesc(String roledesc) {
		this.roledesc = roledesc;
	}

	@Column(name = "ROLENAME")
	public String getRolename() {
		return this.rolename;
	}

	public void setRolename(String rolename) {
		this.rolename = rolename;
	}
}