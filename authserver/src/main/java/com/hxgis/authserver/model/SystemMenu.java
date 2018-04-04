package com.hxgis.authserver.model;

import java.math.BigDecimal;

/**
 * SysModule entity. @author MyEclipse Persistence Tools
 */
public class SystemMenu implements java.io.Serializable {

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
    private String hasThisRight;

    // Constructors

    /** default constructor */
    public SystemMenu() {
    }

    /** minimal constructor */
    public SystemMenu(BigDecimal id) {
        this.id = id;
    }


    public BigDecimal getId() {
        return this.id;
    }

    public void setId(BigDecimal id) {
        this.id = id;
    }

    public String getSysno() {
        return this.sysno;
    }

    public void setSysno(String sysno) {
        this.sysno = sysno;
    }

    public String getModuleno() {
        return this.moduleno;
    }

    public void setModuleno(String moduleno) {
        this.moduleno = moduleno;
    }

    public String getParentno() {
        return this.parentno;
    }

    public void setParentno(String parentno) {
        this.parentno = parentno;
    }

    public BigDecimal getOrdernum() {
        return this.ordernum;
    }

    public void setOrdernum(BigDecimal ordernum) {
        this.ordernum = ordernum;
    }

    public String getName() {
        return this.name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescirp() {
        return this.descirp;
    }

    public void setDescirp(String descirp) {
        this.descirp = descirp;
    }

    public String getUrl() {
        return this.url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getIcon() {
        return this.icon;
    }

    public void setIcon(String icon) {
        this.icon = icon;
    }

    public BigDecimal getIsvisible() {
        return this.isvisible;
    }

    public void setIsvisible(BigDecimal isvisible) {
        this.isvisible = isvisible;
    }

    public String getHasThisRight() {
        return hasThisRight;
    }

    public void setHasThisRight(String hasThisRight) {
        this.hasThisRight = hasThisRight;
    }
}