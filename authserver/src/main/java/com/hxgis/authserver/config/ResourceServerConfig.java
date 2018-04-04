package com.hxgis.authserver.config;

        import org.springframework.context.annotation.Configuration;
        import org.springframework.security.config.annotation.web.builders.HttpSecurity;
        import org.springframework.security.oauth2.config.annotation.web.configuration.EnableResourceServer;
        import org.springframework.security.oauth2.config.annotation.web.configuration.ResourceServerConfigurerAdapter;

/**
 * Created by Cici on 2017/7/3.
 */
@Configuration
@EnableResourceServer
public class ResourceServerConfig extends ResourceServerConfigurerAdapter {
    @Override
    public void configure(HttpSecurity http) throws Exception {
        // @formatter:off
        http.requestMatchers().antMatchers("/me","/userDetail","/user","/privilege","/getUserByDepartmentId")
                .and().authorizeRequests().anyRequest().authenticated();
        // @formatter:on
    }
}
