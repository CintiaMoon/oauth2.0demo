package com.hxgis.authserver.model;

import javax.persistence.*;
import java.math.BigDecimal;

/**
 * SysPrivilege entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_PRIVILEGE", schema = "HXLC")
public class SysPrivilege implements java.io.Serializable {

	// Fields
	@Id
	private BigDecimal privilegeid;
	private String privilegemasterkey;
	private Integer privilegeaccesskey;
	private String privilegemaster;
	private String privilegeaccess;
	private Integer privilegeoperation;

	// Constructors

	/** default constructor */
	public SysPrivilege() {
	}

	/** minimal constructor */
	public SysPrivilege(BigDecimal privilegeid) {
		this.privilegeid = privilegeid;
	}

	/** full constructor */
	public SysPrivilege(BigDecimal privilegeid, String privilegemasterkey,
                        Integer privilegeaccesskey, String privilegemaster,
                        String privilegeaccess, Integer privilegeoperation) {
		this.privilegeid = privilegeid;
		this.privilegemasterkey = privilegemasterkey;
		this.privilegeaccesskey = privilegeaccesskey;
		this.privilegemaster = privilegemaster;
		this.privilegeaccess = privilegeaccess;
		this.privilegeoperation = privilegeoperation;
	}

	// Property accessors
	@Id
	@Column(name = "PRIVILEGEID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getPrivilegeid() {
		return this.privilegeid;
	}

	public void setPrivilegeid(BigDecimal privilegeid) {
		this.privilegeid = privilegeid;
	}

	@JoinColumn(name = "PRIVILEGEMASTERKEY")
	public String getPrivilegemasterkey() {
		return this.privilegemasterkey;
	}

	public void setPrivilegemasterkey(String privilegemasterkey) {
		this.privilegemasterkey = privilegemasterkey;
	}

	@JoinColumn(name = "PRIVILEGEACCESSKEY")
	public Integer getPrivilegeaccesskey() {
		return this.privilegeaccesskey;
	}

	public void setPrivilegeaccesskey(Integer privilegeaccesskey) {
		this.privilegeaccesskey = privilegeaccesskey;
	}

	@Column(name = "PRIVILEGEMASTER")
	public String getPrivilegemaster() {
		return this.privilegemaster;
	}

	public void setPrivilegemaster(String privilegemaster) {
		this.privilegemaster = privilegemaster;
	}

	@Column(name = "PRIVILEGEACCESS")
	public String getPrivilegeaccess() {
		return this.privilegeaccess;
	}

	public void setPrivilegeaccess(String privilegeaccess) {
		this.privilegeaccess = privilegeaccess;
	}

	@Column(name = "PRIVILEGEOPERATION", precision = 5, scale = 0)
	public Integer getPrivilegeoperation() {
		return this.privilegeoperation;
	}

	public void setPrivilegeoperation(Integer privilegeoperation) {
		this.privilegeoperation = privilegeoperation;
	}

}