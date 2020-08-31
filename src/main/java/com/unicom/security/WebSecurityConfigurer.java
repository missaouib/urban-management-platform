package com.unicom.security;

import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;

/**
 * @author liukai
 */
@EnableWebSecurity(debug = true)
public class WebSecurityConfigurer extends WebSecurityConfigurerAdapter {


    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http.authorizeRequests().anyRequest().permitAll();
        http.headers().frameOptions().sameOrigin();
        http.csrf().disable();
//        http
//                .antMatcher("/**")
//                .authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .logout().logoutSuccessUrl("http://192.168.23.41:25047/cas/logout")
//                .and()
//                .oauth2Login()
//                .successHandler(new SimpleUrlAuthenticationSuccessHandler("/"));
    }

    @Override
    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.inMemoryAuthentication().withUser("user").password("{noop}user").authorities("ROLE_USER");
    }


    @Override
    public void configure(WebSecurity web) {
        web.ignoring().antMatchers("/page/**");
    }

}
