package com.hxgis.authserver.model;

import com.alibaba.fastjson.annotation.JSONField;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

/**
 * SysUser entity. @author MyEclipse Persistence Tools
 */
@Entity
@Table(name = "SYS_USER")
public class SysUser implements java.io.Serializable {

    // Fields
    @Id
    @Column(name = "USERID", unique = true, nullable = false, precision = 10, scale = 0)
    @JSONField(ordinal = 1)
    private Integer userId;
    @Column(name = "DEPARTMENTID")
    @JSONField(ordinal = 2)
    private String departmentId;
    @Column(name = "LOGINNAME", unique = true, nullable = false)
    @JSONField(ordinal = 3)
    private String loginName;
    @Column(name = "LOGINPASSWORD", nullable = false)
    @JSONField(ordinal = 4)
    private String loginPassword;
    @Column(name = "ORDERNUM", precision = 10, scale = 0)
    @JSONField(ordinal = 5)
    private Long orderNum;
    @Column(name = "PHONE")
    @JSONField(ordinal = 6)
    private String phone;
    @Column(name = "QQ")
    @JSONField(ordinal = 7)
    private String qq;
    @Column(name = "REALNAME")
    @JSONField(ordinal = 8)
    private String realName;
    @Column(name = "REMARK")
    @JSONField(ordinal = 9)
    private String remark;
    @Column(name = "SEX", precision = 10, scale = 0)
    @JSONField(ordinal = 10)
    private Long sex;
    @Column(name = "TITLE")
    @JSONField(ordinal = 11)
    private String title;
    @Column(name = "WEIXINID")
    @JSONField(ordinal = 12)
    private String weixinId;
    @Column(name = "WEIXINNAME")
    @JSONField(ordinal = 13)
    private String weixinName;
    @Column(name = "ENABLED", nullable = false, precision = 1, scale = 0)
    @JSONField(ordinal = 14)
    private Long enabled;

    // Constructors

    /** default constructor */
    public SysUser() {
    }


    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getDepartmentId() {
        return departmentId;
    }

    public void setDepartmentId(String departmentId) {
        this.departmentId = departmentId;
    }

    public String getLoginName() {
        return loginName;
    }

    public void setLoginName(String loginName) {
        this.loginName = loginName;
    }

    public String getLoginPassword() {
        return loginPassword;
    }

    public void setLoginPassword(String loginPassword) {
        this.loginPassword = loginPassword;
    }

    public Long getOrderNum() {
        return orderNum;
    }

    public void setOrderNum(Long orderNum) {
        this.orderNum = orderNum;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getQq() {
        return qq;
    }

    public void setQq(String qq) {
        this.qq = qq;
    }

    public String getRealName() {
        return realName;
    }

    public void setRealName(String realName) {
        this.realName = realName;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public Long getSex() {
        return sex;
    }

    public void setSex(Long sex) {
        this.sex = sex;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWeixinId() {
        return weixinId;
    }

    public void setWeixinId(String weixinId) {
        this.weixinId = weixinId;
    }

    public String getWeixinName() {
        return weixinName;
    }

    public void setWeixinName(String weixinName) {
        this.weixinName = weixinName;
    }

    public Long getEnabled() {
        return enabled;
    }

    public void setEnabled(Long enabled) {
        this.enabled = enabled;
    }
}