package com.unicom.urban.management.api.framework.security;

import com.unicom.urban.management.service.password.PasswordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.core.AuthorizationGrantType;

/**
 * OAuth2认证服务
 *
 * @author liukai
 */
@Configuration
@EnableAuthorizationServer
public class AuthorizationServerConfigurer extends AuthorizationServerConfigurerAdapter {


    @Autowired
    private PasswordService passwordService;

    @Autowired
    private AuthenticationManager authenticationManager;

    @Override
    public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {
//        super.configure(security);
    }


    @Override
    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
        clients.inMemory()
                .withClient("app")
                .secret(passwordService.getDefaultPassword())
                .autoApprove(true)
                .accessTokenValiditySeconds(-1)
                .authorizedGrantTypes(AuthorizationGrantType.PASSWORD.getValue())
                .scopes("USER_INFO");
    }

    @Override
    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
        endpoints.authenticationManager(authenticationManager)
                .pathMapping("/oauth/token", "/oauth/token")
                .pathMapping("/oauth/authorize", "/oauth/authorize");
    }

}
