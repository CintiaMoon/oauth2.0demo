package com.hxgs.ssotest;

import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.boot.autoconfigure.security.oauth2.client.EnableOAuth2Sso;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;

/**
 * Created by Administrator on 2017-07-24.
 */
@EnableAutoConfiguration
@Configuration
@EnableOAuth2Sso
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class SecurityConfiguration extends WebSecurityConfigurerAdapter {



    @Override
    public void configure(WebSecurity web) throws Exception {
        web
                .ignoring()
                .antMatchers("/css/**", "/lib/**", "/js/**", "/img/**", "/styles/**", "/palettes/**", "/indexImage/**",
                        "/geoJson/**", "/images/**", "/yjxy/**", "/data/**", "/public/error/**");
//        "/resources/**",,   "/static/**"
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
              /*  .antMatchers("/", "/forecast**", "/live*//**", "/forcast*//**", "/index",  "/indexWarn","/login",
                        "/registration", "/webjars*//**", "/warmingPro", "/warmingCity", "/warningCounty").permitAll()*/
               /* .antMatchers("*//*").permitAll()*/
                .antMatchers("/test1").hasAnyAuthority("管理员","超级管理员")
                .antMatchers("/**").permitAll()
//                .antMatchers("/manager/**").hasAty("用户", "管理员","超级管理员")
                .and().logout().logoutSuccessHandler(new LoginOutSuccessHandler())
                .and().csrf().disable().csrf().ignoringAntMatchers("/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
    }
}
