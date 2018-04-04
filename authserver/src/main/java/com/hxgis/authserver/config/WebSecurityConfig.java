package com.hxgis.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.security.SecurityProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;
import org.springframework.security.web.csrf.CookieCsrfTokenRepository;
import org.springframework.security.web.util.matcher.AntPathRequestMatcher;

import javax.sql.DataSource;

/**
 *
 * @author Cici
 * @date 2017/7/3
 */
@Configuration
@Order(SecurityProperties.ACCESS_OVERRIDE_ORDER)
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {
    @Qualifier("dataSource")
    @Autowired
    DataSource dataSource;

    @Override
    public void configure(WebSecurity web) throws Exception {
        web.ignoring().antMatchers("/static/css/**", "/static/img/**", "/static/js/**");
    }

    @Autowired
    public void configAuthentication(AuthenticationManagerBuilder auth) throws Exception {
        auth.jdbcAuthentication().dataSource(dataSource)
                .usersByUsernameQuery(
                        " SELECT u.loginname as username,u.LOGINPASSWORD as password, u.ENABLED FROM SYS_USER  u  WHERE LOGINNAME=?")
                .authoritiesByUsernameQuery(
                        " SELECT LOGINNAME AS username,ROLENAME AS ROLE FROM SYS_USERROLE ur,SYS_ROLE r,SYS_USER u WHERE ur.userid = u.userid AND ur.roleid = r.roleid AND u.LOGINNAME =?");
    }

    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http
                .antMatcher("/**").authorizeRequests()
                .antMatchers("/", "/tokendelete","/login**", "/webjars/**","/registration").permitAll()
                .anyRequest().authenticated()
                .and()
                // TODO: put CSRF protection back into this endpoint
                .csrf()
                .requireCsrfProtectionMatcher(new AntPathRequestMatcher("/oauth/authorize"))
                .disable()
                .exceptionHandling()
                .authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint("/"))
                .and()
                .formLogin()
                .loginPage("/login").failureUrl("/?error=true")
                .usernameParameter("username")
                .passwordParameter("password")
                .permitAll()
                .and()
                .logout()
                .logoutSuccessUrl("/").permitAll()
                .and()
                .csrf()
                .ignoringAntMatchers("/**")
                .csrfTokenRepository(CookieCsrfTokenRepository.withHttpOnlyFalse());
        // @formatter:on
    }
}
