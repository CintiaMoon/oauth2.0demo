package com.hxgis.authserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUserrole entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_USERROLE", schema = "HXLC")
public class SysUserrole implements java.io.Serializable {

	// Fields

	private Integer userid;
	private Integer roleid;

	// Constructors

	/** default constructor */
	public SysUserrole() {
	}

	/** full constructor */
	public SysUserrole(Integer userid, Integer roleid) {
		this.userid = userid;
		this.roleid = roleid;
	}

	@Id
	@Column(name = "USERID")
	public Integer getUserid() {
		return this.userid;
	}

	public void setUserid(Integer userid) {
		this.userid = userid;
	}

	@Column(name = "ROLEID")
	public Integer getRoleid() {
		return this.roleid;
	}

	public void setRoleid(Integer roleid) {
		this.roleid = roleid;
	}

}