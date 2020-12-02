package com.unicom.urban.management.web.framework.security;


import com.unicom.urban.management.web.framework.security.xss.XssFilter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.security.authentication.ProviderManager;
import org.springframework.security.config.annotation.SecurityConfigurerAdapter;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.DefaultSecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;
import org.springframework.security.web.context.request.async.WebAsyncManagerIntegrationFilter;
import org.springframework.security.web.savedrequest.RequestCacheAwareFilter;

import java.util.Collections;

/**
 * 浏览器登录配置
 *
 * @author liukai
 */
@Configuration
public class BrowserSecurityConfigurer extends SecurityConfigurerAdapter<DefaultSecurityFilterChain, HttpSecurity> {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Autowired
    private AuthenticationFailureHandler authenticationFailureHandler;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private UserDetailsService userDetailsService;

    @Autowired
    private StringRedisTemplate redisTemplate;

    @Override
    public void configure(HttpSecurity http) {
        UsernamePasswordCaptchaAuthenticationFilter usernamePasswordCaptchaAuthenticationFilter = new UsernamePasswordCaptchaAuthenticationFilter();
        usernamePasswordCaptchaAuthenticationFilter.setAuthenticationSuccessHandler(authenticationSuccessHandler);
        usernamePasswordCaptchaAuthenticationFilter.setAuthenticationFailureHandler(authenticationFailureHandler);
        usernamePasswordCaptchaAuthenticationFilter.setAuthenticationManager(new ProviderManager(Collections.singletonList(getUserDetailsAuthenticationProvider())));

        http.addFilterBefore(usernamePasswordCaptchaAuthenticationFilter, RequestCacheAwareFilter.class);
        http.addFilterBefore(new XssFilter(), WebAsyncManagerIntegrationFilter.class);

    }

    private UserDetailsAuthenticationProvider getUserDetailsAuthenticationProvider() {
        UserDetailsAuthenticationProvider authenticationProvider = new UserDetailsAuthenticationProvider();
        authenticationProvider.setUserDetailsService(userDetailsService);
        authenticationProvider.setPasswordEncoder(passwordEncoder);
        authenticationProvider.setRedisTemplate(redisTemplate);
        return authenticationProvider;
    }


}