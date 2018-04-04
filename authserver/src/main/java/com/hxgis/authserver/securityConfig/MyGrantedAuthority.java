package com.hxgis.authserver.securityConfig;

import org.springframework.security.core.GrantedAuthority;


/**
 * @author Administrator
 */
public class MyGrantedAuthority implements GrantedAuthority {

    private String sysno;
    private String permissionUrl;
    private String method;

    public String getSysno() {
        return sysno;
    }

    public void setSysno(String sysno) {
        this.sysno = sysno;
    }

    public String getPermissionUrl() {
        return permissionUrl;
    }

    public void setPermissionUrl(String permissionUrl) {
        this.permissionUrl = permissionUrl;
    }

    public String getMethod() {
        return method;
    }

    public void setMethod(String method) {
        this.method = method;
    }

    public MyGrantedAuthority(String sysno, String permissionUrl, String method) {
        this.sysno = sysno;
        this.permissionUrl = permissionUrl;
        this.method = method;
    }

    @Override
    public String getAuthority() {
        return this.permissionUrl + ";" + this.method;
    }
}
