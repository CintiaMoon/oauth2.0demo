package com.hxgs.ssotest;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.logout.LogoutSuccessHandler;
import org.springframework.security.web.authentication.logout.SecurityContextLogoutHandler;
import org.springframework.stereotype.Component;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

@Configuration
@Component
public class LoginOutSuccessHandler implements LogoutSuccessHandler {



    @Override
    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
        if (authentication != null) {
            new SecurityContextLogoutHandler().logout(httpServletRequest, null, null);
        }
        String url = "http://10.104.236.53:9000/exit";
        String requesturl = httpServletRequest.getRequestURL().toString();
        url+="?redirecturl="+requesturl.replace("logout","");
        httpServletResponse.sendRedirect(url);
    }
}
