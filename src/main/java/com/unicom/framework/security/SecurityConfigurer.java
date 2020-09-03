package com.unicom.framework.security;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

/**
 * @author liukai
 */
@EnableWebSecurity(debug = false)
public class SecurityConfigurer extends WebSecurityConfigurerAdapter {

    @Autowired
    private AuthenticationSuccessHandler authenticationSuccessHandler;

    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
                .antMatcher("/**")
                .authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginPage("/login").permitAll()
                .successHandler(authenticationSuccessHandler)
                .and()
                .headers().frameOptions().sameOrigin()
                .and()
                .csrf().disable();
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        auth.userDetailsService(userDetailsService);
        auth.inMemoryAuthentication()
                .withUser("admin").password("{noop}admin").authorities("ROLE_USER")
                .and()
                .withUser("user").password("{noop}user").authorities("ROLE_USER");
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/page/**", "/css/**", "/dist/**", "/fonts/**", "/img/**", "/js/**", "/json/**", "/plugins/**", "/favicon.ico");
    }

}
