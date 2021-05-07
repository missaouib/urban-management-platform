package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.AuthenticationEntryPoint;

/**
 * @author liukai
 */
@EnableWebSecurity(debug = false)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private BrowserSecurityConfigurer browserSecurityConfigurer;

    @Autowired
    private AuthenticationEntryPoint authenticationEntryPoint;


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .authorizeRequests()
                .antMatchers(SystemConstant.PC_LOGIN_PAGE, SystemConstant.DEFAULT_LOGIN_PAGE).permitAll()
                .anyRequest().authenticated()
                .and()
                .logout().logoutSuccessUrl(SystemConstant.DEFAULT_LOGIN_PAGE)
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable();
        http.apply(browserSecurityConfigurer);
        http.exceptionHandling().authenticationEntryPoint(authenticationEntryPoint);
    }

    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers(
                "/captcha.jpeg",
                "/css/**",
                "/fonts/**",
                "/img/**",
                "/js/**",
                "/plugins/**",
                "/favicon.ico"
        );
    }


}
