package com.hxgis.authserver.securityConfig;

import org.springframework.security.access.ConfigAttribute;

import javax.servlet.http.HttpServletRequest;


/**
 * @author Administrator
 */
public class MyConfigAttribute implements org.springframework.security.access.ConfigAttribute {

    private final HttpServletRequest httpServletRequest;

    public MyConfigAttribute(HttpServletRequest httpServletRequest) {
        this.httpServletRequest = httpServletRequest;
    }


    @Override
    public String getAttribute() {
        return null;
    }

    public HttpServletRequest getHttpServletRequest() {
        return httpServletRequest;
    }
}