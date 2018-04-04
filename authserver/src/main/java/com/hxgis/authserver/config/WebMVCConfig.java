package com.hxgis.authserver.config;

import com.hxgis.authserver.controller.AccessConfirmationController;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.web.servlet.config.annotation.EnableWebMvc;
import org.springframework.web.servlet.config.annotation.ResourceHandlerRegistry;
import org.springframework.web.servlet.config.annotation.ViewControllerRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurerAdapter;

/**
 * Created by Cici on 2017/7/6.
 */
@Configuration
@EnableWebMvc
public class WebMVCConfig extends WebMvcConfigurerAdapter {

    @Override
    public void addViewControllers(ViewControllerRegistry registry) {
//        registry.addViewController("/").setViewName("index");
        registry.addViewController("/access-denied").setViewName("accessdenied");
    }

    @Override
    public void addResourceHandlers(ResourceHandlerRegistry registry) {
        if (!registry.hasMappingForPattern("/static/**")) {
            registry.addResourceHandler("/static/**").addResourceLocations("classpath:/static/");
        }
    }

    @Bean
    public AccessConfirmationController accessConfirmationController(ClientDetailsService clientDetailsService,
                                                                     ApprovalStore approvalStore) {
        AccessConfirmationController accessConfirmationController = new AccessConfirmationController();
        accessConfirmationController.setClientDetailsService(clientDetailsService);
        accessConfirmationController.setApprovalStore(approvalStore);
        return accessConfirmationController;
    }

}
