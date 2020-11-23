package com.unicom.urban.management.web.framework.security;

import com.unicom.urban.management.common.constant.SystemConstant;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.web.authentication.LoginUrlAuthenticationEntryPoint;

/**
 * @author liukai
 */
@EnableWebSecurity(debug = false)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private BrowserSecurityConfigurer browserSecurityConfigurer;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.apply(browserSecurityConfigurer);
        http
                .authorizeRequests()
                .antMatchers(SystemConstant.LOGIN_PAGE).permitAll()
                .anyRequest().authenticated()
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable();
        http.exceptionHandling().authenticationEntryPoint(new LoginUrlAuthenticationEntryPoint(SystemConstant.LOGIN_PAGE));
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
