package com.hxgis.authserver.model;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;
import java.math.BigDecimal;

/**
 * SysModule entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_MODULE", schema = "HXLC")
public class SysModule implements java.io.Serializable {

	// Fields

	private BigDecimal id;
	private String sysno;
	private String moduleno;
	private String parentno;
	private BigDecimal ordernum;
	private String name;
	private String descirp;
	private String url;
	private String icon;
	private BigDecimal isvisible;

	// Constructors

	/** default constructor */
	public SysModule() {
	}

	/** minimal constructor */
	public SysModule(BigDecimal id) {
		this.id = id;
	}

	/** full constructor */
	public SysModule(BigDecimal id, String sysno, String moduleno,
                     String parentno, BigDecimal ordernum, String name, String descirp,
                     String url, String icon, BigDecimal isvisible) {
		this.id = id;
		this.sysno = sysno;
		this.moduleno = moduleno;
		this.parentno = parentno;
		this.ordernum = ordernum;
		this.name = name;
		this.descirp = descirp;
		this.url = url;
		this.icon = icon;
		this.isvisible = isvisible;
	}

	// Property accessors
	@Id
	@Column(name = "ID", unique = true, nullable = false, precision = 22, scale = 0)
	public BigDecimal getId() {
		return this.id;
	}

	public void setId(BigDecimal id) {
		this.id = id;
	}

	@Column(name = "SYSNO")
	public String getSysno() {
		return this.sysno;
	}

	public void setSysno(String sysno) {
		this.sysno = sysno;
	}

	@Column(name = "MODULENO")
	public String getModuleno() {
		return this.moduleno;
	}

	public void setModuleno(String moduleno) {
		this.moduleno = moduleno;
	}

	@Column(name = "PARENTNO")
	public String getParentno() {
		return this.parentno;
	}

	public void setParentno(String parentno) {
		this.parentno = parentno;
	}

	@Column(name = "ORDERNUM", precision = 22, scale = 0)
	public BigDecimal getOrdernum() {
		return this.ordernum;
	}

	public void setOrdernum(BigDecimal ordernum) {
		this.ordernum = ordernum;
	}

	@Column(name = "NAME", length = 40)
	public String getName() {
		return this.name;
	}

	public void setName(String name) {
		this.name = name;
	}

	@Column(name = "DESCIRP")
	public String getDescirp() {
		return this.descirp;
	}

	public void setDescirp(String descirp) {
		this.descirp = descirp;
	}

	@Column(name = "URL", length = 260)
	public String getUrl() {
		return this.url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	@Column(name = "ICON", length = 200)
	public String getIcon() {
		return this.icon;
	}

	public void setIcon(String icon) {
		this.icon = icon;
	}

	@Column(name = "ISVISIBLE", precision = 22, scale = 0)
	public BigDecimal getIsvisible() {
		return this.isvisible;
	}

	public void setIsvisible(BigDecimal isvisible) {
		this.isvisible = isvisible;
	}

}