package com.hxgis.authserver.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.*;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.approval.UserApprovalHandler;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.request.DefaultOAuth2RequestFactory;
import org.springframework.security.oauth2.provider.token.DefaultTokenServices;
import org.springframework.security.oauth2.provider.token.TokenEnhancer;
import org.springframework.security.oauth2.provider.token.TokenEnhancerChain;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;

import javax.sql.DataSource;
import java.util.Arrays;

/**
 * 授权服务
 * 配置
 */
@Configuration
@EnableAuthorizationServer
public class OAuth2AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {

    @Autowired
    private DataSource dataSource;

    @Autowired
    @Qualifier("authenticationManagerBean")
    private AuthenticationManager authenticationManager;

    @Autowired
    private UserApprovalHandler userApprovalHandler;

    @Autowired
    ApprovalStore approvalStore;

    @Override
    public void configure(final AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
        oauthServer
                .allowFormAuthenticationForClients()
                .tokenKeyAccess("permitAll()")
                .checkTokenAccess("isAuthenticated()");
    }

    @Override
    public void configure(final ClientDetailsServiceConfigurer clients) throws Exception {
        // @formatter:off
        clients.jdbc(dataSource);
                /*.withClient("ssoclient")
                    .authorizedGrantTypes("password", "client_credentials","authorization_code")
                    .secret("secret")
                    .scopes("read")
                    .and()
                .withClient("apiclient")
                    .authorizedGrantTypes("password","refresh_token")
                    .secret("secret")
                    .scopes("read");*/
        // @formatter:on
    }

    @Override
    public void configure(final AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        // @formatter:off
        final TokenEnhancerChain tokenEnhancerChain = new TokenEnhancerChain();
        tokenEnhancerChain
                .setTokenEnhancers(Arrays.asList(tokenEnhancer()));

        endpoints.approvalStore(approvalStore)
                .tokenStore(tokenStore())
                .tokenEnhancer(tokenEnhancerChain)
                .authenticationManager(authenticationManager)
                .userApprovalHandler(userApprovalHandler);
        // @formatter:on
    }

    @Bean
    @Primary
    public DefaultTokenServices tokenServices() {
        final DefaultTokenServices defaultTokenServices = new DefaultTokenServices();
        defaultTokenServices.setAccessTokenValiditySeconds(-1);
        defaultTokenServices.setRefreshTokenValiditySeconds(-1);
        defaultTokenServices.setSupportRefreshToken(true);
        defaultTokenServices.setReuseRefreshToken(false);
        defaultTokenServices.setTokenStore(tokenStore());
        return defaultTokenServices;
    }

    @Bean
    public TokenEnhancer tokenEnhancer() {
        return new CustomTokenEnhancer();
    }

    @Bean
    public TokenStore tokenStore() {
        return new JdbcTokenStore(dataSource);
    }


    @Bean
    protected AuthorizationCodeServices authorizationCodeServices() {
        return new JdbcAuthorizationCodeServices(dataSource);
    }

    @Bean
    public JdbcClientDetailsService clientDetailsService() {
        return new JdbcClientDetailsService(dataSource);
    }


    protected static class Stuff {

        @Autowired
        private ClientDetailsService clientDetailsService;
        @Autowired
        DataSource dataSource;

//        @Autowired
//        private TokenStore tokenStore;

        @Bean
        public ApprovalStore approvalStore() throws Exception {
            JdbcApprovalStore store = new JdbcApprovalStore(dataSource);
//            store.(tokenStore);
            return store;
        }

        @Bean
        @Lazy
        @Scope(proxyMode = ScopedProxyMode.TARGET_CLASS)
        public UmeteoUserApprovalHandler userApprovalHandler() throws Exception {
            UmeteoUserApprovalHandler handler = new UmeteoUserApprovalHandler();
            handler.setApprovalStore(approvalStore());
            handler.setRequestFactory(new DefaultOAuth2RequestFactory(clientDetailsService));
            handler.setClientDetailsService(clientDetailsService);
            handler.setUseApprovalStore(true);
            return handler;
        }
    }
}
